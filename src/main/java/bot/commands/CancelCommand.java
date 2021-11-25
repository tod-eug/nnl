package bot.commands;

import bot.TaxBot;
import bot.enums.State;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class CancelCommand implements IBotCommand {
    @Override
    public String getCommandIdentifier() {
        return "cancel";
    }

    @Override
    public String getDescription() {
        return "set state to FREE";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        TaxBot.stateMap.put(message.getChatId(), State.FREE);
    }
}
