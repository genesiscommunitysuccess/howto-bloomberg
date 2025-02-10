/**
  * This file defines the event handler APIs. These APIs (modeled after CQRS
  * commands) allow callers to manipulate the data in the database. By default,
  * insert, update and delete event handlers (or commands) have been created.
  * These result in the data being written to the database and messages to be
  * published for the rest of the platform to by notified.
  * 
  * Custom event handlers may be added to extend the functionality of the
  * application as well as custom logic added to existing event handlers.
  *
  * The following objects are visible in each eventhandler
  * `event.details` which holds the entity that this event handler is acting upon
  * `entityDb` which is database object used to perform INSERT, MODIFY and UPDATE the records
  * Full documentation on event handler may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/

 */
eventHandler {
  eventHandler<MarketData>("MARKET_DATA_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<MarketData>("MARKET_DATA_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<MarketData.ByInstrumentid>("MARKET_DATA_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<Trades>("TRADES_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      // return an ack response which contains a list of record IDs
      ack(listOf(mapOf(
        "TRADEID" to insertedRow.record.tradeid,
      )))
    }
  }
  eventHandler<Trades>("TRADES_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Trades.ByTradeid>("TRADES_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<Instruments>("INSTRUMENTS_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      // return an ack response which contains a list of record IDs
      ack(listOf(mapOf(
        "INSTRUMENTID" to insertedRow.record.instrumentid,
      )))
    }
  }
  eventHandler<Instruments>("INSTRUMENTS_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Instruments.ByInstrumentid>("INSTRUMENTS_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }

  /**
    * If you want to provide some validation before the action, you need to have an onValidate block before the onCommit. The last value of the code block must always be the return message type.
    * eventHandler<THIS_ENTITY>(name = "THIS_ENTITY_INSERT") {
    *      onValidate { event ->
    *          val thisEntity = event.details
    *          require(thisEntity.name != null) { "ThisEntity should have a name" }
    *          ack()
    *      }
    *      onCommit { event ->
    *          ...
    *      }
    *  }
    * You can add permission to the query by using permission codes like below
    * permissioning {
    *     // 'permission Code' list, users must have the permission to access the enclosing resource
    *     permissionCodes = listOf("PERMISSION1", "PERMISSION2")
    * }
    * Full documentation on permissions may be found here https://docs.genesis.global/docs/develop/server-capabilities/access-control/authorization/
    */
}
