package bot;

import controller.TradesController;
import dto.TradeRaw;
import dto.Trades;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import output.xlsx.TradesWriter;
import parser.TradesParser;
import util.DataProvider;
import util.PropertiesProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaxBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return PropertiesProvider.configurationProperties.get("BotName");
    }

    @Override
    public String getBotToken() {
        return PropertiesProvider.configurationProperties.get("Token");
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());


            File file = docs();
            InputFile inputFile = new InputFile(file);

            SendDocument document = new SendDocument();
            document.setChatId(update.getMessage().getChatId().toString());
            document.setDocument(inputFile);
            document.setCaption("123.xlsx");
            try {
                execute(message); // Call method to send the message
                execute(document);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private File docs() {
        //get document
        Document doc = DataProvider.getDocument();

        //get list of trades
        TradesParser tradesParser = new TradesParser();
        ArrayList<TradeRaw> tradesList = tradesParser.parseTrades(doc);

        //calculate trades
        TradesController tradesController = new TradesController();
        Map<String, List<Trades>> trades =  tradesController.calculateTrades(tradesList);
        trades.size();

        XSSFWorkbook workbook = new XSSFWorkbook();
        TradesWriter tradesWriter = new TradesWriter();
        workbook = tradesWriter.writeTrades(trades, workbook);

        String fileName = UUID.randomUUID() + ".xlsx";
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(fileName);

            return file;
    }
}
