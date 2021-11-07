package dto;

import java.util.Date;
import java.util.UUID;

public class TradeCalculated {

    private final UUID id;
    private final String instrument;
    private final String currency;
    private final Double exchangeRate;
    private final String ticker;
    private final Date date;
    private final Double quantity;
    private final Double tradePrice;
    private final Double sum;
    private final Double sumRub;
    private final Double commission;
    private final Double commissionRub;
    private final Double basis;
    private final Double basisRub;
    private final Double realizedPL;
    private final Double realizedPLRub;
    private final Double result;

    public TradeCalculated(String instrument,
                           String currency,
                           Double exchangeRate,
                           String ticker,
                           Date date,
                           Double quantity,
                           Double tradePrice,
                           Double sum,
                           Double sumRub,
                           Double commission,
                           Double commissionRub,
                           Double basis,
                           Double basisRub,
                           Double realizedPL,
                           Double realizedPLRub,
                           Double result) {
        id = UUID.randomUUID();
        this.instrument = instrument;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.ticker = ticker;
        this.date = date;
        this.quantity = quantity;
        this.tradePrice = tradePrice;
        this.sum = sum;
        this.sumRub = sumRub;
        this.commission = commission;
        this.commissionRub = commissionRub;
        this.basis = basis;
        this.basisRub = basisRub;
        this.realizedPL = realizedPL;
        this.realizedPLRub = realizedPLRub;
        this.result = result;
    }

    public UUID getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTicker() {
        return ticker;
    }

    public Date getDate() {
        return date;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public Double getSum() {
        return sum;
    }

    public Double getSumRub() {
        return sumRub;
    }

    public Double getCommission() {
        return commission;
    }

    public Double getCommissionRub() {
        return commissionRub;
    }

    public Double getRealizedPL() {
        return realizedPL;
    }

    public Double getRealizedPLRub() {
        return realizedPLRub;
    }

    public Double getResult() {
        return result;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public Double getBasis() {
        return basis;
    }

    public Double getBasisRub() {
        return basisRub;
    }
}
