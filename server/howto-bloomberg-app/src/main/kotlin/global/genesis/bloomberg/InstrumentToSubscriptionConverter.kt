package global.genesis.bloomberg

import global.genesis.bloomberg.api.BloombergRequest
import global.genesis.bloomberg.api.DataSubscription
import global.genesis.bloomberg.api.DataUnsubscribe
import global.genesis.db.updatequeue.Bulk
import global.genesis.gen.dao.Instruments
import global.genesis.pipeline.api.SuspendElementOperator

/**
 * Implementation of [SuspendElementOperator] for Data Pipelines that
 * converts subscription events received on the Instruments table to
 * requests that can be sent to the Bloomberg Client.
 */
class InstrumentToSubscriptionConverter : SuspendElementOperator<Bulk<Instruments>, BloombergRequest>{

    override suspend fun apply(input: Bulk<Instruments>): BloombergRequest? {
        return when (input) {
            Bulk.Prime.Completed, is Bulk.Update.Modify -> null

            // We create a subscription for every record in the database
            // on startup, or when a record is inserted
            is Bulk.Prime.Record -> createSubscription(input.record)
            is Bulk.Update.Insert -> createSubscription(input.record)

            // When an instrument is deleted, we unsubscribe
            is Bulk.Update.Delete -> createUnsubscribe(input.record)
        }
    }

    private fun createSubscription(instruments: Instruments): BloombergRequest {

        // The DataSubscription class is a part of the Genesis Bloomberg
        // API, and how we initiate a data subscription
        return DataSubscription(
            correlationId = instruments.instrumentid.toString(),
            topic = instruments.bbgTicker,
            fields = listOf(
                BID,
                BID_SIZE,
                ASK,
                ASK_SIZE
            ),
            options = emptyList()
        )
    }

    private fun createUnsubscribe(instruments: Instruments): BloombergRequest {

        // The DataUnsubscribe is the companion operation to DataSubscription
        return DataUnsubscribe(
            correlationId = instruments.instrumentid.toString()
        )
    }
}