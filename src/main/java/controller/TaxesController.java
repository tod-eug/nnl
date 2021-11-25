package controller;

import bot.enums.Format;
import currency.ExchangeRatesProvider;
import db.DocumentsHelper;
import dto.*;
import org.jsoup.nodes.Document;
import output.pdf.TPdfWriter;
import output.xlsx.XlsWriter;
import parser.*;
import util.DataProvider;
import util.DateUtil;
import util.FilesUtils;

import java.io.File;
import java.util.*;

public class TaxesController {

    public File getCalculatedTaxes(File gotFile, String userId, Format format) {

        FilesUtils fileUtils = new FilesUtils();
        DocumentsHelper dHelper = new DocumentsHelper();

        //save raw file
        String uuidRawFile = UUID.randomUUID().toString();
        String rawFileName = uuidRawFile + ".htm";
        File rawFile = fileUtils.saveRawFile(gotFile, rawFileName);
        dHelper.createRawDocument(uuidRawFile, userId, rawFileName);

        //get document
        Document doc = DataProvider.getDocument(rawFile);

        //parse document
        DividendParser dividendParser = new DividendParser();
        TradesParser tradesParser = new TradesParser();
        InterestParser interestParser = new InterestParser();
        FeesParser feesParser = new FeesParser();
        FeesTransactionsParser feesTransactionsParser = new FeesTransactionsParser();

        ArrayList<DividendRaw> dividendList = dividendParser.parseDividends(doc);
        ArrayList<TradeRaw> tradesList = tradesParser.parseTrades(doc);
        ArrayList<InterestRaw> interestList = interestParser.parseInterest(doc);
        ArrayList<FeesRaw> feesList = feesParser.parseFees(doc);
        ArrayList<FeesTransactionsRaw> feesTransactionsList = feesTransactionsParser.parseFeesTransactions(doc);

        //request exchange rates for all currencies in dates range
        Map<String, Date> datesRange = getDatesRange(dividendList, tradesList, interestList, feesList, feesTransactionsList);
        List<String> currencies = getListOfCurrencies(dividendList, tradesList, interestList, feesList, feesTransactionsList);

        ExchangeRatesProvider exchangeRatesProvider = new ExchangeRatesProvider();
        Map<String, Map<Date, ExchangeRate>> exchangeRates = exchangeRatesProvider.getExchangeRates(currencies, datesRange);

        //calculate document
        DocumentCalculated documentCalculated = calculate(dividendList, tradesList, interestList, feesList, feesTransactionsList, exchangeRates);

        //write results in file and save file
        String uuidProcessedFile = UUID.randomUUID().toString();
        String processedFileName = ".pdf";
        TPdfWriter tPdfWriter = new TPdfWriter();
        File file = null;
        switch (format) {
            case pdf:
                processedFileName = uuidProcessedFile + ".pdf";
                file = tPdfWriter.writePdfFile(documentCalculated, processedFileName);
                break;
            case xlsx:
                processedFileName = uuidProcessedFile + ".xlsx";
                file = XlsWriter.writeXlsFile(documentCalculated, processedFileName);
                break;
        }
        dHelper.createProcessedDocument(uuidProcessedFile, userId, uuidRawFile, processedFileName);
        return file;
    }

    private DocumentCalculated calculate(ArrayList<DividendRaw> dividendList, ArrayList<TradeRaw> tradesList,
                                         ArrayList<InterestRaw> interestList, ArrayList<FeesRaw> feesList,
                                         ArrayList<FeesTransactionsRaw> feesTransactionsList,
                                         Map<String, Map<Date, ExchangeRate>> exchangeRates) {
        DividendController dividendController = new DividendController();
        TradesController tradesController = new TradesController();
        InterestController interestController = new InterestController();
        FeesController feesController = new FeesController();
        FeesTransactionsController feesTransactionsController = new FeesTransactionsController();

        ArrayList<DividendCalculated> dividends = dividendController.calculateDivs(dividendList, exchangeRates);
        Map<String, List<Trades>> trades =  tradesController.calculateTrades(tradesList, exchangeRates);
        ArrayList<InterestCalculated> interests = interestController.calculateInterest(interestList, exchangeRates);
        ArrayList<FeesCalculated> fees = feesController.calculateFees(feesList, exchangeRates);
        ArrayList<FeesTransactionsCalculated> feesTransactions = feesTransactionsController.calculateFeesTransactions(feesTransactionsList, exchangeRates);

        double dividendResult = 0.0;
        double tradesTaxResult = 0.0;
        double tradesDeductionResult = 0.0;
        double interestsTaxResult = 0.0;
        double feesResult = 0.0;
        double feesTransactionsResult = 0.0;
        double finalTaxResult = 0.0;
        double finalDeductionResult = 0.0;

        for (DividendCalculated d : dividends) {
            dividendResult = dividendResult + d.getResult();
        }

        Map<String, TradeResultsForInstrument> finalResultsByInstruments = new HashMap<>();
        Set<String> instruments = trades.keySet();
        for (String instrument : instruments) {
            double tax = 0.0;
            double deduction = 0.0;
            List<Trades> list = trades.get(instrument);
            for (Trades t : list) {
                tax = tax + t.getTaxRub();
                deduction = deduction + t.getDeductionRub();
                tradesTaxResult = tradesTaxResult + t.getTaxRub();
                tradesDeductionResult = tradesDeductionResult + t.getDeductionRub();
            }
            finalResultsByInstruments.put(instrument, new TradeResultsForInstrument(tax, deduction));
        }

        for (InterestCalculated i : interests) {
            interestsTaxResult = interestsTaxResult + i.getTaxRub();
        }

        for (FeesCalculated f : fees) {
            feesResult = feesResult + f.getAmountRub();
        }

        for (FeesTransactionsCalculated ft : feesTransactions) {
            feesTransactionsResult = feesTransactionsResult + ft.getAmountRub();
        }

        finalTaxResult = dividendResult + tradesTaxResult + interestsTaxResult;
        finalDeductionResult = tradesDeductionResult + feesResult + feesTransactionsResult;

        return new DocumentCalculated(dividends, dividendResult, trades, finalResultsByInstruments, tradesTaxResult, tradesDeductionResult,
                interests, interestsTaxResult, fees, feesResult, feesTransactions, feesTransactionsResult, finalTaxResult, finalDeductionResult);
    }

    /**
     * Method defines range of date from all lists. By default, "from" date decreased on 30 days
     * because in the new year holiday could be 8 -10 empty dates.
     *
     * @params lists - lists of transactions
     * Return map with two keys:
     * - "from" with the earliest date in the list
     * - "to" with the latest date in the list
     * @return - map with range of dates
     */
    private Map<String, Date> getDatesRange(ArrayList<DividendRaw> dividendList, ArrayList<TradeRaw> tradesList,
                                            ArrayList<InterestRaw> interestList, ArrayList<FeesRaw> feesList,
                                            ArrayList<FeesTransactionsRaw> feesTransactionsList) {
        DateUtil dateUtil = new DateUtil();
        Map<String, Date> result = new HashMap<>();
        Date from = dividendList.get(0).getDate();
        Date to = dividendList.get(0).getDate();
        for (DividendRaw d : dividendList) {
            if (d.getDate().before(from))
                from = d.getDate();
            if (d.getDate().after(to))
                to = d.getDate();
        }
        for (TradeRaw t : tradesList) {
            if (t.getDate().before(from))
                from = t.getDate();
            if (t.getDate().after(to))
                to = t.getDate();
        }
        for (InterestRaw i : interestList) {
            if (i.getDate().before(from))
                from = i.getDate();
            if (i.getDate().after(to))
                to = i.getDate();
        }
        for (FeesRaw f : feesList) {
            if (f.getDate().before(from))
                from = f.getDate();
            if (f.getDate().after(to))
                to = f.getDate();
        }
        for (FeesTransactionsRaw ft : feesTransactionsList) {
            if (ft.getDate().before(from))
                from = ft.getDate();
            if (ft.getDate().after(to))
                to = ft.getDate();
        }
        from = dateUtil.increaseDate(from, -30);
        result.put("from", from);
        result.put("to", to);
        return result;
    }

    /**
     * Method defines list of currencies from all lists
     *
     * @params lists - lists of transactions
     * @return list of currencies
     */
    private List<String> getListOfCurrencies(ArrayList<DividendRaw> dividendList, ArrayList<TradeRaw> tradesList,
                                             ArrayList<InterestRaw> interestList, ArrayList<FeesRaw> feesList,
                                             ArrayList<FeesTransactionsRaw> feesTransactionsList) {
        List<String> currencies = new ArrayList<>();
        for (DividendRaw d : dividendList) {
            if (currencies.contains(d.getCurrency()))
                continue;
            currencies.add(d.getCurrency());
        }
        for (TradeRaw t : tradesList) {
            if (currencies.contains(t.getCurrency()))
                continue;
            currencies.add(t.getCurrency());
        }
        for (InterestRaw i : interestList) {
            if (currencies.contains(i.getCurrency()))
                continue;
            currencies.add(i.getCurrency());
        }
        for (FeesRaw f : feesList) {
            if (currencies.contains(f.getCurrency()))
                continue;
            currencies.add(f.getCurrency());
        }
        for (FeesTransactionsRaw ft : feesTransactionsList) {
            if (currencies.contains(ft.getCurrency()))
                continue;
            currencies.add(ft.getCurrency());
        }
        return currencies;
    }
}