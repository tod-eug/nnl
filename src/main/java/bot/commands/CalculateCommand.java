package bot.commands;

import bot.Constants;
import bot.enums.Format;
import bot.enums.State;
import bot.TaxBot;
import db.SubscriptionsHelper;
import db.FormatHelper;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import util.PropertiesProvider;

import java.util.Date;

public class CalculateCommand implements IBotCommand {
    @Override
    public String getCommandIdentifier() {
        return "calculate";
    }

    @Override
    public String getDescription() {
        return "set state to CALCULATE and calculate report";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        boolean payments = Boolean.parseBoolean(PropertiesProvider.configurationProperties.get("payments"));
        if (payments) {
            SubscriptionsHelper ah = new SubscriptionsHelper();
            if (ah.getSubscriptionEndDateByTgId(message.getFrom().getId()).before(new Date()))
                subscriptionInvalid(absSender, message);
            else
                subscriptionValid(absSender, message);
        } else {
            subscriptionValid(absSender, message);
        }
    }

    private void subscriptionInvalid(AbsSender absSender, Message message) {
        MessageProcessor mp = new MessageProcessor();
        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId().toString());
        sm.setText(Constants.SUBSCRIPTION_EXPIRED);
        mp.sendMsg(absSender, sm);
    }

    private void subscriptionValid(AbsSender absSender, Message message) {
        MessageProcessor mp = new MessageProcessor();
        FormatHelper ufh = new FormatHelper();
        Format format = ufh.getFormat(message.getChatId(), Format.pdf);
        String response = Constants.CALCULATE_REPLY_PART_1 + format + Constants.CALCULATE_REPLY_PART_2;
        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId().toString());
        sm.setText(response);
        mp.sendMsg(absSender, sm);

        SendMessage sendMeReport = new SendMessage();
        sendMeReport.setChatId(message.getChatId().toString());
        sendMeReport.setText(Constants.WAIT_INCOME_FILE);
        mp.sendMsg(absSender, sendMeReport);

        TaxBot.stateMap.put(message.getChatId(), State.CALCULATE);
    }
}
