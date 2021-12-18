package bot.commands;

import bot.TaxBot;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageProcessor {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

    protected void sendMsg(AbsSender absSender, SendMessage sm) {
        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Error while sending message in commands. Exception: ", e);
        }
    }

    protected void sendInvoice(AbsSender absSender, SendInvoice si) {
        try {
            absSender.execute(si);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Error while sending invoice. Exception: ", e);
        }
    }
}
