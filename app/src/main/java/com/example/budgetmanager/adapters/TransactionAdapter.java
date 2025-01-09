package com.example.budgetmanager.adapters;

import android.content.Context;
import android.util.Log;
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

    public TransactionAdapter(Context context) {
        this.context = context;
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

        Log.d("TransactionAdapter", "Binding transaction: " + transaction.getCategory() + ", " + transaction.getAmount() + ", " + transaction.getDate());

        if (holder.category != null) {
            holder.category.setText(transaction.getCategory());
        }
        if (holder.amount != null) {
            // Format the amount as currency and append $ symbol
            String amountWithCurrency = "$" + String.format("%.2f", transaction.getAmount());
            holder.amount.setText(amountWithCurrency);
        }
        if (holder.note != null) {
            holder.note.setText(transaction.getNote());
        }
        if (holder.date != null) {
            // Format the date to the required format (e.g., "January 09, 2025")
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            String formattedDate = sdf.format(transaction.getDate());
            holder.date.setText(formattedDate);
        }

        if (holder.accountLbl != null) {
            if (transaction.getTransactionType().equals("Income")) {
                holder.accountLbl.setText("Income");
                holder.accountLbl.setBackgroundColor(ContextCompat.getColor(context, R.color.greenColor)); // Green background for Income
            } else if (transaction.getTransactionType().equals("Expense")) {
                holder.accountLbl.setText("Expense");
                holder.accountLbl.setBackgroundResource(R.drawable.accounts_bg); // Default background for Expense
            }
        }

        if (holder.categoryIcon != null) {
            int iconResId = getCategoryIcon(transaction.getCategory());
            holder.categoryIcon.setImageResource(iconResId);
        }
    }

    // Helper method to format the Date object as a String
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
        return sdf.format(date); // Convert the Date object to String in "dd MMM, yyyy" format
    }




    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactionList = transactions;
        notifyDataSetChanged();
    }

    // Helper method to get the correct icon based on the category
    private int getCategoryIcon(String category) {
        switch (category) {
            case "Salary":
                return R.drawable.ic_salary; // Ensure you have this icon in the drawable folder
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
    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView category, amount, note, date, accountLbl; // Add accountLbl here
        ImageView categoryIcon;

        public TransactionViewHolder(View itemView) {
            super(itemView);

            // Initialize views using findViewById()
            category = itemView.findViewById(R.id.transactionCategory);
            amount = itemView.findViewById(R.id.transactionAmount);
            note = itemView.findViewById(R.id.note); // Make sure 'note' is in the layout
            date = itemView.findViewById(R.id.transactionDate); // Bind the date TextView here
            accountLbl = itemView.findViewById(R.id.accountLbl); // Bind the accountLbl TextView here
            categoryIcon = itemView.findViewById(R.id.categoryIcon); // Bind the category icon ImageView here

            // Ensure views are found correctly
            if (category == null || amount == null || note == null || date == null || accountLbl == null || categoryIcon == null) {
                Log.e("TransactionAdapter", "Error binding views: one or more views are null");
            }
        }
    }

}
