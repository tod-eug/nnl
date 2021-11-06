package dto;

import java.util.List;
import java.util.UUID;

public class Trades {

    private final UUID id;
    private final String instrument;
    private final String currency;
    private final String ticker;
    private final Double finalPLRub;
    private final Double taxRub;
    private final Double deductionRub;
    private final List<TradeCalculated> purchases;
    private final List<TradeCalculated> sells;

    public Trades(String instrument,
                  String currency,
                  String ticker,
                  Double finalPLRub,
                  Double taxRub,
                  Double deductionRub,
                  List<TradeCalculated> purchases,
                  List<TradeCalculated> sells) {
        id = UUID.randomUUID();
        this.instrument = instrument;
        this.currency = currency;
        this.ticker = ticker;
        this.finalPLRub = finalPLRub;
        this.taxRub = taxRub;
        this.deductionRub = deductionRub;
        this.purchases = purchases;
        this.sells = sells;
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

    public Double getFinalPLRub() {
        return finalPLRub;
    }

    public Double taxRub() {
        return taxRub;
    }

    public List<TradeCalculated> getPurchases() {
        return purchases;
    }

    public List<TradeCalculated> getSells() {
        return sells;
    }

    public Double getDeductionRub() {
        return deductionRub;
    }
}
