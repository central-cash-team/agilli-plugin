export interface AgilliIntegrationPlugin {
    startCreditPayment(options: {
        amount: string;
        installments: number;
    }): Promise<void>;
    startDebitPayment(options: {
        amount: string;
    }): Promise<void>;
    startVoucherPayment(options: {
        amount: string;
    }): Promise<void>;
    startPixPayment(options: {
        amount: string;
    }): Promise<void>;
    startParcelamentoInteligentePayment(options: {
        amount: string;
    }): Promise<void>;
    startReversal(options: {
        numDoc?: number;
    }): Promise<void>;
    startReprint(options: {
        numDoc?: number;
    }): Promise<void>;
    lockApp(): Promise<void>;
    unlockApp(): Promise<void>;
    printTextAndImage(): Promise<void>;
}
