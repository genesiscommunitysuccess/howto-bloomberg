import { ColDef } from '@ag-grid-community/core';
import { getNumberFormatter, getDateFormatter } from '@genesislcap/foundation-utils';

export const columnDefs: ColDef[] = [
  {
    field: "INSTRUMENTID",
  },
  {
    field: "SECURITY_NAME",
  },
  {
    field: "CCY",
  },
  {
    field: "BBG_TICKER",
  }
]
