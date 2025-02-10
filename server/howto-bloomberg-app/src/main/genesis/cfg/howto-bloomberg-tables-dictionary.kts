/**
  * This file defines the entities (or tables) for the application.  
  * Entities aggregation a selection of the universe of fields defined in 
  * {app-name}-fields-dictionary.kts file into a business entity.  
  *
  * Note: indices defined here control the APIs available to the developer.
  * For example, if an entity requires lookup APIs by one or more of its attributes, 
  * be sure to define either a unique or non-unique index.

  * Full documentation on tables may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/data-model/

 */

tables {
  table(name = "MARKET_DATA", id = 11_000) {
    field("ASK_PX", DOUBLE)
    field("ASK_SIZE", LONG)
    field("BID_PX", DOUBLE)
    field("BID_SIZE", LONG)
    field("INSTRUMENTID", LONG).notNull()

    primaryKey("INSTRUMENTID")

  }
  table(name = "TRADES", id = 11_001) {
    field("TRADEID", LONG).autoIncrement()
    field("INSTRUMENTID", LONG).notNull()
    field("PRICE", DOUBLE)
    field("SIDE", STRING)
    field("TRADE_DATE", DATE)
    field("VOLUME", LONG)

    primaryKey("TRADEID")

  }
  table(name = "INSTRUMENTS", id = 11_002) {
    field("INSTRUMENTID", LONG).autoIncrement()
    field("BBG_TICKER", STRING(100)).notNull().metadata {
      maxLength = 100
    }
    field("CCY", STRING)
    field("SECURITY_NAME", STRING)

    primaryKey("INSTRUMENTID")

  }

}
