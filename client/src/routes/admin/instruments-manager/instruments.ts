import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { InstrumentsStyles as styles } from './instruments.styles';
import { InstrumentsTemplate as template } from './instruments.template';


@customElement({
  name: 'admin-instruments-manager',
  template,
  styles,
})
export class AdminInstrumentsManager extends GenesisElement {
  @User user: User;
}
