import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { AdminStyles as styles } from './admin.styles';
import { AdminTemplate as template } from './admin.template';

@customElement({
  name: 'admin-route',
  template,
  styles,
})
export class Admin extends GenesisElement {
  @User user: User;

  constructor() {
    super();
  }
}
