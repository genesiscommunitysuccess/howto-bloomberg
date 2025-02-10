import { html } from '@genesislcap/web-core';
import { persistLayout } from '../../utils';
import type { Home } from './home';
import { HomeAllTradesGrid } from './all-trades-grid';

HomeAllTradesGrid;

export const HomeTemplate = html<Home>`
  <rapid-layout auto-save-key="${() => persistLayout('HOME_1736180658921')}">
     <rapid-layout-region>
         <rapid-layout-item title="All Trades">
             <home-all-trades-grid></home-all-trades-grid>
         </rapid-layout-item>
     </rapid-layout-region>
  </rapid-layout>
`;
