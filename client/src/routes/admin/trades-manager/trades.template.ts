import { html, whenElse, repeat } from '@genesislcap/web-core';
import { getViewUpdateRightComponent } from '../../../utils';
import type { AdminTradesManager } from './trades';
import { createFormSchema } from './trades.create.form.schema';
import { updateFormSchema } from './trades.update.form.schema';
import { columnDefs } from './trades.column.defs';


export const TradesTemplate = html<AdminTradesManager>`
    ${whenElse(
        (x) => getViewUpdateRightComponent(x.user, ''),
        html`
            <entity-management
                design-system-prefix="rapid"
                header-case-type="capitalCase"
                enable-row-flashing
                enable-cell-flashing
                resourceName="TRADES"
                createEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_TRADES_INSERT')}"
                :createFormUiSchema=${() => createFormSchema }
                updateEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_TRADES_MODIFY')}"
                :updateFormUiSchema=${() => updateFormSchema}
                deleteEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_TRADES_DELETE')}"
                :datasourceConfig=${() => ({pollingInterval: 5000 })}
                entityLabel="Trades"
                :columns=${() => columnDefs }
                modal-position="centre"
                size-columns-to-fit
            ></entity-management>
        `,
        html`
          <not-permitted-component></not-permitted-component>
        `,
    )}`;
