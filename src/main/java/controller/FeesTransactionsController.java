package controller;

import currency.ExchangeRatesProvider;
import dto.*;
import util.DateUtil;

import java.util.*;

public class FeesTransactionsController {

    public ArrayList<FeesTransactionsCalculated> calculateFeesTransactions(ArrayList<FeesTransactionsRaw> list, Map<String, Map<Date, ExchangeRate>> exchangeRates) {

        ArrayList<FeesTransactionsCalculated> listOfTaxes = new ArrayList<>();

        if (list.size() > 0) {
            ExchangeRatesProvider exchangeRatesProvider = new ExchangeRatesProvider();

            //calculate
            for (FeesTransactionsRaw f : list) {
                DateUtil dateUtil = new DateUtil();
                Date justDate = dateUtil.removeTimeFromDate(f.getDate());
                Double amount = f.getAmount();

                Date exchangeRateDate = exchangeRatesProvider.adjustExchangeRateDate(justDate, exchangeRates.get(f.getCurrency()));
                Double value = exchangeRates.get(f.getCurrency()).get(exchangeRateDate).getValue();
                int nominal = exchangeRates.get(f.getCurrency()).get(exchangeRateDate).getNominal();

                Double amountRub = amount * value / nominal;

                listOfTaxes.add(new FeesTransactionsCalculated(f.getInstrument(),f.getCurrency(), f.getTicker(),
                        f.getDate(), f.getDescription(), f.getQuantity(), f.getTradePrice(), amount,
                        amountRub, value));
            }
        }
        return listOfTaxes;
    }
}
