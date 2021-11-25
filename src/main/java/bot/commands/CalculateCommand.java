package bot.commands;

import bot.Constants;
import bot.enums.State;
import bot.TaxBot;
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
        String response = Constants.CALCULATE_REPLY_PART_1 + TaxBot.getFormat(message.getChatId()) + Constants.CALCULATE_REPLY_PART_2;
        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId().toString());
        sm.setText(response);
        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        TaxBot.stateMap.put(message.getChatId(), State.CALCULATE);
    }
}
