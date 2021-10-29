package dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class TradeRaw {

    private final UUID id;
    private final String instrument;
    private final String currency;
    private final String ticker;
    private final Date date;
    private final Double quantity;
    private final Double tradePrice;
    private final Double closePrice;
    private final Double sum;
    private final Double commission;
    private final Double basis;
    private final Double realizedPL;
    private final Double mtmPL;
    private final ArrayList<String> code;

    public TradeRaw(String instrument,
                    String currency,
                    String ticker,
                    Date date,
                    Double quantity,
                    Double tradePrice,
                    Double closePrice,
                    Double sum,
                    Double commission,
                    Double basis,
                    Double realizedPL,
                    Double mtmPL,
                    ArrayList<String> code) {
        id = UUID.randomUUID();
        this.instrument = instrument;
        this.currency = currency;
        this.ticker = ticker;
        this.date = date;
        this.quantity = quantity;
        this.tradePrice = tradePrice;
        this.closePrice = closePrice;
        this.sum = sum;
        this.commission = commission;
        this.basis = basis;
        this.realizedPL = realizedPL;
        this.mtmPL = mtmPL;
        this.code = code;
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

    public Double getClosePrice() {
        return closePrice;
    }

    public Double getSum() {
        return sum;
    }

    public Double getCommission() {
        return commission;
    }

    public Double getBasis() {
        return basis;
    }

    public Double getRealizedPL() {
        return realizedPL;
    }

    public Double getMtmPL() {
        return mtmPL;
    }

    public ArrayList<String> getCode() {
        return code;
    }
}
