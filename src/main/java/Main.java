import controller.DividendController;
import controller.TradesController;
import dto.DividendCalculated;
import dto.DividendRaw;
import dto.TradeRaw;
import dto.Trades;
import org.jsoup.nodes.Document;
import output.xlsx.XlsWriter;
import parser.DividendParser;
import parser.TradesParser;
import util.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {

        //get document
        Document doc = DataProvider.getDocument();

        //get list of trades
        TradesParser tradesParser = new TradesParser();
        ArrayList<TradeRaw> tradesList = tradesParser.parseTrades(doc);

        //calculate trades
        TradesController tradesController = new TradesController();
        Map<String, List<Trades>> trades =  tradesController.calculateTrades(tradesList);
        trades.size();

        //get list of dividends
        ArrayList<DividendRaw> dividendList = DividendParser.parseDividends(doc);

        //calculate dividends
        ArrayList<DividendCalculated> list = DividendController.calculateDivs(dividendList);

        //write results in file
        XlsWriter.writeXlsFile(list, trades);
    }
}
