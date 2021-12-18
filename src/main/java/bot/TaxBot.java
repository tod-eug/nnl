package bot;

import bot.commands.*;
import bot.enums.Format;
import bot.enums.State;
import controller.TaxesController;
import db.CheckoutsHelper;
import db.SubscriptionsHelper;
import db.FormatHelper;
import db.UsersHelper;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxBot extends TelegramLongPollingCommandBot {

    private static Logger log = Logger.getLogger(TaxBot.class.getName());
    public static Map<Long, State> stateMap = new HashMap<>();

    public TaxBot() {
        super();
        register(new StartCommand());
        register(new CalculateCommand());
        register(new FormatCommand());
        register(new CancelCommand());
        register(new SubscriptionCommand());
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
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        FormatHelper fh = new FormatHelper();

        if (update.hasCallbackQuery()) {
            processCallbackQuery(update, fh);
        }

        if (update.hasPreCheckoutQuery()) {
            processPreCheckoutQuery(update);
        }

        if (update.hasMessage() && update.getMessage().hasSuccessfulPayment()) {
            processSuccessfulPayment(update);
        }

        State state = State.FREE;
        if (stateMap.containsKey(update.getMessage().getChatId()))
            state = stateMap.get(update.getMessage().getChatId());

        if (update.hasMessage() && update.getMessage().hasText()) {
            processEmptyMessage(update, state);
        }

        if (update.getMessage().hasDocument()) {
            if (state.equals(State.CALCULATE)) {
                processDocument(update, fh);
            } else {
                sendMsg(update.getMessage().getChatId(), Constants.USE_CALCULATE_COMMAND);
            }
        }
    }

    private void processPreCheckoutQuery(Update update) {
        sendAnswerPreCheckoutQuery(update.getPreCheckoutQuery().getId(), true);
        CheckoutsHelper checkoutsHelper = new CheckoutsHelper();
        checkoutsHelper.savePreCheckout(update.getPreCheckoutQuery());
        log.info("PreCheckout Query processed");
    }

    private void processSuccessfulPayment(Update update) {
        SubscriptionsHelper subscriptionsHelper = new SubscriptionsHelper();
        subscriptionsHelper.setSubscriptionEndDate(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
        CheckoutsHelper checkoutsHelper = new CheckoutsHelper();
        checkoutsHelper.updateCheckoutWithPayment(update.getMessage().getSuccessfulPayment());
        log.info("Successful Payment processed for user: " + update.getMessage().getFrom().getId());
    }

    private void processCallbackQuery(Update update, FormatHelper fh) {
        switch (update.getCallbackQuery().getData().toLowerCase(Locale.ROOT)) {
            case "pdf":
                fh.setFormat(update.getCallbackQuery().getMessage().getChatId(), Format.pdf);
                sendAnswerCallbackQuery(update.getCallbackQuery().getId(), true);
                deleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
                break;
            case "xlsx":
                fh.setFormat(update.getCallbackQuery().getMessage().getChatId(), Format.xlsx);
                sendAnswerCallbackQuery(update.getCallbackQuery().getId(), true);
                deleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
        }
        log.info("File format defined for user: " + update.getCallbackQuery().getMessage().getChatId());
    }

    private void processDocument(Update update, FormatHelper fh) {
        Format format = fh.getFormat(update.getMessage().getChatId(), Format.pdf);
        UsersHelper uh = new UsersHelper();
        String userId = uh.findUserByTgId(update.getMessage().getFrom().getId().toString(), update.getMessage().getFrom());

        String uploadedFilePath = getFilePath(update);
        File gotFile = getFile(uploadedFilePath);

        IBReportValidator ibReportValidator = new IBReportValidator();
        if (ibReportValidator.isFileIBReport(gotFile)) {
            //send message "Working on it"
            sendMsg(update.getMessage().getChatId(), Constants.PROCESSING);

            //calculations
            TaxesController taxesController = new TaxesController();
            File file = taxesController.getCalculatedTaxes(gotFile, userId, format);
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
            stateMap.put(update.getMessage().getChatId(), State.FREE);
            log.info("File processed for user: " + update.getMessage().getFrom().getId());
        } else {
            sendMsg(update.getMessage().getChatId(), Constants.WRONG_FILE_FORMAT);
            log.info("Wrong file was sent by the user: " + update.getMessage().getFrom().getId());
        }
    }

    private void processEmptyMessage(Update update, State state) {
        if (state.equals(State.CALCULATE)) {
            sendMsg(update.getMessage().getChatId(), Constants.WAIT_INCOME_FILE);
        } else {
            sendMsg(update.getMessage().getChatId(), Constants.USE_CALCULATE_COMMAND);
        }
    }

    private void sendMsg(long chatId, String text) {
        SendMessage sm = new SendMessage();
        sm.setChatId(Long.toString(chatId));
        sm.setText(text);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Error while sending message. Exception: ", e);
        }
    }

    private void sendAnswerCallbackQuery(String id, Boolean success) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(id);
        answerCallbackQuery.setShowAlert(success);
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Error while sending AnswerCallbackQuery. Exception: ", e);
        }
    }

    private void sendAnswerPreCheckoutQuery(String id, Boolean success) {
        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
        answerPreCheckoutQuery.setOk(success);
        answerPreCheckoutQuery.setPreCheckoutQueryId(id);
        try {
            execute(answerPreCheckoutQuery);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Error while sending PreCheckoutQuery. Exception: ", e);
        }
    }

    private void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(Long.toString(chatId));
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Error while deleting message. Exception: ", e);
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
