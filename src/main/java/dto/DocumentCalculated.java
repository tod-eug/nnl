package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentCalculated {
    private final ArrayList<DividendCalculated> dividends;
    private final double dividendResult;
    private final Map<String, List<Trades>> trades;
    private final Map<String, TradeResultsForInstrument> finalResultsByInstruments;
    private final double tradesTaxResult;
    private final double tradesDeductionResult;
    private final ArrayList<InterestCalculated> interests;
    private final double interestsTaxResult;
    private final ArrayList<FeesCalculated> fees;
    private final double feesResult;
    private final ArrayList<FeesTransactionsCalculated> feesTransactions;
    private final double feesTransactionsResult;
    private final double finalTaxResult;
    private final double finalDeductionResult;

    public DocumentCalculated(ArrayList<DividendCalculated> dividends,
                              double dividendResult,
                              Map<String, List<Trades>> trades,
                              Map<String, TradeResultsForInstrument> finalResultsByInstruments,
                              double tradesTaxResult,
                              double tradesDeductionResult,
                              ArrayList<InterestCalculated> interests,
                              double interestsTaxResult,
                              ArrayList<FeesCalculated> fees,
                              double feesResult,
                              ArrayList<FeesTransactionsCalculated> feesTransactions,
                              double feesTransactionsResult,
                              double finalTaxResult,
                              double finalDeductionResult) {
        this.dividends = dividends;
        this.dividendResult = dividendResult;
        this.trades = trades;
        this.finalResultsByInstruments = finalResultsByInstruments;
        this.tradesTaxResult = tradesTaxResult;
        this.tradesDeductionResult = tradesDeductionResult;
        this.interests = interests;
        this.interestsTaxResult = interestsTaxResult;
        this.fees = fees;
        this.feesResult = feesResult;
        this.feesTransactions = feesTransactions;
        this.feesTransactionsResult = feesTransactionsResult;
        this.finalTaxResult = finalTaxResult;
        this.finalDeductionResult = finalDeductionResult;
    }

    public ArrayList<DividendCalculated> getDividends() {
        return dividends;
    }

    public double getDividendResult() {
        return dividendResult;
    }

    public Map<String, List<Trades>> getTrades() {
        return trades;
    }

    public Map<String, TradeResultsForInstrument> getFinalResultsByInstruments() {
        return finalResultsByInstruments;
    }

    public double getTradesTaxResult() {
        return tradesTaxResult;
    }

    public double getTradesDeductionResult() {
        return tradesDeductionResult;
    }

    public ArrayList<InterestCalculated> getInterests() {
        return interests;
    }

    public double getInterestsTaxResult() {
        return interestsTaxResult;
    }

    public ArrayList<FeesCalculated> getFees() {
        return fees;
    }

    public double getFeesResult() {
        return feesResult;
    }

    public ArrayList<FeesTransactionsCalculated> getFeesTransactions() {
        return feesTransactions;
    }

    public double getFeesTransactionsResult() {
        return feesTransactionsResult;
    }

    public double getFinalTaxResult() {
        return finalTaxResult;
    }

    public double getFinalDeductionResult() {
        return finalDeductionResult;
    }
}
