package it.satispay.signatureimpl.entity;

public class Body {
    private String flow;
    private Long amount_unit;
    private String currency;

    public Body(String flow, Long amount_unit, String currency) {
        this.flow = flow;
        this.amount_unit = amount_unit;
        this.currency = currency;
    }
}
