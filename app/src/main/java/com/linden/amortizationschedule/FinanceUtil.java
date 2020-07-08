package com.linden.amortizationschedule;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FinanceUtil {

    public static String getRandValue(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        String formatted =  format.format(value);
        return "R".concat(formatted.substring(1));
    }

    public static String getRandValue(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        String formatted =  format.format(value);
        return "R".concat(formatted.substring(1));
    }

    public static double interestPaid(double total, double monthlyPaymentAmount, double interestRate) {
        return monthlyPaymentAmount - (total * (interestRate/12));
    }

    /**
     * Calculate monthly payments
     *
     * @param rate Annual interest rate divided by 12 is the monthly interest rate
     * @param nper Number of loans, unit month The total number of payments for this loan.
     * @param pv   The sum of the loan amount, present value, or current value of a series of future payments, also known as the principal.
     * @return
     */
    public static double calculatePMT(double rate, double nper, double pv) {
        double v = (1 + (rate / 12));
        double t = (-(nper / 12) * 12);
        return (pv * (rate / 12)) / (1 - Math.pow(v, t));
    }

    /**
     * Future value of an amount given the number of payments, rate, amount
     * of individual payment, present value and boolean value indicating whether
     * payments are due at the beginning of period
     * (false => payments are due at end of period)
     * @param r rate
     * @param n num of periods
     * @param y pmt per period
     * @param p future value
     * @param t type (true=pmt at end of period, false=pmt at begining of period)
     */
    public static double fv(double r, double n, double y, double p, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1*(p+(n*y));
        }
        else {
            double r1 = r + 1;
            retval =((1-Math.pow(r1, n)) * (t ? r1 : 1) * y ) / r
                    -
                    p*Math.pow(r1, n);
        }
        return retval;
    }
}
