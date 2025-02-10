import { UiSchema } from '@genesislcap/foundation-forms';

export const updateFormSchema: UiSchema = {
  "type": "VerticalLayout",
  "elements": [
    {
      "type": "Control",
      "label": "Instrumentid",
      "scope": "#/properties/INSTRUMENTID",
      "options": {
        "readonly": true
      }
    },
    {
      "type": "Control",
      "label": "Security Name",
      "scope": "#/properties/SECURITY_NAME",
      "options": {}
    },
    {
      "type": "Control",
      "label": "Ccy",
      "scope": "#/properties/CCY",
      "options": {}
    },
    {
      "type": "Control",
      "label": "Bbg Ticker",
      "scope": "#/properties/BBG_TICKER",
      "options": {}
    }
  ]
}
