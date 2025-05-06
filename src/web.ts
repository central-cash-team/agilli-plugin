import { WebPlugin } from '@capacitor/core';

import type { AgilliIntegrationPluginPlugin } from './definitions';

export class AgilliIntegrationPluginWeb extends WebPlugin implements AgilliIntegrationPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
