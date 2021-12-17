package db;

import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CheckoutsHelper {

    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    public void savePreCheckout(PreCheckoutQuery preCheckoutQuery) {

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        UUID id = UUID.randomUUID();
        String createdDate = formatter.format(new Date());

        String query = String.format("insert into public.checkouts (id, checkout_id, currency, amount, payload, tg_id, provider_payment_charge_id, telegram_payment_charge_id, created_date) VALUES ('%s', '%s', '%s', %s, '%s', '%s', null, null, '%s');",
                id, preCheckoutQuery.getId(), preCheckoutQuery.getCurrency(), preCheckoutQuery.getTotalAmount(),
                preCheckoutQuery.getInvoicePayload(), preCheckoutQuery.getFrom().getId(), createdDate);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(query).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
    }

    public void updateCheckoutWithPayment(SuccessfulPayment successfulPayment) {

        String query = String.format("update public.checkouts set (provider_payment_charge_id, telegram_payment_charge_id) = ('%s', '%s') where payload = '%s';",
                successfulPayment.getProviderPaymentChargeId(), successfulPayment.getTelegramPaymentChargeId(),
                successfulPayment.getInvoicePayload());

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.getPreparedStatement(query).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeConnections();
        }
    }
}
