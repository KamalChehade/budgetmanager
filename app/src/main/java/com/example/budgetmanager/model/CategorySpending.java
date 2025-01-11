package com.example.budgetmanager.model;

public class CategorySpending {
    private String category;
    private double totalSpending;

    public CategorySpending(String category, double totalSpending) {
        this.category = category;
        this.totalSpending = totalSpending;
    }

    public String getCategory() {
        return category;
    }

    public double getTotalSpending() {
        return totalSpending;
    }
}
