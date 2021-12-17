package bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageProcessor {

    protected void sendMsg(AbsSender absSender, SendMessage sm) {
        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void sendInvoice(AbsSender absSender, SendInvoice si) {
        try {
            absSender.execute(si);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
