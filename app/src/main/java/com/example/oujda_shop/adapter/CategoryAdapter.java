package com.example.oujda_shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.oujda_shop.R;
import com.example.oujda_shop.entities.Category;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categoryList;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoryList.size(); // Number of items in the list
    }

    @Override
    public Category getItem(int position) {
        return categoryList.get(position); // Returns the category object at the given position
    }

    @Override
    public long getItemId(int position) {
        return categoryList.get(position).getId(); // Unique ID for the category
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view every time (inefficient)
        convertView = inflater.inflate(R.layout.category_item, parent, false);

        TextView name = convertView.findViewById(R.id.category_name);
        TextView createdAt = convertView.findViewById(R.id.category_date);
        ImageView catImage = convertView.findViewById(R.id.category_image);

        Category category = categoryList.get(position);

        File imgFile = new File(category.getImageResource());

        if (imgFile.exists()) {
            catImage.setImageURI(Uri.fromFile(imgFile));
        } else {
            catImage.setImageResource(R.drawable.oujda_shop);
        }


        name.setText(category.getName());
        createdAt.setText(category.getCreatedAt());

        return convertView;
    }
    public void updateList(ArrayList<Category> newItems) {
        this.categoryList.clear(); // Clear the existing data
        this.categoryList.addAll(newItems); // Add the new data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }


}
