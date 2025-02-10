package global.genesis.bloomberg

import global.genesis.bloomberg.api.BloombergEvent
import global.genesis.bloomberg.api.DataField
import global.genesis.bloomberg.api.SubscriptionDataEvent
import global.genesis.db.entity.TableEntity
import global.genesis.gen.dao.MarketData
import global.genesis.pipeline.api.SuspendElementOperator
import global.genesis.pipeline.api.db.DbOperation

/**
 * Implementation of [SuspendElementOperator] for Data Pipelines that
 * converts subscription update events received from the Bloomberg Client
 * to market data table updates, that will update the database and also the
 * UI grids in real time.
 */
class SubscriptionDataToDatabaseConverter : SuspendElementOperator<BloombergEvent, DbOperation<out TableEntity>> {

    override suspend fun apply(input: BloombergEvent): DbOperation<out TableEntity>? {
        return when (input) {
            is SubscriptionDataEvent -> createMarketDataUpsert(input)

            else -> null
        }
    }

    private fun createMarketDataUpsert(event: SubscriptionDataEvent): DbOperation.Upsert<MarketData> {
        // We need to keep track of which fields have been modified
        // to avoid doing a db lookup to get the current record state
        val modifiedFields = mutableListOf<String>()

        // Here we construct the record, checking for the presence of each Bloomberg FID in the update
        val update = MarketData {
            instrumentid = event.correlationId.toLong()

            val askField = event.data[ASK]
            askField?.let {
                askPx = askField.getDouble(0.0)
                modifiedFields += "ASK_PX"
            }

            val askSizeField = event.data[ASK_SIZE]
            askSizeField?.let {
                val askSizeValue = askSizeField.getInt(0)?.toLong()
                if (askSizeValue != null) {
                    askSize = askSizeValue
                    modifiedFields += "ASK_SIZE"
                }
            }

            val bidField = event.data[BID]
            bidField?.let {
                bidPx = bidField.getDouble(0.0)
                modifiedFields += "BID_PX"
            }

            val bidSizeField = event.data[BID_SIZE]
            bidSizeField?.let {
                val bidSizeValue = bidSizeField.getInt(0)?.toLong()
                if (bidSizeValue != null) {
                    bidSize = bidSizeValue
                    modifiedFields += "BID_SIZE"
                }
            }
        }

        return DbOperation.Upsert(update, modifiedFields)
    }

    private fun DataField?.getDouble(defaultValue: Double): Double {
        return (this as? DataField.DoubleField)?.value ?: defaultValue
    }

    private fun DataField?.getInt(defaultValue: Int): Int {
        return (this as? DataField.IntField)?.value ?: defaultValue
    }
}