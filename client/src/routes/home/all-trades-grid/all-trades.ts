import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { AllTradesStyles as styles } from './all-trades.styles';
import { AllTradesTemplate as template } from './all-trades.template';


@customElement({
  name: 'home-all-trades-grid',
  template,
  styles,
})
export class HomeAllTradesGrid extends GenesisElement {
  @User user: User;
}
