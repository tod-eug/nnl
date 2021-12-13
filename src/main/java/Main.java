import bot.TaxBot;

import controller.FeesController;
import controller.FeesTransactionsController;
import controller.InterestController;
import dto.*;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import parser.FeesParser;
import parser.FeesTransactionsParser;
import parser.InterestParser;
import util.DataProvider;
import util.FilesUtils;
import util.PropertiesProvider;

import java.io.File;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {


//        File file = new File("U3434582.htm");
//        Document doc = DataProvider.getDocument(file);
//        FeesParser feesParser = new FeesParser();
//        ArrayList<FeesRaw> list = feesParser.parseFees(doc);
//        FeesController feesController = new FeesController();
//        ArrayList<FeesCalculated> list2 = feesController.calculateFees(list);
//        FeesTransactionsParser feesTransactionsParser = new FeesTransactionsParser();
//        ArrayList<FeesTransactionsRaw> raws = feesTransactionsParser.parseFeesTransactions(doc);
//        FeesTransactionsController feesTransactionsController = new FeesTransactionsController();
//        ArrayList<FeesTransactionsCalculated> feesTransactionsCalculateds = feesTransactionsController.calculateFeesTransactions(raws);
//        InterestParser interestParser = new InterestParser();
//        ArrayList<InterestRaw> ir = interestParser.parseInterest(doc);
//        InterestController controller = new InterestController();
//        ArrayList<InterestCalculated> interestCalculateds = controller.calculateInterest(ir);
//        System.out.println(list2.get(0).getCurrency());
//    }

        PropertiesProvider.setup();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TaxBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
