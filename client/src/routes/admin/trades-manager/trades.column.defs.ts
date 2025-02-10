import { ColDef } from '@ag-grid-community/core';
import { getNumberFormatter, getDateFormatter } from '@genesislcap/foundation-utils';

export const columnDefs: ColDef[] = [
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
    field: "TRADEID",
  }
]
