package bot;

import bot.commands.CalculateCommand;
import bot.commands.CancelCommand;
import bot.commands.FormatCommand;
import bot.commands.StartCommand;
import bot.enums.Format;
import bot.enums.State;
import controller.TaxesController;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import util.IBReportValidator;
import util.PropertiesProvider;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TaxBot extends TelegramLongPollingCommandBot {

    public static Map<Long, State> stateMap = new HashMap<>();
    public static Map<Long, Format> formatMap = new HashMap<>();

    public static String getFormat(long chatId) {
        Format format = Format.pdf;
        if (formatMap.containsKey(chatId))
            format = formatMap.get(chatId);
        return format.toString();
    }

    public TaxBot() {
        super();
        register(new StartCommand());
        register(new CalculateCommand());
        register(new FormatCommand());
        register(new CancelCommand());
    }

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
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData().toLowerCase(Locale.ROOT)) {
                case "pdf":
                    formatMap.put(update.getCallbackQuery().getMessage().getChatId(), Format.pdf);
                    sendAnswerCallbackQuery(update.getCallbackQuery().getId(), true);
                    break;
                case "xlsx":
                    formatMap.put(update.getCallbackQuery().getMessage().getChatId(), Format.xlsx);
                    sendAnswerCallbackQuery(update.getCallbackQuery().getId(), true);
            }
        }
        Format format = Format.pdf;
        if (formatMap.containsKey(update.getMessage().getChatId()))
            format = formatMap.get(update.getMessage().getChatId());

        State state = State.FREE;
        if (stateMap.containsKey(update.getMessage().getChatId()))
            state = stateMap.get(update.getMessage().getChatId());

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (state.equals(State.CALCULATE)) {
                sendMsg(update.getMessage().getChatId(), Constants.WAIT_INCOME_FILE);
            } else {
                sendMsg(update.getMessage().getChatId(), Constants.USE_CALCULATE_COMMAND);
            }
        }
        if (update.getMessage().hasDocument()) {
            if (state.equals(State.CALCULATE)) {
                String uploadedFilePath = getFilePath(update);
                File gotFile = getFile(uploadedFilePath);

                IBReportValidator ibReportValidator = new IBReportValidator();
                if (ibReportValidator.isFileIBReport(gotFile)) {
                    //send message "Working on it"
                    sendMsg(update.getMessage().getChatId(), Constants.PROCESSING);

                    //calculations
                    TaxesController taxesController = new TaxesController();
                    File file = taxesController.getCalculatedTaxes(gotFile, format);
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
                    stateMap.put(update.getMessage().getChatId(), State.FREE);
                } else {
                    sendMsg(update.getMessage().getChatId(), Constants.WRONG_FILE_FORMAT);
                }
            } else {
                sendMsg(update.getMessage().getChatId(), Constants.USE_CALCULATE_COMMAND);
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private void sendMsg(long chatId, String text) {
        SendMessage sm = new SendMessage();
        sm.setChatId(Long.toString(chatId));
        sm.setText(text);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswerCallbackQuery(String id, Boolean success) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(id);
        answerCallbackQuery.setShowAlert(success);
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
