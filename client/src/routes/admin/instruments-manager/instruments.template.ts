import { html, whenElse, repeat } from '@genesislcap/web-core';
import { getViewUpdateRightComponent } from '../../../utils';
import type { AdminInstrumentsManager } from './instruments';
import { createFormSchema } from './instruments.create.form.schema';
import { updateFormSchema } from './instruments.update.form.schema';
import { columnDefs } from './instruments.column.defs';


export const InstrumentsTemplate = html<AdminInstrumentsManager>`
    ${whenElse(
        (x) => getViewUpdateRightComponent(x.user, ''),
        html`
            <entity-management
                design-system-prefix="rapid"
                header-case-type="capitalCase"
                enable-row-flashing
                enable-cell-flashing
                resourceName="INSTRUMENTS"
                createEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_INSTRUMENTS_INSERT')}"
                :createFormUiSchema=${() => createFormSchema }
                updateEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_INSTRUMENTS_MODIFY')}"
                :updateFormUiSchema=${() => updateFormSchema}
                deleteEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_INSTRUMENTS_DELETE')}"
                :datasourceConfig=${() => ({pollingInterval: 5000 })}
                entityLabel="Instruments"
                :columns=${() => columnDefs }
                modal-position="centre"
                size-columns-to-fit
            ></entity-management>
        `,
        html`
          <not-permitted-component></not-permitted-component>
        `,
    )}`;
