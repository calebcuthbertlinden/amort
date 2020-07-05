package com.linden.amortizationschedule;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.amortization_schedule)
    RecyclerView recyclerView;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.interest)
    EditText interest;
    @BindView(R.id.term)
    EditText term;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private double loanAmount;
    private double interestRate;
    private int termInMonths;
    private double monthlyPaymentAmount;
    List<TermPaymentDetail> termPayments = new ArrayList<>();
    DisposableObserver<List<TermPaymentDetail>> paymentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //
        paymentDetails = new DisposableObserver<List<TermPaymentDetail>>() {

            @Override
            public void onNext(List<TermPaymentDetail> paymentDetails) {
                termPayments = paymentDetails;
                displaySchedule();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };
    }

    private void showProgress() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }

    @OnClick(R.id.btn_calculate)
    public void createSchedule(View view) {
        showProgress();
        new Thread(() -> {
            loanAmount = Double.valueOf(amount.getText().toString());
            interestRate = Double.valueOf(interest.getText().toString());
            termInMonths = Integer.valueOf(term.getText().toString());
            paymentsObservable().subscribeWith(paymentDetails);
            paymentDetails.dispose();
        }).start();
    }

    @OnClick(R.id.btn_view_schedule)
    public void viewSchedule (View view){

    }

    private void displaySchedule() {
        runOnUiThread(() -> {
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new AmortizationAdapter(termPayments);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        });
    }

    /**
     * Calculate monthly payments
     *
     * @param rate Annual interest rate divided by 12 is the monthly interest rate
     * @param nper Number of loans, unit month The total number of payments for this loan.
     * @param pv   The sum of the loan amount, present value, or current value of a series of future payments, also known as the principal.
     * @return
     */
    public double calculatePMT(double rate, double nper, double pv) {
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
    public double fv(double r, double n, double y, double p, boolean t) {
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

    private Observable<List<TermPaymentDetail>> paymentsObservable() {
        return Observable.just(calucalteMonthlyPayment());
    }

    private List<TermPaymentDetail> calucalteMonthlyPayment() {
        List<TermPaymentDetail> monthly = new ArrayList<>();
        monthlyPaymentAmount = Math.floor(calculatePMT(interestRate, termInMonths, loanAmount));
        double total = monthlyPaymentAmount * termInMonths;
        while (total > 0) {
            total = total - monthlyPaymentAmount;
            monthly.add(new TermPaymentDetail(BigDecimal.valueOf(total), null, null,
                    null, BigDecimal.valueOf(monthlyPaymentAmount)));
        }
        return monthly;
    }
}
