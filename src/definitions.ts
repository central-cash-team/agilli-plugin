export interface PaymentResult {
  status: 'success';
  transactionId: string;
  numDoc: number;
  nsu: string;
  authorizationCode: string;
  cardFlag?: string;
  cardNumber?: string;
  amount?: string;
  cnpj?: string;
  transactionReturn?: string;
  dataHoraAutorizacao?: string;
  responseType: number;
}

export interface AgilliIntegrationPlugin {
  startCreditPayment(options: { amount: string; installments: number }): Promise<PaymentResult>;
  startDebitPayment(options: { amount: string }): Promise<PaymentResult>;
  startVoucherPayment(options: { amount: string }): Promise<PaymentResult>;
  startPixPayment(options: { amount: string }): Promise<PaymentResult>;
  startParcelamentoInteligentePayment(options: { amount: string }): Promise<PaymentResult>;
  startReversal(options: { numDoc?: number }): Promise<PaymentResult>;
  startReprint(options: { numDoc?: number }): Promise<PaymentResult>;
  lockApp(): Promise<void>;
  unlockApp(): Promise<void>;
  printTextAndImage(): Promise<void>;
}
