package it.satispay.signatureimpl.entity;

public class Body {
    private String flow;
    private Long amount_unit;
    private String currency;

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public void setAmount_unit(Long amount_unit) {
        this.amount_unit = amount_unit;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
