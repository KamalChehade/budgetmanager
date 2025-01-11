package com.example.budgetmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.budgetmanager.R;
import com.example.budgetmanager.model.Transaction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;
    private Context context;
    private OnTransactionClickListener onTransactionClickListener;

    public TransactionAdapter(Context context, OnTransactionClickListener listener) {
        this.context = context;
        this.onTransactionClickListener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        holder.category.setText(transaction.getCategory());

         String amountWithCurrency = "$" + String.format("%.2f", transaction.getAmount());
        holder.amount.setText(amountWithCurrency);
        holder.note.setText(transaction.getNote());

         SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(transaction.getDate());
        holder.date.setText(formattedDate);


        if (transaction.getTransactionType().equals("Income")) {
            holder.accountLbl.setText("Income");
            holder.accountLbl.setBackgroundColor(ContextCompat.getColor(context, R.color.greenColor));
        } else if (transaction.getTransactionType().equals("Expense")) {
            holder.accountLbl.setText("Expense");
            holder.accountLbl.setBackgroundResource(R.drawable.accounts_bg);
        }

         int iconResId = getCategoryIcon(transaction.getCategory());
        holder.categoryIcon.setImageResource(iconResId);


        holder.itemView.setOnLongClickListener(v -> {
             onTransactionClickListener.onTransactionLongClick(transaction);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            v.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        });
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactionList = transactions;
        notifyDataSetChanged();
    }

    private int getCategoryIcon(String category) {
        switch (category) {
            case "Salary":
                return R.drawable.ic_salary;
            case "Loan":
                return R.drawable.ic_loan;
            case "Food":
                return R.drawable.ic_food;
            case "Grocery":
                return R.drawable.ic_grocery;
            default:
                return R.drawable.ic_other;
        }
    }


    public interface OnTransactionClickListener {
        void onTransactionLongClick(Transaction transaction);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView category, amount, note, date, accountLbl;
        ImageView categoryIcon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.transactionCategory);
            amount = itemView.findViewById(R.id.transactionAmount);
            note = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.transactionDate);
            accountLbl = itemView.findViewById(R.id.accountLbl);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
        }
    }
}
