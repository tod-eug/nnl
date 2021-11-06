package controller;

import currency.ExchangeRatesProvider;
import dto.DividendCalculated;
import dto.DividendRaw;
import dto.ExchangeRate;
import util.DateUtil;

import java.util.*;
import java.util.stream.Collectors;

public class DividendController {

    /**
     * Например, в первом квартале 2021 года американская компания ExxonMobil решила выплатить 0,87 $ на акцию в виде дивидендов.
     * Выплата произошла 10 марта, а российские инвесторы получили деньги, допустим, 19 марта.
     *
     * Предположим, у налогового резидента России есть 100 акций ExxonMobil. Сумма дивидендов в таком случае — 87 $ без учета налогов.
     *
     * Если инвестор подписал форму W-8BEN, он получит дивиденды за вычетом налога по ставке 10%.
     * В нашем примере инвестору придет не 87 $, а 78,3 $. Удержанный в США налог составил 8,7 $.
     *
     * Еще 3% надо будет самостоятельно уплатить в России, чтобы в итоге налог с дивидендов составил 13%.
     *
     * Вот как считать размер доплаты:
     * - Пересчитать полученную сумму в рубли по курсу ЦБ на дату получения. Человек получил 78,3 $ на брокерский счет в России 19 числа.
     * Доллар согласно курсу ЦБ тогда стоил 73,6582 Р, значит, доход инвестора — 5767,437 Р.
     * - Посчитать 13% от этой суммы. Это 750 Р.
     * - Посчитать, сколько налога удержали в США, переведя его в рубли по курсу ЦБ на дату выплаты.
     * 19 марта в США в виде налога удержали 8,7 $, а курс доллара на эту дату был 73,6582 Р. Значит, в США удержали 641 Р.
     * - Вот сколько осталось уплатить в России: 750 Р − 641 Р = 109 Р.
     *
     * -> Фильтруем все строки с кодом Re где paymentDate = date
     */

    public static Double taxRate = 0.13;

    public static ArrayList<DividendCalculated> calculateDivs(ArrayList<DividendRaw> list) {

        ArrayList<Double> taxes = new ArrayList<>();
        ArrayList<DividendCalculated> listOfTaxes = new ArrayList<>();

        //get range of dates from list
        Map<String, Date> datesRange = getDatesRange(list);

        //get list of currencies
        List<String> currencies = getListOfCurrencies(list);

        Map<String, Map<Date, ExchangeRate>> exchangeRates = ExchangeRatesProvider.getExchangeRates(currencies, datesRange);

        //filter only real received dividends
        List<DividendRaw> filteredList = filterDividendList(list);

        //calculate
        for (DividendRaw d : filteredList) {
            Double grossDividend = d.getDividendGross();
            Double payedTax = d.getTax();
            Date exchangeRateDate = ExchangeRatesProvider.adjustExchangeRateDate(d.getDate(), exchangeRates.get(d.getCurrency()));
            Double value = exchangeRates.get(d.getCurrency()).get(exchangeRateDate).getValue();
            int nominal = exchangeRates.get(d.getCurrency()).get(exchangeRateDate).getNominal();

            Double dividendRub = grossDividend * value / nominal;
            Double expectedDividendRub = dividendRub * taxRate;
            Double payedTaxRub = payedTax * value / nominal;
            Double result = expectedDividendRub - payedTaxRub;

            if (result > 0) {
                taxes.add(result);
                listOfTaxes.add(new DividendCalculated(d.getTicker(), d.getPaymentDate(),
                        grossDividend, d.getDividendNet(), payedTax,
                        dividendRub, expectedDividendRub, payedTaxRub, result));
            }
        }
        return listOfTaxes;
    }


    private static List<DividendRaw> filterDividendList(List<DividendRaw> list) {
        return list.stream()
                .filter(dividendRaw -> dividendRaw.getCode().equals("Re"))
                .filter(dividendRaw -> dividendRaw.getDate().equals(dividendRaw.getPaymentDate()))
                .collect(Collectors.toList());
    }

    /**
     * Method defines range of date from the list of transactions. By default, "from" date decreased on 30 days
     * because in the new year holiday could be 8 -10 empty dates.
     *
     * @param list - list of transactions
     * Return map with two keys:
     * - "from" with the earliest date in the list
     * - "to" with the latest date in the list
     * @return - map with range of dates
     */
    private static Map<String, Date> getDatesRange(List<DividendRaw> list) {
        Map<String, Date> result = new HashMap<>();
        Date from = list.get(0).getDate();
        Date to = list.get(0).getDate();
        for (DividendRaw d : list) {
            if (d.getDate().before(from))
                from = d.getDate();
            if (d.getDate().after(to))
                to = d.getDate();
        }
        from = DateUtil.increaseDate(from, -30);
        result.put("from", from);
        result.put("to", to);
        return result;
    }

    /**
     * Method defines list of currencies from the list of transactions
     *
     * @param list - list of transactions
     * @return list of currencies
     */
    private static List<String> getListOfCurrencies(List<DividendRaw> list) {
        List<String> currencies = new ArrayList<>();
        for (DividendRaw d : list) {
            if (currencies.contains(d.getCurrency()))
                continue;
            currencies.add(d.getCurrency());
        }
        return currencies;
    }
}
