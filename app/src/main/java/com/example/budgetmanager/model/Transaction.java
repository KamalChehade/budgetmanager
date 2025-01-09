package com.example.budgetmanager.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private String transactionType;
    private String category;
    private double amount;
    private String note;
    private Date date;  // Use Date object

    // Constructor with Date object
    public Transaction(String transactionType, String category, double amount, String note, Date date) {
        this.transactionType = transactionType;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    // Getters
    public String getTransactionType() {
        return transactionType;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public Date getDate() {
        return date;
    }

    // Method to get the current date in the desired format (day, month, year)
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
        return sdf.format(new Date());  // Format the current date as "Day Month, Year"
    }

    // Method to format a Date object to string
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
        return sdf.format(date);  // Format the given Date object
    }

    // Method to parse a date string back into a Date object
    public static Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
}
