package com.linden.amortizationschedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
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
    @BindView(R.id.schedule_progress_bar) ProgressBar progressBar;

    List<TermPaymentDetail> termPayments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        ButterKnife.bind(this);
        showProgress();
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
        createSchedule();
    }

    @SuppressLint("CheckResult")
    public void createSchedule() {
        paymentsObservable().subscribeWith(new DisposableObserver<List<TermPaymentDetail>>() {
            @Override
            public void onNext(List<TermPaymentDetail> paymentDetails) {
                termPayments = paymentDetails;
                runOnUiThread(() -> displayScheduleTable());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        }).dispose();
    }

    private Observable<List<TermPaymentDetail>> paymentsObservable() {
        return Observable.just(calculateAllTermPayments());
    }

    private List<TermPaymentDetail> calculateAllTermPayments() {
        List<TermPaymentDetail> monthly = new ArrayList<>();
        double monthlyPaymentAmount = getIntent().getDoubleExtra(MONTHLY_PAYMENT, 0);
        double total = monthlyPaymentAmount * getIntent().getIntExtra(TERM_IN_MONTHS, 0);
        while (total > 0) {
            total = total - monthlyPaymentAmount;
            double capitalPaid = FinanceUtil.interestPaid(total, monthlyPaymentAmount,
                    (getIntent().getDoubleExtra(INTEREST_RATE, 0)));
            monthly.add(new TermPaymentDetail(
                    BigDecimal.valueOf(total),
                    BigDecimal.valueOf(Math.floor(total - (monthlyPaymentAmount - capitalPaid))),
                    BigDecimal.valueOf(Math.floor(monthlyPaymentAmount - capitalPaid)),
                    BigDecimal.valueOf(Math.floor(capitalPaid)),
                    BigDecimal.valueOf(Math.floor(monthlyPaymentAmount))));
        }
        return monthly;
    }

    @OnClick(R.id.export_table)
    public void exportTable (View view){
        try {
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);

            StringBuilder data = new StringBuilder();
            data.append("Total balance,Monthly payment,Interest paid,Capital balance paid,Remaining capital balance");
            for (TermPaymentDetail termPayment : termPayments) {
                data.append("\n" + termPayment.getBalance().toString()+","
                        +termPayment.getPayment().toString()+","
                        +termPayment.getInterestPayment().toString()+","
                        +termPayment.getPrinciplePayment().toString()+","
                        +termPayment.getCapitalBalance().toString());
            }

            out.write((data.toString()).getBytes());
            out.close();

            Context context = getApplicationContext();
            File file = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.linden.amortizationschedule.fileprovider", file);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));


        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void displayScheduleTable() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        adapter = new AmortizationAdapter(termPayments);
        recyclerView.setAdapter(adapter);
    }
}
