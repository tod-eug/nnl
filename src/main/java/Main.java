import bot.TaxBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import util.PropertiesProvider;

import java.io.IOException;
import java.util.logging.LogManager;


public class Main {

    public static void main(String[] args) {

        try {
            LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e);
        }

        PropertiesProvider.setup();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TaxBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
