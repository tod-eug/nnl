package controller;

import db.DocumentsHelper;
import dto.DividendCalculated;
import dto.DividendRaw;
import dto.TradeRaw;
import dto.Trades;
import org.jsoup.nodes.Document;
import output.xlsx.XlsWriter;
import parser.DividendParser;
import parser.TradesParser;
import util.DataProvider;
import util.FilesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaxesController {

    public File getCalculatedTaxes(String userId, File gotFile) {

        FilesUtils fileUtils = new FilesUtils();
        DocumentsHelper dHelper = new DocumentsHelper();

        //save raw file
        String uuidRawFile = UUID.randomUUID().toString();
        String rawFileName = uuidRawFile + ".htm";
        File rawFile = fileUtils.saveRawFile(gotFile, rawFileName);
        dHelper.createRawDocument(uuidRawFile, userId, rawFileName);

        //get document
        Document doc = DataProvider.getDocument(rawFile);

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

        //write results in file and save file
        String uuidProcessedFile = UUID.randomUUID().toString();
        String processedFileName = uuidProcessedFile + ".xlsx";
        File file = XlsWriter.writeXlsFile(list, trades, processedFileName);
        dHelper.createProcessedDocument(uuidProcessedFile, userId, uuidRawFile, processedFileName);
        return file;
    }
}