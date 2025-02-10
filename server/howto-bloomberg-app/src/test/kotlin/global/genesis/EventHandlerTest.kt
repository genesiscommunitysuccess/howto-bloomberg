import global.genesis.db.rx.entity.multi.AsyncEntityDb
import global.genesis.gen.dao.Instruments
import global.genesis.gen.dao.MarketData
import global.genesis.gen.dao.Trades
import global.genesis.message.core.event.EventReply
import global.genesis.testsupport.client.eventhandler.EventClientSync
import global.genesis.testsupport.jupiter.GenesisJunit
import global.genesis.testsupport.jupiter.ScriptFile
import global.genesis.testsupport.jupiter.assertedCast
import javax.inject.Inject
import kotlin.String
import kotlin.Unit
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime.now
import org.joda.time.DateTime.parse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * This file tests the event handlers generated for your project.
 *
 * As you edit any event handler code, for example editing existing events or adding new, you
 * should try to extend this test suite to test the changes.
 *
 * Full documentation on event handler tests may be found here >>
 * https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/#testing
 */
@ExtendWith(GenesisJunit::class)
@ScriptFile("howto-bloomberg-eventhandler.kts")
class EventHandlerTest {
  @Inject
  lateinit var client: EventClientSync

  @Inject
  lateinit var entityDb: AsyncEntityDb

  private val adminUser: String = "admin"

  @Test
  fun `test insert MARKET_DATA`(): Unit = runBlocking {
    val result = client.sendEvent(
      details = MarketData {
        askPx = 0.1
        askSize = 1
        bidPx = 0.1
        bidSize = 1
        instrumentid = 1
      },
      messageType = "EVENT_MARKET_DATA_INSERT",
      userName = adminUser
    )
    result.assertedCast<EventReply.EventAck>()
    val marketData = entityDb.getBulk<MarketData>().toList()
    assertTrue(marketData.isNotEmpty())
  }

  @Test
  fun `test modify MARKET_DATA`(): Unit = runBlocking {
    val result = entityDb.insert(
      MarketData {
        askPx = 0.1
        askSize = 1
        bidPx = 0.1
        bidSize = 1
        instrumentid = 1
      }
    )
    val instrumentidValue = result.record.instrumentid
    val modifyResult = client.sendEvent(
      details = MarketData {
        askPx = 0.2
        askSize = 2
        bidPx = 0.2
        bidSize = 2
        instrumentid = instrumentidValue
      },
      messageType = "EVENT_MARKET_DATA_MODIFY",
      userName = adminUser
    )
    modifyResult.assertedCast<EventReply.EventAck>()
    val modifiedRecord = entityDb.get(MarketData.ByInstrumentid(instrumentidValue))
    assertEquals(0.2, modifiedRecord?.askPx)
    assertEquals(2, modifiedRecord?.askSize)
    assertEquals(0.2, modifiedRecord?.bidPx)
    assertEquals(2, modifiedRecord?.bidSize)
  }

  @Test
  fun `test delete MARKET_DATA`(): Unit = runBlocking {
    val result = entityDb.insert(
      MarketData {
        askPx = 0.1
        askSize = 1
        bidPx = 0.1
        bidSize = 1
        instrumentid = 1
      }
    )
    val numRecordsBefore = entityDb.getBulk<MarketData>().toList().size
    val instrumentidValue = result.record.instrumentid
    val deleteResult = client.sendEvent(
      details = MarketData.ByInstrumentid(instrumentidValue),
      messageType = "EVENT_MARKET_DATA_DELETE",
      userName = adminUser
    )
    deleteResult.assertedCast<EventReply.EventAck>()
    val numRecordsAfter = entityDb.getBulk<MarketData>().toList().size
    assertEquals(numRecordsBefore - 1, numRecordsAfter)
  }

  @Test
  fun `test insert TRADES`(): Unit = runBlocking {
    val result = client.sendEvent(
      details = Trades {
        instrumentid = 1
        price = 0.1
        side = "1"
        tradeDate = now()
        volume = 1
      },
      messageType = "EVENT_TRADES_INSERT",
      userName = adminUser
    )
    result.assertedCast<EventReply.EventAck>()
    val trades = entityDb.getBulk<Trades>().toList()
    assertTrue(trades.isNotEmpty())
  }

  @Test
  fun `test modify TRADES`(): Unit = runBlocking {
    val result = entityDb.insert(
      Trades {
        instrumentid = 1
        price = 0.1
        side = "1"
        tradeDate = now()
        volume = 1
      }
    )
    val tradeidValue = result.record.tradeid
    val modifyResult = client.sendEvent(
      details = Trades {
        instrumentid = 2
        price = 0.2
        side = "2"
        tradeDate = parse("2024-01-01T00:00:00.000Z")
        tradeid = tradeidValue
        volume = 2
      },
      messageType = "EVENT_TRADES_MODIFY",
      userName = adminUser
    )
    modifyResult.assertedCast<EventReply.EventAck>()
    val modifiedRecord = entityDb.get(Trades.ByTradeid(tradeidValue))
    assertEquals(2, modifiedRecord?.instrumentid)
    assertEquals(0.2, modifiedRecord?.price)
    assertEquals("2", modifiedRecord?.side)
    assertEquals(0, parse("2024-01-01T00:00:00.000Z").compareTo(modifiedRecord?.tradeDate))
    assertEquals(2, modifiedRecord?.volume)
  }

  @Test
  fun `test delete TRADES`(): Unit = runBlocking {
    val result = entityDb.insert(
      Trades {
        instrumentid = 1
        price = 0.1
        side = "1"
        tradeDate = now()
        volume = 1
      }
    )
    val numRecordsBefore = entityDb.getBulk<Trades>().toList().size
    val tradeidValue = result.record.tradeid
    val deleteResult = client.sendEvent(
      details = Trades.ByTradeid(tradeidValue),
      messageType = "EVENT_TRADES_DELETE",
      userName = adminUser
    )
    deleteResult.assertedCast<EventReply.EventAck>()
    val numRecordsAfter = entityDb.getBulk<Trades>().toList().size
    assertEquals(numRecordsBefore - 1, numRecordsAfter)
  }

  @Test
  fun `test insert INSTRUMENTS`(): Unit = runBlocking {
    val result = client.sendEvent(
      details = Instruments {
        bbgTicker = "1"
        ccy = "1"
        securityName = "1"
      },
      messageType = "EVENT_INSTRUMENTS_INSERT",
      userName = adminUser
    )
    result.assertedCast<EventReply.EventAck>()
    val instruments = entityDb.getBulk<Instruments>().toList()
    assertTrue(instruments.isNotEmpty())
  }

  @Test
  fun `test modify INSTRUMENTS`(): Unit = runBlocking {
    val result = entityDb.insert(
      Instruments {
        bbgTicker = "1"
        ccy = "1"
        securityName = "1"
      }
    )
    val instrumentidValue = result.record.instrumentid
    val modifyResult = client.sendEvent(
      details = Instruments {
        bbgTicker = "2"
        ccy = "2"
        instrumentid = instrumentidValue
        securityName = "2"
      },
      messageType = "EVENT_INSTRUMENTS_MODIFY",
      userName = adminUser
    )
    modifyResult.assertedCast<EventReply.EventAck>()
    val modifiedRecord = entityDb.get(Instruments.ByInstrumentid(instrumentidValue))
    assertEquals("2", modifiedRecord?.bbgTicker)
    assertEquals("2", modifiedRecord?.ccy)
    assertEquals("2", modifiedRecord?.securityName)
  }

  @Test
  fun `test delete INSTRUMENTS`(): Unit = runBlocking {
    val result = entityDb.insert(
      Instruments {
        bbgTicker = "1"
        ccy = "1"
        securityName = "1"
      }
    )
    val numRecordsBefore = entityDb.getBulk<Instruments>().toList().size
    val instrumentidValue = result.record.instrumentid
    val deleteResult = client.sendEvent(
      details = Instruments.ByInstrumentid(instrumentidValue),
      messageType = "EVENT_INSTRUMENTS_DELETE",
      userName = adminUser
    )
    deleteResult.assertedCast<EventReply.EventAck>()
    val numRecordsAfter = entityDb.getBulk<Instruments>().toList().size
    assertEquals(numRecordsBefore - 1, numRecordsAfter)
  }
}
