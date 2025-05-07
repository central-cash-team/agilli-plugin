package com.centralcash.agilli.plugin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import br.com.agilli.sdk.AgilliPayments;
import br.com.agilli.sdk.AgilliUtil;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;

import com.getcapacitor.JSObject;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

@CapacitorPlugin(name = "AgilliIntegrationPlugin")
public class AgilliIntegrationPlugin extends Plugin {
    private static final String TAG = "AgilliIntegration";
    private PluginCall savedCall;

    @PluginMethod
    public void startCreditPayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        int installments = call.getInt("installments", 1);

        Log.d(TAG, "startCreditPayment chamado com amount=" + amount + ", installments=" + installments);

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setCreditPayment(amount)
            .setInstallments(installments)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void startDebitPayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Log.d(TAG, "startDebitPayment chamado com amount=" + amount);

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setDebitPayment(amount)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void startVoucherPayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Log.d(TAG, "startVoucherPayment chamado com amount=" + amount);

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setVoucherPayment(amount)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void startPixPayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Log.d(TAG, "startPixPayment chamado com amount=" + amount);

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setPixPayment(amount)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void startParcelamentoInteligentePayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Log.d(TAG, "startParcelamentoInteligentePayment chamado com amount=" + amount);

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setParcelamentoInteligentePayment(amount)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void startReversal(PluginCall call) {
        String numDoc = call.getString("numDoc", "0");
        Log.d(TAG, "startReversal chamado com numDoc=" + numDoc);

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForReversalBuilder()
            .setNumDoc(numDoc)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void startReprint(PluginCall call) {
        String numDoc = call.getString("numDoc", "0");
        Log.d(TAG, "startReprint chamado com numDoc=" + numDoc);

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForReprintBuilder()
            .setNumDoc(numDoc)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void lockApp(PluginCall call) {
        Log.d(TAG, "lockApp chamado");

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForControlAppAccessBuilder()
            .setLockApp(true)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void unlockApp(PluginCall call) {
        Log.d(TAG, "unlockApp chamado");

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForControlAppAccessBuilder()
            .setLockApp(false)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    @PluginMethod
    public void printTextAndImage(PluginCall call) {
        Log.d(TAG, "printTextAndImage chamado");

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        String text = "      TESTE CADASTRO\n"
                + "CNPJ=00.305.345/699\n"
                + "R: MACHADO DE ASSIS,904 CENTRO\n"
                + "TERMINAL=BW000048\n"
                + "LOTE=000004 SEQ=119 AUT=046119\n"
                + "DOC=000118 04/09/2024 10:37:30\n"
                + "A VISTA - Valecard\n"
                + "CARTAO=6064*******466830 VALOR: R$ 50,00";

        int id = activity.getResources().getIdentifier("logo_imp", "drawable", activity.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), id);

        List<String> list = new ArrayList<>();
        AgilliUtil.addImageToPrintList(list, bitmap);
        AgilliUtil.addTextToPrintList(list, text);

        Intent intent = payments.intentForGenericPrintBuilder()
            .setObjectListToPrint(list)
            .build();

        launchAgilliIntent(activity, intent, call);
    }

    private void launchAgilliIntent(Activity activity, Intent intent, PluginCall call) {
        if (activity instanceof MainActivity) {
            Log.d(TAG, "Enviando intent para MainActivity lançar");
            ((MainActivity) activity).setPendingIntent(intent);
            ((MainActivity) activity).launchAgilliPayment();
            call.resolve();
        } else {
            Log.e(TAG, "Activity não é MainActivity!");
            call.reject("Activity não é MainActivity!");
        }
    }


    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "handleOnActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);


        if (savedCall == null) {
            return;
        }

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);
        payments.getPaymentsForData(data);
        int actionStatus = payments.getActionStatus();

        JSObject result = new JSObject();

        if (actionStatus == AgilliPayments.ACTION_RESULT_OK) {
            Log.d(TAG, "Pagamento concluído com sucesso. TxID=" + payments.getTxTransactionId());
            result.put("status", "success");
            result.put("transactionId", payments.getTxTransactionId());
            result.put("numDoc", payments.getNumDoc());
            result.put("nsu", payments.getNsu());
            result.put("authorizationCode", payments.getCtfAuthorizer());

            // 💡 Adicionando informações adicionais:
            String cardFlag = payments.getCardFlag();
            if (cardFlag != null) {
                result.put("cardFlag", cardFlag);
            }

            String cardNumber = payments.getCardNumber();
            if (cardNumber != null) {
                result.put("cardNumber", cardNumber);
            }

            String amount = payments.getAmount();
            if (amount != null) {
                result.put("amount", amount);
            }

            String cnpj = payments.getCnpj();
            if (cnpj != null) {
                result.put("cnpj", cnpj);
            }

            String transactionReturn = payments.getTransactionReturn();
            if (transactionReturn != null) {
                result.put("transactionReturn", transactionReturn);
            }

            String dataHoraAutorizacao = payments.getDataHoraAutorizacao();
            if (dataHoraAutorizacao != null) {
                result.put("dataHoraAutorizacao", dataHoraAutorizacao);
            }

            int responseType = payments.getResponseType();
            result.put("responseType", responseType);

            savedCall.resolve(result);
        } else {
            Log.e(TAG, "Pagamento falhou. Erro: " + payments.getErrorMessage());
            result.put("status", "error");
            result.put("errorMessage", payments.getErrorMessage());
            savedCall.reject("Pagamento falhou.", payments.getErrorMessage());
        }

        savedCall = null;
    }



}
