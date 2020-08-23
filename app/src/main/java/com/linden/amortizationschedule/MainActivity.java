package com.linden.amortizationschedule;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.amount) EditText amount;
    @BindView(R.id.interest) EditText interest;
    @BindView(R.id.term) EditText term;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.calculated_monthly_payment) TextView calculatedMonthlyPayment;
    @BindView(R.id.calculated_total_interest) TextView calculatedTotalInterest;
    @BindView(R.id.calculated_total_amount) TextView calculatedTotalAmount;

    private double loanAmount;
    private double interestRate;
    private int termInMonths;
    private double monthlyPaymentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_calculate)
    public void createSchedule(View view) {
        showProgress();
        new Thread(() -> {
            loanAmount = Double.valueOf(amount.getText().toString());
            interestRate = Double.valueOf(interest.getText().toString());
            termInMonths = Integer.valueOf(term.getText().toString());
            paymentsObservable().subscribeWith(new DisposableObserver<Double>() {

                @Override
                public void onNext(Double paymentDetails) {
                    monthlyPaymentAmount = paymentDetails;
                    displayBreakdown();
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            }).dispose();
        }).start();
    }

    private Observable<Double> paymentsObservable() {
        return Observable.just(calculateMonthlyPayment());
    }

    private Double calculateMonthlyPayment() {
        monthlyPaymentAmount = Math.floor(FinanceUtil.calculatePMT(interestRate, termInMonths, loanAmount));
        return monthlyPaymentAmount;
    }

    private void displayBreakdown() {
        double balanceTotal = monthlyPaymentAmount * termInMonths;
        runOnUiThread(() -> {
            calculatedMonthlyPayment.setText(FinanceUtil.getRandValue(monthlyPaymentAmount));
            calculatedTotalInterest.setText(FinanceUtil.getRandValue((balanceTotal - loanAmount)));
            calculatedTotalAmount.setText(FinanceUtil.getRandValue(balanceTotal));
            progressBar.setVisibility(View.GONE);
        });
    }

    @OnClick(R.id.btn_view_schedule)
    public void viewSchedule (View view){
        startActivity(ScheduleActivity.getStartIntent(this, monthlyPaymentAmount, termInMonths, loanAmount, interestRate));
    }

    private void showProgress() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }
}
