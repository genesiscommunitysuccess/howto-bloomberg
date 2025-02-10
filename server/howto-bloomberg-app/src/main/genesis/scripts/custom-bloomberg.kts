/*
 This file is used when injecting the bloomberg client using
 dependency injection into custom code. The structure and configuration
 options are the same as when using the Bloomberg connector within
 Data Pipelines.
 */
bloomberg {
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