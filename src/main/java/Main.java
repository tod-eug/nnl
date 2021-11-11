import bot.TaxBot;
import db.DatabaseHelper;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import util.PropertiesProvider;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) {

        PropertiesProvider.setup();

        DatabaseHelper databaseHelper = new DatabaseHelper();

        String insertQuery = String.format("insert into exchange_rate (\"id\", \"currency\", \"date\", \"nominal\", \"value\") VALUES ('7a101829-c3a0-4379-9968-e0dc5835a56f', 'USD', '2021-03-02', 1, 74.0448);");

        try {
            databaseHelper.getPreparedStatement(insertQuery).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseHelper.closeConnections();
        }

//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot(new TaxBot());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }
}
