/**
  * This file defines request-replies of the application. 
  * Request-Replies provide snapshot data from a table or view in response to a request from the front end.
  * Once the response is received, the transaction is over (unlike a Data Server, which stays connected to the client and pushes updates)

  * Full documentation on request server may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/snapshot-queries-request-server/

 */
requestReplies {
  requestReply(MARKET_DATA)
  requestReply(TRADES)
  requestReply(INSTRUMENTS)
  requestReply(TRADES_VIEW)
}
