package dto;

import java.util.Date;
import java.util.UUID;

public class InterestRaw {
    private final UUID id;
    private final String currency;
    private final Date date;
    private final String description;
    private final double amount;

    public InterestRaw(String currency,
                       Date date,
                       String description,
                       Double amount) {
        id = UUID.randomUUID();
        this.currency = currency;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}
