package com.example.budgetmanager.ui.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.budgetmanager.R;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    private final String[] categories;
    private final int[] icons;

    public CategorySpinnerAdapter(@NonNull Context context, String[] categories, int[] icons) {
        super(context, R.layout.spinner_item_with_icon, categories);
        this.categories = categories;
        this.icons = icons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createCustomView(position, convertView, parent);
    }

    private View createCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_with_icon, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinnerItemText);
        ImageView imageView = convertView.findViewById(R.id.spinnerItemIcon);

        textView.setText(categories[position]);
        imageView.setImageResource(icons[position]);

        return convertView;
    }
}
