package dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FeesTransactionsRaw {
    private final UUID id;
    private final String instrument;
    private final String currency;
    private final Date date;
    private final String ticker;
    private final String description;
    private final double quantity;
    private final double tradePrice;
    private final double amount;
    private final ArrayList<String> code;

    public FeesTransactionsRaw(String instrument,
                               String currency,
                               Date date,
                               String ticker,
                               String description,
                               Double quantity,
                               Double tradePrice,
                               Double amount,
                               ArrayList<String> code) {
        id = UUID.randomUUID();
        this.instrument = instrument;
        this.currency = currency;
        this.date = date;
        this.ticker = ticker;
        this.description = description;
        this.quantity = quantity;
        this.tradePrice = tradePrice;
        this.amount = amount;
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

    public Date getDate() {
        return date;
    }

    public String getTicker() {
        return ticker;
    }

    public String getDescription() {
        return description;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public double getAmount() {
        return amount;
    }

    public ArrayList<String> getCode() {
        return code;
    }
}
