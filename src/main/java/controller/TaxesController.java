package controller;

import dto.DividendCalculated;
import dto.DividendRaw;
import dto.TradeRaw;
import dto.Trades;
import org.jsoup.nodes.Document;
import output.xlsx.XlsWriter;
import parser.DividendParser;
import parser.TradesParser;
import util.DataProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaxesController {

    public File getCalculatedTaxes(File gotFile) {

        //get document
        Document doc = DataProvider.getDocument(gotFile);

        //get list of trades
        TradesParser tradesParser = new TradesParser();
        ArrayList<TradeRaw> tradesList = tradesParser.parseTrades(doc);

        //calculate trades
        TradesController tradesController = new TradesController();
        Map<String, List<Trades>> trades =  tradesController.calculateTrades(tradesList);

        //get list of dividends
        DividendParser dividendParser = new DividendParser();
        ArrayList<DividendRaw> dividendList = dividendParser.parseDividends(doc);

        //calculate dividends
        DividendController dividendController = new DividendController();
        ArrayList<DividendCalculated> list = dividendController.calculateDivs(dividendList);

        //write results in file
        File dir = new File("processed");
        boolean isCreated = dir.mkdirs();
        String fileName = "processed/" + UUID.randomUUID() + ".xlsx";

        XlsWriter.writeXlsFile(list, trades, fileName);
        File file = new File(fileName);
        return file;
    }
}