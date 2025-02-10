import global.genesis.bloomberg.InstrumentToSubscriptionConverter
import global.genesis.bloomberg.SubscriptionDataToDatabaseConverter

/**
 * This file defines the pipelines to connect and consume data from Bloomberg B-PIPE.
 *
 * Full documentation on pipelines may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/integrations/data-pipelines/
 *
 */
pipelines {

    val bloomberg = bloomberg {
        /*
        Add your connectivity information here.
         */
        addServer("localhost", 8194)
        applicationName = "TestApp"

        /*
        If you are using a Bloomberg cloud-hosted instance (also known as 'Zero Footprint')
        you will need to provide data on the security certificates you will be provided in
        order to connect.
        */
        pathToPk7 = "/certs/root.pk7"
        pathToPk12 = "certs/cert.pk12"
        pkPassword = "***********"

        /*
        Add any additional configuration properties here.
         */
        defaultSubscriptionService = "//blp/mktdata"
    }

    /*
    This pipeline listens to real-time updates on the instrument table
    and creates a data subscription for every instrument in the application
    universe.
     */
    pipeline(name = "Bloomberg Subscriptions") {
        source(dbBulkSubscribe<Instruments>())
            .map(InstrumentToSubscriptionConverter())
            .sink(bloomberg)
    }

    /*
    This pipeline listens to data updates received as a result of Bloomberg
    subscriptions and sinks them to the market data database table in order
    to update the TRADE_VIEW in real-time.
     */
    pipeline(name = "Bloomberg Data Updates") {
        source(bloomberg)
            .map(SubscriptionDataToDatabaseConverter())
            .sink(dbSink())
    }
}
