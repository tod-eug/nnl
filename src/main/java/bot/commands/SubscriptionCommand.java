package bot.commands;

import bot.Constants;
import db.AccessibilityHelper;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import util.PropertiesProvider;

import java.text.SimpleDateFormat;
import java.util.Date;



public class SubscriptionCommand implements IBotCommand {

    private static final String pattern = "dd.MM.yyyy HH:mm:ss";

    @Override
    public String getCommandIdentifier() {
        return "subscription";
    }

    @Override
    public String getDescription() {
        return "Buy subscription";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        AccessibilityHelper ah = new AccessibilityHelper();
        Date subscriptionEndDate = ah.getEndDateByTgId(message.getFrom().getId());

        if (subscriptionEndDate.before(new Date())) {
            subscribe(absSender, message);
        } else {
            subscriptionStillValid(absSender, message, subscriptionEndDate);
        }
    }

    private void subscribe(AbsSender absSender, Message message) {
        int amount = Integer.parseInt(PropertiesProvider.configurationProperties.get("amount"));
        LabeledPrice price = new LabeledPrice(Constants.INVOICE_ITEM_TITLE, amount * 100);

        SendInvoice si = SendInvoice.builder()
                .chatId(message.getChatId().toString())
                .title(Constants.INVOICE_TITLE)
                .description(Constants.INVOICE_DESCRIPTION)
                .payload("payload")
                .providerToken(PropertiesProvider.configurationProperties.get("paymentToken"))
                .startParameter(Constants.INVOICE_START_PARAMETER)
                .currency(PropertiesProvider.configurationProperties.get("currency"))
                .price(price)
                .build();
        try {
            absSender.execute(si);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void subscriptionStillValid(AbsSender absSender, Message message, Date subscriptionEndDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId().toString());
        sm.setText(Constants.SUBSCRIPTION_STILL_VALID + formatter.format(subscriptionEndDate));

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
