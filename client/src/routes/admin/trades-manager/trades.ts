import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { TradesStyles as styles } from './trades.styles';
import { TradesTemplate as template } from './trades.template';


@customElement({
  name: 'admin-trades-manager',
  template,
  styles,
})
export class AdminTradesManager extends GenesisElement {
  @User user: User;
}
