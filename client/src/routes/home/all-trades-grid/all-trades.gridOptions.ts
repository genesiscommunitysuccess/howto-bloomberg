import { GridOptionsConfig } from '@genesislcap/rapid-grid-pro';
import { getNumberFormatter, getDateFormatter } from '@genesislcap/foundation-utils';
export const gridOptions: GridOptionsConfig = 
  {
    columnDefs: [
     {
          field: "TRADE_DATE",
     },
     {
          field: "SIDE",
     },
     {
          field: "VOLUME",
          valueFormatter: getNumberFormatter("0,0", null),
     },
     {
          field: "PRICE",
          valueFormatter: getNumberFormatter("0,0.00", null),
     },
     {
          field: "INSTRUMENTID",
          hide: true,
     },
     {
          field: "SECURITY_NAME",
     },
     {
          field: "CCY",
     },
     {
          field: "TRADEID",
     },
     {
          field: "ASK_PX",
     },
     {
          field: "ASK_SIZE",
     },
     {
          field: "BID_PX",
     },
     {
          field: "BID_SIZE",
     }
],
  }
