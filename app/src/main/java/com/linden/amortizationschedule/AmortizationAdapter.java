package com.linden.amortizationschedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        TermPaymentViewHolder(View itemView) {
            super(itemView);
            balance = itemView.findViewById(R.id.balance);
            payment = itemView.findViewById(R.id.you_paid);
            interest = itemView.findViewById(R.id.interest);
            principal = itemView.findViewById(R.id.principal);
            endBalance = itemView.findViewById(R.id.end_balance);
        }
    }

    AmortizationAdapter(List<TermPaymentDetail> termPayments) {
        this.termPayments = termPayments;
    }

    @Override
    @NonNull
    public AmortizationAdapter.TermPaymentViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amortization_item, parent, false);
        return new TermPaymentViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull TermPaymentViewHolder holder, int position) {
        holder.balance.setText(
                FinanceUtil.getRandValue(termPayments.get(position).getBalance()));
        holder.payment.setText(
                FinanceUtil.getRandValue(termPayments.get(position).getPayment()));
        holder.interest.setText(
                FinanceUtil.getRandValue(termPayments.get(position).getInterestPayment()));
        holder.principal.setText(
                FinanceUtil.getRandValue(termPayments.get(position).getPrinciplePayment()));
        holder.endBalance.setText(
                FinanceUtil.getRandValue(termPayments.get(position).getCapitalBalance()));
    }

    @Override
    public int getItemCount() {
        return termPayments.size();
    }
}
