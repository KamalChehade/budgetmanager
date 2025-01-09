package com.example.budgetmanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.budgetmanager.R;
import com.example.budgetmanager.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        // Debugging: Log the data being bound to check if it matches
        Log.d("TransactionAdapter", "Binding transaction: " + transaction.getCategory() + ", " + transaction.getAmount() + ", " + transaction.getDate());

        // Check if views are properly initialized
        if (holder.category == null) {
            Log.e("TransactionAdapter", "Category TextView is null");
        }
        if (holder.amount == null) {
            Log.e("TransactionAdapter", "Amount TextView is null");
        }
        if (holder.note == null) {
            Log.e("TransactionAdapter", "Note TextView is null");
        }
        if (holder.date == null) {
            Log.e("TransactionAdapter", "Date TextView is null");
        }
        if (holder.categoryIcon == null) {
            Log.e("TransactionAdapter", "Category Icon ImageView is null");
        }

        // Bind data if views are not null
        if (holder.category != null) {
            holder.category.setText(transaction.getCategory());
        }
        if (holder.amount != null) {
            holder.amount.setText(String.format("%.2f", transaction.getAmount()));
        }
        if (holder.note != null) {
            holder.note.setText(transaction.getNote());
        }
        if (holder.date != null) {
            // Convert Date object to String before setting it in the TextView
            String formattedDate = formatDate(transaction.getDate());
            holder.date.setText(formattedDate);  // Set the formatted date as a String
        }

        // Set the category icon based on the category type
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
        TextView category, amount, note, date; // Add the date field here
        ImageView categoryIcon;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.transactionCategory);
            amount = itemView.findViewById(R.id.transactionAmount);
            note = itemView.findViewById(R.id.note);  // Make sure 'note' is in the layout
            date = itemView.findViewById(R.id.transactionDate); // Bind the date TextView here
            categoryIcon = itemView.findViewById(R.id.categoryIcon); // Bind the category icon ImageView here

            // Ensure views are found correctly
            if (category == null || amount == null || note == null || date == null || categoryIcon == null) {
                Log.e("TransactionAdapter", "Error binding views: one or more views are null");
            }
        }
    }

}
