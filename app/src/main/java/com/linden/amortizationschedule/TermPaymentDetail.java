package com.linden.amortizationschedule;

import java.math.BigDecimal;

public class TermPaymentDetail {

    private BigDecimal balance;
    private BigDecimal interestBalance;
    private BigDecimal interestPayment;
    private BigDecimal principlePayment;
    private BigDecimal payment;

    TermPaymentDetail(BigDecimal balance, BigDecimal interestBalance, BigDecimal interestPayment,
                      BigDecimal principlePayment, BigDecimal payment) {
        this.balance = balance;
        this.interestBalance = interestBalance;
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

    public BigDecimal getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(BigDecimal interestBalance) {
        this.interestBalance = interestBalance;
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
