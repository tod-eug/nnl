package bot.commands;

import bot.Constants;
import bot.enums.Format;
import bot.enums.State;
import bot.TaxBot;
import db.FormatHelper;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        FormatHelper ufh = new FormatHelper();
        Format format = ufh.getFormat(message.getChatId(), Format.pdf);
        String response = Constants.CALCULATE_REPLY_PART_1 + format + Constants.CALCULATE_REPLY_PART_2;
        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId().toString());
        sm.setText(response);

        SendMessage sendMeReport = new SendMessage();
        sendMeReport.setChatId(message.getChatId().toString());
        sendMeReport.setText(Constants.WAIT_INCOME_FILE);
        try {
            absSender.execute(sm);
            absSender.execute(sendMeReport);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        TaxBot.stateMap.put(message.getChatId(), State.CALCULATE);
    }
}
