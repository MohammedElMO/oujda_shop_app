package com.example.oujda_shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.oujda_shop.R;
import com.example.oujda_shop.entities.Category;
import com.example.oujda_shop.entities.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> productList;
    private LayoutInflater inflater;

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.product_item, parent, false);

        // Bind views
        TextView categoryBadge = convertView.findViewById(R.id.categoryBadge);
        ImageView productImage = convertView.findViewById(R.id.productImage);
        TextView productName = convertView.findViewById(R.id.productName);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        TextView productDate = convertView.findViewById(R.id.productDate);
        TextView productDescription = convertView.findViewById(R.id.productDescription);
        CardView cardView = convertView.findViewById(R.id.productCard);

        Product product = productList.get(position);
        categoryBadge.setText(product.getCategory().getName());

        File imgFile = new File(product.getImageUrl());

        if (imgFile.exists()) {
            productImage.setImageURI(Uri.fromFile(imgFile));
        } else {
            productImage.setImageResource(R.drawable.oujda_shop);
        }
        productName.setText(product.getName());
        productPrice.setText(String.valueOf(product.getPrice()) + " DH") ;
        productDescription.setText(product.getDescription());
        productDate.setText(product.getCreatedAt());
        return convertView;
    }

    public void updateList(ArrayList<Product> newItems) {
        this.productList.clear();
        this.productList.addAll(newItems);
        notifyDataSetChanged();
    }
}
