package com.centralcash.agilli.plugin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import br.com.agilli.sdk.AgilliPayments;
import br.com.agilli.sdk.AgilliUtil;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;


@CapacitorPlugin(name = "AgilliIntegrationPlugin")
public class AgilliIntegrationPlugin extends Plugin {
    private static final String TAG = "AgilliIntegration";
    private PluginCall savedCall;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void load() {
        super.load();

        // Inicializa o launcher assim que o plugin é carregado
        activityResultLauncher = getActivity().registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::handleActivityResult
        );
    }

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

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void startDebitPayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setDebitPayment(amount)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void startVoucherPayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setVoucherPayment(amount)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void startPixPayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setPixPayment(amount)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void startParcelamentoInteligentePayment(PluginCall call) {
        String amount = call.getString("amount", "0");
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForPaymentBuilder()
            .setParcelamentoInteligentePayment(amount)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void startReversal(PluginCall call) {
        String numDoc = call.getString("numDoc", "0");
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForReversalBuilder()
            .setNumDoc(numDoc)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void startReprint(PluginCall call) {
        String numDoc = call.getString("numDoc", "0");
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForReprintBuilder()
            .setNumDoc(numDoc)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void lockApp(PluginCall call) {
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForControlAppAccessBuilder()
            .setLockApp(true)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void unlockApp(PluginCall call) {
        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent intent = payments.intentForControlAppAccessBuilder()
            .setLockApp(false)
            .build();

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    @PluginMethod
    public void printTextAndImage(PluginCall call) {
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

        savedCall = call;
        activityResultLauncher.launch(intent);
    }

    private void handleActivityResult(ActivityResult result) {
        Log.d(TAG, "handleActivityResult: resultCode=" + result.getResultCode());

        if (savedCall == null) {
            Log.w(TAG, "No savedCall to resolve.");
            return;
        }

        Activity activity = getActivity();
        AgilliPayments payments = AgilliPayments.getInstance(activity);

        Intent data = result.getData();
        if (data != null) {
            payments.getPaymentsForData(data);
        }

        JSObject resultObj = new JSObject();

        if (payments.getActionStatus() == AgilliPayments.ACTION_RESULT_OK) {
            Log.d(TAG, "Pagamento concluído com sucesso. TxID=" + payments.getTxTransactionId());
            resultObj.put("status", "success");
            resultObj.put("transactionId", payments.getTxTransactionId());
            resultObj.put("numDoc", payments.getNumDoc());
            resultObj.put("nsu", payments.getNsu());
            resultObj.put("authorizationCode", payments.getCtfAuthorizer());

            // Extras
            putIfNotNull(resultObj, "cardFlag", payments.getCardFlag());
            putIfNotNull(resultObj, "cardNumber", payments.getCardNumber());
            putIfNotNull(resultObj, "amount", payments.getAmount());
            putIfNotNull(resultObj, "cnpj", payments.getCnpj());
            putIfNotNull(resultObj, "transactionReturn", payments.getTransactionReturn());
            putIfNotNull(resultObj, "dataHoraAutorizacao", payments.getDataHoraAutorizacao());
            resultObj.put("responseType", payments.getResponseType());

            // Alias opcional (ou use outro método real, se existir)
            resultObj.put("transactionUniqueId", payments.getTxTransactionId());

            savedCall.resolve(resultObj);
        } else {
            Log.e(TAG, "Pagamento falhou. Erro: " + payments.getErrorMessage());
            resultObj.put("status", "error");
            resultObj.put("errorMessage", payments.getErrorMessage());
            savedCall.reject("Pagamento falhou.", payments.getErrorMessage());
        }

        savedCall = null;
    }

    @PluginMethod
    public void getSerialNumber(PluginCall call) {
        String serial;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            JSObject result = new JSObject();
            result.put("serial", serial);
            call.resolve(result);
        } catch (SecurityException e) {
            call.reject("Permissão negada para acessar o número de série.");
        }
    }



    private void putIfNotNull(JSObject obj, String key, String value) {
        if (value != null) {
            obj.put(key, value);
        }
    }
}
