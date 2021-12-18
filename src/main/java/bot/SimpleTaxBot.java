package bot;

import bot.enums.Format;
import controller.TaxesController;
import db.UsersHelper;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleTaxBot extends TelegramLongPollingBot {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());

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
            if (update.getMessage().getText().startsWith("/start")) {
                UsersHelper uh = new UsersHelper();
                String userId = uh.findUserByTgId(update.getMessage().getFrom().getId().toString(), update.getMessage().getFrom());
                String response = "Добро пожаловать!";
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(response);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.log(Level.SEVERE, "Error while sending message. Exception: ", e);
                }
            }
            String response = "Для старта расчета просто пришли мне отчет Interactive Brokers в формате htm";
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(response);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.log(Level.SEVERE, "Error while sending message. Exception: ", e);
            }
        }

        if (update.getMessage().hasDocument()) {
            UsersHelper uh = new UsersHelper();
            String userId = uh.findUserByTgId(update.getMessage().getFrom().getId().toString(), update.getMessage().getFrom());
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
                    log.log(Level.SEVERE, "Error while sending message. Exception: ", e);
                }
                //calculations
                TaxesController taxesController = new TaxesController();
                File file = taxesController.getCalculatedTaxes(gotFile, userId, Format.pdf);
                InputFile inputFile = new InputFile(file);

                SendDocument document = new SendDocument();
                document.setChatId(update.getMessage().getChatId().toString());
                document.setDocument(inputFile);
                document.setCaption(file.getName());
                try {
                    execute(document);
                } catch (TelegramApiException e) {
                    log.log(Level.SEVERE, "Error while sending document. Exception: ", e);
                }
            } else {
                String response = "Файл не распознан как отчет Interactive Brokers в формате htm";
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(response);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.log(Level.SEVERE, "Error while sending message. Exception: ", e);
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
            log.log(Level.SEVERE, "Error while getting file path. Exception: ", e);
        }
        return uploadedFilePath;
    }

    private File getFile(String uploadedFilePath) {
        File file = null;
        try {
            file = downloadFile(uploadedFilePath);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Error while downloading file. Exception: ", e);
        }
        return file;
    }
}
