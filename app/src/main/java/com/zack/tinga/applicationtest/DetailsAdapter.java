package com.zack.tinga.applicationtest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 2017/07/28.
 */

public class DetailsAdapter extends ArrayAdapter<Product> {

    private Activity context;
    private List<Product> productsList;

    public DetailsAdapter(@NonNull Activity context, List<Product> productsList) {
        super(context, R.layout.list_layout, productsList);
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewPrice);
        ImageView imageViewImage = (ImageView) listViewItem.findViewById(R.id.imageView_product);

        Product product = productsList.get(position);
        textViewName.setText(product.getName());
        textViewGenre.setText("R" + product.getPrice());
        Picasso.with(context).load(product.getImg()).into(imageViewImage);

        return listViewItem;
    }
}
