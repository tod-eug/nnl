package bot.commands;

import bot.Constants;
import bot.KeyboardFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class FormatCommand implements IBotCommand {
    @Override
    public String getCommandIdentifier() {
        return "format";
    }

    @Override
    public String getDescription() {
        return "set output format";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        MessageProcessor mp = new MessageProcessor();
        String response = Constants.FORMAT_REPLY;
        SendMessage sm = new SendMessage();
        sm.setChatId(message.getChatId().toString());
        sm.setText(response);
        sm.setReplyMarkup(KeyboardFactory.withPdfXlsButtons());
        mp.sendMsg(absSender, sm);
    }
}
