import { registerPlugin } from '@capacitor/core';
import type { AgilliIntegrationPlugin } from './definitions';

const AgilliIntegration = registerPlugin<AgilliIntegrationPlugin>('AgilliIntegrationPlugin');

export * from './definitions';
export { AgilliIntegration };
