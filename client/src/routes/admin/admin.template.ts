import { html } from '@genesislcap/web-core';
import { persistLayout } from '../../utils';
import type { Admin } from './admin';
import { AdminInstrumentsManager } from './instruments-manager';
import { AdminTradesManager } from './trades-manager';

AdminInstrumentsManager;
AdminTradesManager;

export const AdminTemplate = html<Admin>`
  <rapid-layout auto-save-key="${() => persistLayout('ADMIN_1736180658922')}">
     <rapid-layout-region>
         <rapid-layout-item title="Instruments">
             <admin-instruments-manager></admin-instruments-manager>
         </rapid-layout-item>
         <rapid-layout-item title="Trades">
             <admin-trades-manager></admin-trades-manager>
         </rapid-layout-item>
     </rapid-layout-region>
  </rapid-layout>
`;
