package bot;

import controller.TaxesController;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import util.IBReportValidator;
import util.PropertiesProvider;

import java.io.*;
import java.util.List;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String response = "Для старта расчета просто пришли мне отчет Interactive Brokers в формате htm";
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(response);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (update.getMessage().hasDocument()) {
            String uploadedFilePath = getFilePath(update);
            File gotFile = getFile(uploadedFilePath);

            IBReportValidator ibReportValidator = new IBReportValidator();
            if (ibReportValidator.isFileIBReport(gotFile)) {
                //send message "Working on it"
                String response = "Произвожу расчеты";
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(response);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                //calculations
                TaxesController taxesController = new TaxesController();
                File file = taxesController.getCalculatedTaxes(gotFile);
                InputFile inputFile = new InputFile(file);

                SendDocument document = new SendDocument();
                document.setChatId(update.getMessage().getChatId().toString());
                document.setDocument(inputFile);
                document.setCaption(file.getName());
                try {
                    execute(document);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                String response = "Файл не распознан как отчет Interactive Brokers в формате htm";
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(response);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private String getFilePath(Update update) {
        String uploadedFileId = update.getMessage().getDocument().getFileId();
        GetFile uploadedFile = new GetFile();
        uploadedFile.setFileId(uploadedFileId);
        String uploadedFilePath = null;
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(uploadedFile);
            uploadedFilePath = file.getFilePath();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return uploadedFilePath;
    }

    private File getFile(String uploadedFilePath) {
        File file = null;
        try {
            file = downloadFile(uploadedFilePath);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return file;
    }
}
