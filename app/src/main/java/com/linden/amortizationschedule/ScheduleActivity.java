package com.linden.amortizationschedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static final String MONTHLY_PAYMENT = "MONTHLY_PAYMENT";
    public static final String TERM_IN_MONTHS = "TERM_IN_MONTHS";
    public static final String LOAN_AMOUNT = "LOAN_AMOUNT";
    public static final String INTEREST_RATE = "INTEREST_RATE";

    @BindView(R.id.amortization_schedule) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    List<TermPaymentDetail> termPayments = new ArrayList<>();
    DisposableObserver<List<TermPaymentDetail>> paymentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        ButterKnife.bind(this);
    }

    public static Intent getStartIntent(Context context, double monthlyPayment, int terms, double loanAmount,
                                        double interestRate) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        intent.putExtra(MONTHLY_PAYMENT, monthlyPayment);
        intent.putExtra(TERM_IN_MONTHS, terms);
        intent.putExtra(LOAN_AMOUNT, loanAmount);
        intent.putExtra(INTEREST_RATE, interestRate);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AmortizationAdapter(termPayments);
        recyclerView.setAdapter(adapter);
        createSchedule();
    }

    @OnClick(R.id.btn_view_schedule)
    public void exportTable (View view){

    }

    private void showProgress() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }

    @SuppressLint("CheckResult")
    public void createSchedule() {
        showProgress();
        new Thread(() -> {
            paymentsObservable().subscribeWith(new DisposableObserver<List<TermPaymentDetail>>() {

                @Override
                public void onNext(List<TermPaymentDetail> paymentDetails) {
                    termPayments = paymentDetails;
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
            paymentDetails.dispose();
        }).start();
    }

    private Observable<List<TermPaymentDetail>> paymentsObservable() {
        return Observable.just(calucalteMonthlyPayment());
    }

    private List<TermPaymentDetail> calucalteMonthlyPayment() {
        List<TermPaymentDetail> monthly = new ArrayList<>();
        double monthlyPaymentAmount = getIntent().getDoubleExtra(MONTHLY_PAYMENT, 0);
        double total = monthlyPaymentAmount * getIntent().getIntExtra(TERM_IN_MONTHS, 0);
        while (total > 0) {
            total = total - monthlyPaymentAmount;
            monthly.add(new TermPaymentDetail(BigDecimal.valueOf(total), null, null,
                    null, BigDecimal.valueOf(monthlyPaymentAmount)));
        }
        return monthly;
    }
}
