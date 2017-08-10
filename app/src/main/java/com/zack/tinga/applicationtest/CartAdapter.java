package com.zack.tinga.applicationtest;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/08/03.
 */

public class CartAdapter extends ArrayAdapter<Cart> {

    private Activity context;
    private List<Cart> carts;

    private boolean isPrinted = false;
    double sum = 0.0;

    public CartAdapter(@NonNull Activity context, ArrayList<Cart> carts) {
        super(context, R.layout.cart_list_item, carts);
        this.carts = carts;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View cartViewItems = inflater.inflate(R.layout.cart_list_item, null, false);

        TextView textViewName = cartViewItems.findViewById(R.id.cart_item);
        TextView textViewPrice = cartViewItems.findViewById(R.id.cart_price);
        TextView textViewPriceTotal = cartViewItems.findViewById(R.id.cart_total_price);
        ImageView imageViewCart = cartViewItems.findViewById(R.id.cart_img);

        Cart cart = carts.get(position);
        textViewName.setText(cart.getName());
        textViewPrice.setText(String.valueOf(cart.getPrice()));
        Picasso.with(context).load(cart.getImg()).into(imageViewCart);

        if(!isPrinted)
        {
            countCart();
            textViewPriceTotal.setText(String.valueOf(sum));
            isPrinted = true;
        }

        return cartViewItems;
    }

    public double countCart(){
//
        for (int i = 0; i < carts.size(); i++) {
            sum = sum + carts.get(i).getPrice();
        }
        System.out.println("Count Price: " + sum);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("sum", String.valueOf(sum)).apply();

        return sum;
    }

}
