package bot.commands;

import bot.Constants;
import db.SubscriptionsHelper;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.bots.AbsSender;
import util.PropertiesProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class SubscriptionCommand implements IBotCommand {

    private static final String pattern = "dd.MM.yyyy HH:mm:ss";

    @Override
    public String getCommandIdentifier() {
        return "subscription";
    }

    @Override
    public String getDescription() {
        return "Subscription status and purchase";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        SubscriptionsHelper ah = new SubscriptionsHelper();
        Date subscriptionEndDate = ah.getSubscriptionEndDateByTgId(message.getFrom().getId());

        if (subscriptionEndDate.before(new Date())) {
            subscribe(absSender, message);
        } else {
            subscriptionStillValid(absSender, message, subscriptionEndDate);
        }
    }

    private void subscribe(AbsSender absSender, Message message) {
        int amount = Integer.parseInt(PropertiesProvider.configurationProperties.get("amount"));
        LabeledPrice price = new LabeledPrice(Constants.INVOICE_ITEM_TITLE, amount * 100);

        MessageProcessor mp = new MessageProcessor();
        SendInvoice si = SendInvoice.builder()
                .chatId(message.getChatId().toString())
                .title(Constants.INVOICE_TITLE)
                .description(Constants.INVOICE_DESCRIPTION)
                .payload(UUID.randomUUID().toString())
                .providerToken(PropertiesProvider.configurationProperties.get("paymentToken"))
                .startParameter(Constants.INVOICE_START_PARAMETER)
                .currency(PropertiesProvider.configurationProperties.get("currency"))
                .price(price)
                .build();
        mp.sendInvoice(absSender, si);
    }

    private void subscriptionStillValid(AbsSender absSender, Message message, Date subscriptionEndDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        MessageProcessor mp = new MessageProcessor();
        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId().toString());
        sm.setText(Constants.SUBSCRIPTION_STILL_VALID + formatter.format(subscriptionEndDate));
        mp.sendMsg(absSender, sm);
    }
}
