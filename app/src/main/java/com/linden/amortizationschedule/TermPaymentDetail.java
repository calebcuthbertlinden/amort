package com.linden.amortizationschedule;

import java.math.BigDecimal;

public class TermPaymentDetail {

    private BigDecimal balance;
    private BigDecimal capitalBalance;
    private BigDecimal interestPayment;
    private BigDecimal principlePayment;
    private BigDecimal payment;
    private int period;

    TermPaymentDetail(BigDecimal balance, BigDecimal capitalBalance, BigDecimal interestPayment,
                      BigDecimal principlePayment, BigDecimal payment) {
        this.balance = balance;
        this.capitalBalance = capitalBalance;
        this.interestPayment = interestPayment;
        this.principlePayment = principlePayment;
        this.payment = payment;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCapitalBalance() {
        return capitalBalance;
    }

    public void setCapitalBalance(BigDecimal interestBalance) {
        this.capitalBalance = interestBalance;
    }

    public BigDecimal getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimal getPrinciplePayment() {
        return principlePayment;
    }

    public void setPrinciplePayment(BigDecimal principlePayment) {
        this.principlePayment = principlePayment;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }
}
