# bloomberg

## Introduction

This application has been designed to demonstrate how to connect to a Bloomberg SAPI or B-PIPE instance in order to consume financial markets data.

There are two ways to utilise the Genesis Bloomberg Client.

1. Using the Bloomberg connector within Genesis Data Pipelines which acts as a pipeline source and sink
2. Using the Bloomberg client directly within your custom code

This application demonstrates both cases, and in both, the core concept is the same. For every instrument defined in the INSTRUMENT table, we create a price subscription for real time market data and send it to Bloomberg.
When data updates are received from Bloomberg, we write them into the MARKET_DATA table in order to update the UI in real-time.

Please note, it is not currently possible to run this project directly after check-out without modifications due to the requirement to connect to a real B-PIPE instance. 
When you get to the step in the guide where you need to configure connectivity, you will need to enter the host and port details of the B-PIPE instance you want to connect to.
These settings can be set in the [howto-bloomberg-system-definition.kts](server/howto-bloomberg-app/src/main/genesis/cfg/howto-bloomberg-system-definition.kts) file. 

For more detailed technical documentation, you can visit the Bloomberg API page on our [documentation portal](https://docs.genesis.global/docs/develop/business-components/bloomberg/).

## Adding Bloomberg to your Genesis application

In order to integrate with Bloomberg in your Genesis Application, we must first add the relevant gradle dependencies in the `build.gradle.kts` file of our main application module. See [build.gradle.kts](server/howto-bloomberg-app/build.gradle.kts).

The next steps depend on which method you are using to interact with the Bloomberg client.

### Using data pipelines
Using data pipelines is the easiest way to connect to Bloomberg, with the least amount of code.

1. Create the mapper to create subscriptions from instruments:

   In order to create a market data subscription for every instrument in the database, we need to write a small piece of custom logic to convert an Instrument record to a Bloomberg DataSubscription.
   This is handled by the [InstrumentToSubscriptionConverter](server/howto-bloomberg-app/src/main/kotlin/global/genesis/bloomberg/InstrumentToSubscriptionConverter.kt) class.

2. Create the mapper to create market data updates from data events:

   In order to create a market data record update for every data event that comes from Bloomberg, we need to write a small piece of custom logic to convert the data update to a market data record.
   This is handled by the [SubscriptionDataToDatabaseConverter](server/howto-bloomberg-app/src/main/kotlin/global/genesis/bloomberg/SubscriptionDataToDatabaseConverter.kt) class.

3. Create the pipelines script to configure the connectivity and connect everything together
   - see [howto-bloomberg-pipelines.kts](server/howto-bloomberg-app/src/main/genesis/scripts/howto-bloomberg-pipelines.kts).

4. Set up the Pipeline process

    We finally need to create a microservice that will run the pipeline definitions we have created, so we need to create a process and service definition:
   - see [howto-bloomberg-processes.xml](server/howto-bloomberg-app/src/main/genesis/cfg/howto-bloomberg-processes.xml)
   - see [howto-bloomberg-service-definitions.xml](server/howto-bloomberg-app/src/main/genesis/cfg/howto-bloomberg-service-definitions.xml)

### Using the client directly
Using the client directly gives more flexibility and control in how you subscribe to market data, as well as making it much easier to feed market data updates into custom code.

1. Create the custom bloomberg configuration

   When using the client directly, it will attempt to bootstrap itself from a GPAL script matching the pattern `*-bloomberg.kts`. Therefore, we need to create a script for the configuration. 
   This file uses the same structure as the data pipelines configuration. 
      - see [custom-bloomberg.kts](server/howto-bloomberg-app/src/main/genesis/scripts/custom-bloomberg.kts)

2. Create a custom class to inject the client

   We now need to create a custom module to utilise the client.
      - see [BloombergSubscriber](server/howto-bloomberg-app/src/main/kotlin/global/genesis/bloomberg/BloombergSubscriber.kt)

3. Write the custom business logic to create subscriptions from instruments
   - see [initialise() and createDataSubscription() methods](server/howto-bloomberg-app/src/main/kotlin/global/genesis/bloomberg/BloombergSubscriber.kt)

4. Write the custom business logic to create market data records from data updates
   
   - see [onSubscriptionUpdated() method](server/howto-bloomberg-app/src/main/kotlin/global/genesis/bloomberg/BloombergSubscriber.kt)

5. Set up the custom process

   We finally need to create a microservice that will run the custom module we have created, so we need to create a process and service definition:
    - see [howto-bloomberg-processes.xml](server/howto-bloomberg-app/src/main/genesis/cfg/howto-bloomberg-processes.xml)
    - see [howto-bloomberg-service-definitions.xml](server/howto-bloomberg-app/src/main/genesis/cfg/howto-bloomberg-service-definitions.xml)

## Running the app

Please see the [Quick Start](https://docs.genesis.global/docs/develop/development-environments/) guide for information on how to start up the application.

If you need an introduction to the Genesis platform and its modules it's worth heading [here](https://docs.genesis.global/docs/develop/platform-overview/).

## Project Structure

This project contains **server** and **client** directories which contain the server and client code respectively.

### Server

The server code for this project can be found [here](./server/README.md).
It is built using a DSL-like definition based on the Kotlin language: GPAL.

When first opening the project, if you receive a notification from IntelliJ IDE detecting Gradle project select the option to 'Load as gradle project'.

### Web client

The Web client for this project can be found [here](./client/README.md). It is built using Genesis's next
generation web development framework, which is based on Web Components.

# License

This is free and unencumbered software released into the public domain. For full terms, see [LICENSE](./LICENSE)

**NOTE** This project uses licensed components listed in the next section, thus licenses for those components are required during development.

## Licensed components
Genesis application platform
# Test results
BDD test results can be found [here](https://genesiscommunitysuccess.github.io/howto-bloomberg/test-results)
