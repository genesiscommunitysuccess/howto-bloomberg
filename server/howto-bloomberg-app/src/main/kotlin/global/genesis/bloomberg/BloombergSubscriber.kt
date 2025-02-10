package global.genesis.bloomberg

import global.genesis.bloomberg.api.DataField
import global.genesis.bloomberg.api.DataSubscription
import global.genesis.bloomberg.api.EventListener
import global.genesis.bloomberg.api.SubscriptionDataEvent
import global.genesis.bloomberg.api.SubscriptionStatusEvent
import global.genesis.commons.annotation.Module
import global.genesis.db.EntityModifyDetails
import global.genesis.db.rx.entity.multi.SyncEntityDb
import global.genesis.db.rx.entity.multi.getBulk
import global.genesis.db.updatequeue.GenericRecordUpdate
import global.genesis.gen.dao.Instruments
import global.genesis.gen.dao.MarketData
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.inject.Inject

/**
 * Custom module that gives direct access to the [BloombergClient] object for
 * interacting with B-PIPE directly in custom code.
 *
 * The client will bootstrap itself as part of the app initialisation using a
 * GPAL file matching the pattern `*-bloomberg.kts`.
 *
 * This class also implements [EventListener] in the Genesis Bloomberg API
 * in order to function as a receiver of event callbacks when data is received from
 * Bloomberg.
 * */
@Module
class BloombergSubscriber @Inject constructor(
    private val bloombergClient: BloombergClient,
    private val entityDb: SyncEntityDb
) : EventListener {

    // This method will be called after the process initialisation has finished.
    // We will put our initialisation logic here.
    @PostConstruct
    fun initialise() {
        bloombergClient.addEventListener(this)

        // On start we create a subscription for every instrument in the database
        entityDb.getBulk<Instruments>().forEach { instrument ->
            bloombergClient.subscribe(createDataSubscription(instrument))
        }

        entityDb.subscribe(Instruments::class.java) { update ->
            when (update) {

                // When an instrument is inserted, we create a subscription for it
                is GenericRecordUpdate.Insert -> {
                    bloombergClient.subscribe(createDataSubscription(update.record))
                }

                // When an instrument is modified, if the Bloomberg symbol has changed, we must resubscribe
                is GenericRecordUpdate.Modify -> {
                    if (update.oldRecord.bbgTicker != update.newRecord.bbgTicker) {
                        bloombergClient.unsubscribe(update.newRecord.instrumentid.toString())
                        bloombergClient.subscribe(createDataSubscription(update.newRecord))
                    }

                }

                // When an instrument is deleted, we unsubscribe
                is GenericRecordUpdate.Delete -> {
                    bloombergClient.unsubscribe(update.record.instrumentid.toString())
                }
            }
        }
    }

    // Here we create the subscription. The fields correspond to the Bloomberg Field IDs
    // (FIDs) we want to receive updates for
    private fun createDataSubscription(instruments: Instruments): DataSubscription {
        return DataSubscription(
            correlationId = instruments.instrumentid.toString(),
            topic = instruments.bbgTicker,
            fields = listOf(
                "BID",
                "BID_SIZE",
                "ASK",
                "ASK_SIZE"
            ),
            options = emptyList()
        )
    }

    // This method is inherited from the EventListener interface and will be
    // invoked when a data update is received from Bloomberg
    override fun onSubscriptionUpdated(event: SubscriptionDataEvent) {

        // We need to keep track of which fields have been modified
        // to avoid doing a db lookup to get the current record state
        val modifiedFields = mutableListOf<String>()

        // Here we construct the record, checking for the presence of each Bloomberg FID in the update
        val update = MarketData {
            val askField = event.data[ASK]
            askField?.let {
                askPx = askField.getDouble(0.0)
                modifiedFields += ASK
            }

            val askSizeField = event.data[ASK_SIZE]
            askSizeField?.let {
                askSize = askSizeField.getLong(0L)
                modifiedFields += ASK_SIZE
            }

            val bidField = event.data[BID]
            bidField?.let {
                bidPx = bidField.getDouble(0.0)
                modifiedFields += BID
            }

            val bidSizeField = event.data[BID_SIZE]
            bidSizeField?.let {
                bidSize = bidSizeField.getLong(0L)
                modifiedFields += BID_SIZE
            }
        }

        entityDb.upsert(
            EntityModifyDetails(
                entity = update,
                fields = modifiedFields
            )
        )
    }

    private fun DataField?.getDouble(defaultValue: Double): Double {
        return (this as? DataField.DoubleField)?.value ?: defaultValue
    }

    private fun DataField?.getLong(defaultValue: Long): Long {
        return (this as? DataField.LongField)?.value ?: defaultValue
    }

    // This method is inherited from the EventListener interface and will be
    // invoked when a subscription changes status
    override fun onSubscriptionStatus(event: SubscriptionStatusEvent) {
        LOG.info("Subscription for instrument {} changed status to {}", event.correlationId, event.status)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BloombergSubscriber::class.java)
    }
}