package it.satispay.signatureimpl.pojo;

public class Body {
    private String flow;
    private Long amount_unit;
    private String currency;

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Long getAmount_unit() {
        return amount_unit;
    }

    public void setAmount_unit(Long amount_unit) {
        this.amount_unit = amount_unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
