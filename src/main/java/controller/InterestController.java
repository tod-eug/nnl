package controller;

import currency.ExchangeRatesProvider;
import dto.*;

import java.util.*;

public class InterestController {

    public static Double taxRate = 0.13;

    public ArrayList<InterestCalculated> calculateInterest(ArrayList<InterestRaw> list, Map<String, Map<Date, ExchangeRate>> exchangeRates) {

        ArrayList<InterestCalculated> listOfTaxes = new ArrayList<>();

        if (list.size() > 0) {
            ExchangeRatesProvider exchangeRatesProvider = new ExchangeRatesProvider();

            //calculate
            for (InterestRaw i : list) {
                Double amount = i.getAmount();

                Date exchangeRateDate = exchangeRatesProvider.adjustExchangeRateDate(i.getDate(), exchangeRates.get(i.getCurrency()));
                Double value = exchangeRates.get(i.getCurrency()).get(exchangeRateDate).getValue();
                int nominal = exchangeRates.get(i.getCurrency()).get(exchangeRateDate).getNominal();

                Double amountRub = amount * value / nominal;
                Double taxRub = amountRub * taxRate;

                listOfTaxes.add(new InterestCalculated(i.getCurrency(), i.getDate(), i.getDescription(), amount,
                        amountRub, taxRub, value));
            }
        }
        return listOfTaxes;
    }
}
