package controller;

import dto.*;

import java.util.*;
import java.util.stream.Collectors;

import currency.ExchangeRatesProvider;
import util.DateUtil;

public class TradesController {

    /**
     * https://vc.ru/finance/111767-slozhnosti-s-raschetom-nalogooblagaemoy-bazy-pri-rabote-s-interactive-brokers
     * https://rusfinrevolution.ru/instrukcziya-kak-ya-sdaval-deklaracziyu-ndfl-interactive-brokers-za-2020-god/
     * https://longterminvestments.ru/3-ndfl-guide/
     *
     * Сложность № 1. Пересчёт всех операций в рубли по курсу на дату сделки. Нужно взять все операции за год,
     * по каждой сделке узнать курс валюты того дня и пересчитать все операции в рубли. Если вы активно торговали,
     * это дичайший онанизм. В плохом смысле и совсем без удовольствия.
     *
     * Сложность № 2. Пересчёт всех комиссий в рубли по курсу на дату сделки. Веселье продолжается.
     * Пересчитываем все комиссии брокера за каждую покупку и продажу ценных бумаг.
     *
     * Сложность № 3. Учёт доходов и убытков ведется раздельно по фондовому, валютному и срочному рынкам.
     * Учёт операций по сделкам, дивидендам и начислению процентов также ведётся раздельно.
     *
     * Сложность № 4. Подобрать коды доходов и расходов по всем операциям. Например, 1530 для дохода по ценным бумагам,
     * 201 для расхода по сделкам, 1010 для дивидендов и 1011 для процентов.
     * При заполнении декларации также понадобится указать данные коды.
     *
     * Сложность № 5. Рассчитать прибыль или убыток по сделкам. Определить, сколько налогов было уплачено по дивидендам
     * и сколько осталось доплатить в российской налоговой.
     *
     * -> Фильтруем все строки с кодом C или O
     */

    public static Double taxRate = 0.13;

    public Map<String, List<Trades>> calculateTrades(List<TradeRaw> list, Map<String, Map<Date, ExchangeRate>> exchangeRates) {

        Map<String, List<Trades>> result = new HashMap<>();

        if (list.size() > 0) {

            //filter only code = C or code = O
            list = filterOnlyTrades(list);

            //get set of instruments
            Set<String> instruments = new HashSet<>();
            for (TradeRaw t : list) {
                instruments.add(t.getInstrument());
            }
            for (String instrument : instruments) {
                List<Trades> trades = new ArrayList<>();

                //filter instruments
                List<TradeRaw> instrumentsFiltered = filterTradesByInstruments(list, instrument);

                //get set of tickers
                Set<String> tickers = new HashSet<>();
                for (TradeRaw t : instrumentsFiltered) {
                    tickers.add(t.getTicker());
                }

                //calculate each raw trade one by one for one ticker
                for (String ticker : tickers) {
                    List<TradeCalculated> purchases = new ArrayList<>();
                    List<TradeCalculated> sales = new ArrayList<>();
                    double finalPLRub = 0.0;
                    double finalResult = 0.0;
                    double finalTaxRub = 0.0;
                    double finalDeductionRub = 0.0;
                    //calculate sales
                    List<TradeRaw> s = filterTypeTrades(instrumentsFiltered, instrument, ticker, "C");
                    if (s.size() > 0) {
                        for (TradeRaw t : s) {
                            TradeCalculated calculated = calculate(exchangeRates, t, true);
                            finalPLRub = finalPLRub + calculated.getRealizedPLRub();
                            finalResult = finalResult + calculated.getResult();
                            sales.add(calculated);
                        }
                    }

                    //calculate purchases
                    List<TradeRaw> p = filterTypeTrades(instrumentsFiltered, instrument, ticker, "O");
                    if (p.size() > 0) {
                        for (TradeRaw t : p) {
                            purchases.add(calculate(exchangeRates, t, false));
                        }
                    }

                    //get currency
                    String currency = "";
                    if (purchases.size() > 0)
                        currency = purchases.get(0).getCurrency();
                    else
                        currency = sales.get(0).getCurrency();
                    if (finalResult > 0)
                        finalTaxRub = finalResult;
                    else
                        finalDeductionRub = finalPLRub;
                    trades.add(new Trades(instrument, currency, ticker, finalPLRub, finalTaxRub, finalDeductionRub, purchases, sales));
                }
                result.put(instrument, trades);
            }
        }
        return result;
    }

    /**
     * Method calculates separate trade into TradeCalculated object
     * @param exchangeRates - exchange rates for all currencies
     * @param t - trade itself
     * @param sales - if trade is sales then we need to calculate tax
     * @return - TradeCalculated object
     */
    private TradeCalculated calculate(Map<String, Map<Date, ExchangeRate>> exchangeRates, TradeRaw t, Boolean sales) {
        DateUtil dateUtil = new DateUtil();
        ExchangeRatesProvider exchangeRatesProvider = new ExchangeRatesProvider();
        Double sum = t.getSum();
        Double commission = t.getCommission();
        Double realizedPL = t.getRealizedPL();
        Double basis = t.getBasis();
        Date justDate = dateUtil.removeTimeFromDate(t.getDate());
        Date exchangeRateDate = exchangeRatesProvider.adjustExchangeRateDate(justDate, exchangeRates.get(t.getCurrency()));
        Double value = exchangeRates.get(t.getCurrency()).get(exchangeRateDate).getValue();
        int nominal = exchangeRates.get(t.getCurrency()).get(exchangeRateDate).getNominal();

        Double sumRub = sum * value / nominal;
        Double commissionRub = commission * value / nominal;
        Double realizedPLRub = realizedPL * value / nominal;
        Double basisRub = basis * value / nominal;
        double result = 0.0;
        if (sales)
            result = realizedPLRub * taxRate;

        return new TradeCalculated(t.getInstrument(), t.getCurrency(), value, t.getTicker(), t.getDate(), t.getQuantity(),
                t.getTradePrice(), sum, sumRub, commission, commissionRub, basis, basisRub, realizedPL, realizedPLRub, result);
    }

    /**
     * Filter only trades which contains "C" or "O" in the code
     * @param list - list with trades raw
     * @return - filtered list
     */
    private List<TradeRaw> filterOnlyTrades(List<TradeRaw> list) {
        return list.stream()
                .filter(tradeRaw -> tradeRaw.getCode().contains("C") || tradeRaw.getCode().contains("O"))
                .collect(Collectors.toList());
    }

    /**
     * Filter trades by necessary instrument
     * @param list - list with trades raw
     * @param instrument - necessary instrument
     * @return - filtered list
     */
    private List<TradeRaw> filterTradesByInstruments(List<TradeRaw> list, String instrument) {
        return list.stream()
                .filter(tradeRaw -> tradeRaw.getInstrument().equals(instrument))
                .collect(Collectors.toList());
    }

    /**
     * Filter trades from the list for necessary Instrument, necessary Ticker and necessary type of trade.
     * @param list - list with trades raw
     * @param instrument - necessary Instrument
     * @param ticker - necessary Ticker
     * @param code - necessary type of trade "C" or "O"
     * @return - filtered list
     */
    private List<TradeRaw> filterTypeTrades(List<TradeRaw> list, String instrument, String ticker, String code) {
        return list.stream()
                .filter(tradeRaw -> tradeRaw.getInstrument().equals(instrument))
                .filter(tradeRaw -> tradeRaw.getTicker().equals(ticker))
                .filter(tradeRaw -> tradeRaw.getCode().contains(code))
                .collect(Collectors.toList());
    }
}
