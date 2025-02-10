import { UiSchema } from '@genesislcap/foundation-forms';

export const createFormSchema: UiSchema = {
  "type": "VerticalLayout",
  "elements": [
    {
      "type": "Control",
      "label": "Trade Date",
      "scope": "#/properties/TRADE_DATE",
      "options": {}
    },
    {
      "type": "Control",
      "label": "Side",
      "scope": "#/properties/SIDE",
      "options": {}
    },
    {
      "type": "Control",
      "label": "Volume",
      "scope": "#/properties/VOLUME",
      "options": {}
    },
    {
      "type": "Control",
      "label": "Price",
      "scope": "#/properties/PRICE",
      "options": {}
    },
    {
      "type": "Control",
      "label": "Instrumentid",
      "scope": "#/properties/INSTRUMENTID",
      "options": {
        "allOptionsResourceName": "INSTRUMENTS",
        "valueField": "INSTRUMENTID",
        "labelField": "INSTRUMENTID"
      }
    },
    {
      "type": "Control",
      "label": "Tradeid",
      "scope": "#/properties/TRADEID",
      "options": {
        "hidden": true
      }
    }
  ]
}
