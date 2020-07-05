package com.linden.amortizationschedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class AmortizationAdapter extends RecyclerView.Adapter<AmortizationAdapter.TermPaymentViewHolder> {
    private List<TermPaymentDetail> termPayments;

    static class TermPaymentViewHolder extends RecyclerView.ViewHolder {

        TextView balance;
        TextView payment;
        TextView interest;
        TextView principal;
        TextView endBalance;
        TextView totalInterest;

        TermPaymentViewHolder(View itemView) {
            super(itemView);
            balance = itemView.findViewById(R.id.balance);
            payment = itemView.findViewById(R.id.you_paid);
            interest = itemView.findViewById(R.id.interest);
            principal = itemView.findViewById(R.id.principal);
            endBalance = itemView.findViewById(R.id.end_balance);
            totalInterest = itemView.findViewById(R.id.interest_paid);
        }
    }

    AmortizationAdapter(List<TermPaymentDetail> termPayments) {
        this.termPayments = termPayments;
    }

    @Override
    @NonNull
    public AmortizationAdapter.TermPaymentViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amortization_item, parent, false);
        return new TermPaymentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TermPaymentViewHolder holder, int position) {
        holder.balance.setText(
                "R".concat(termPayments.get(position).getBalance().toString()));
        holder.payment.setText(
                "R".concat(termPayments.get(position).getPayment().toString()));
        holder.interest.setText("-");
        holder.principal.setText("-");
        holder.endBalance.setText("-");
        holder.totalInterest.setText("-");

    }

    @Override
    public int getItemCount() {
        return termPayments.size();
    }
}
