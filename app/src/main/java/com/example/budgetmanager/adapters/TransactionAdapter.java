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
        // Format the amount as currency and append $ symbol
        String amountWithCurrency = "$" + String.format("%.2f", transaction.getAmount());
        holder.amount.setText(amountWithCurrency);
        holder.note.setText(transaction.getNote());

        // Format the date to the required format (e.g., "January 09, 2025")
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(transaction.getDate());
        holder.date.setText(formattedDate);

        // Handle transaction type for accountLbl
        if (transaction.getTransactionType().equals("Income")) {
            holder.accountLbl.setText("Income");
            holder.accountLbl.setBackgroundColor(ContextCompat.getColor(context, R.color.greenColor)); // Green background for Income
        } else if (transaction.getTransactionType().equals("Expense")) {
            holder.accountLbl.setText("Expense");
            holder.accountLbl.setBackgroundResource(R.drawable.accounts_bg); // Default background for Expense
        }

        // Set the category icon based on the category type
        int iconResId = getCategoryIcon(transaction.getCategory());
        holder.categoryIcon.setImageResource(iconResId);

        // Add long press event listener for the delete confirmation
        holder.itemView.setOnLongClickListener(v -> {
             onTransactionClickListener.onTransactionLongClick(transaction); // Notify listener
            return true; // Indicate the long press was handled
        });

        holder.itemView.setOnClickListener(v -> {
            v.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent)); // Reset background color when clicked
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
                return R.drawable.ic_other; // Default icon
        }
    }

    // Interface to handle long click
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
