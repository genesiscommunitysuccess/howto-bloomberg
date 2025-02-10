/**
  * This file defines the views for this application. Views provide a way to
  * create a view across entities which are more friendly, for example joining a
  * trade to an instrument to provide the details of the instrument for
  * presentation next to the trade.
  *
  * Views also allow for the addition of dynamic columns calculated in real time.
  * 
  * Views may be used across the system including as inputs to consolidators,
  * expose as APIs in the request response server as snapshots of data or as real
  * time ticking APIs in the data server.

  * Full documentation on views may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/data-model/

 */

views {
  view("TRADES_VIEW", TRADES) {
    joins {
      /**
        * In below example backward join indicates that updates to the joined entity should also update values in this view
        * joining(OTHER_ENTITY, backwardsJoin = true) {
        *     on( THIS_ENTITY { THIS_ENTITY_FOREIGN_KEY } to OTHER_ENTITY { PRIMARY_KEY })
        * }
        */
      joining(INSTRUMENTS, backwardsJoin = true) {
        on(TRADES { INSTRUMENTID } to INSTRUMENTS { INSTRUMENTID })
        .joining(MARKET_DATA, backwardsJoin = true) {
          on(INSTRUMENTS { INSTRUMENTID } to MARKET_DATA { INSTRUMENTID })
        }
      }
    }
    fields {
      TRADES.TRADEID
      TRADES.TRADE_DATE
      TRADES.SIDE
      TRADES.VOLUME
      TRADES.PRICE
      TRADES.INSTRUMENTID
      INSTRUMENTS.SECURITY_NAME
      INSTRUMENTS.CCY
      INSTRUMENTS.BBG_TICKER
      MARKET_DATA.BID_PX
      MARKET_DATA.BID_SIZE
      MARKET_DATA.ASK_SIZE
      MARKET_DATA.ASK_PX
      /**
        * You can expand the list of inputs from all joined entities to perform more  * complex calculations as needed
        *   derivedField("FIELD_NAME", DOUBLE) {
        *     withInput(THIS_ENTITY.ATTR1, OTHER_ENTITY.ATTR2) {
        *         attr1, attr2 -> attr1 * attr2
        *     }
        *   }
        *
        *   Find more : https://docs.genesis.global/docs/develop/server-capabilities/data-model/#derivedfield
        */
    }
  }

}
