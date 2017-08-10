package com.zack.tinga.applicationtest;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    TextView textViewCartItem;
    TextView textViewPrice_Dis;
    DatabaseReference mDatabase;
    ListView listViewCart;
    List<Cart> cartList;

    Double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        textViewCartItem = (TextView) findViewById(R.id.cart_item);

        textViewPrice_Dis = (TextView) findViewById(R.id.cart_total_price);

        String sum = PreferenceManager.getDefaultSharedPreferences(this).getString("sum", null);
        Toast.makeText(getApplicationContext(), sum, Toast.LENGTH_SHORT).show();

        String id = getIntent().getStringExtra("id");

        mDatabase = FirebaseDatabase.getInstance().getReference("cart").child(id);

        cartList = new ArrayList<>();

        listViewCart = (ListView) findViewById(R.id.list_cart);

//        sumMyIntValues(listViewCart);

        textViewPrice_Dis.setText(sum);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartList.clear();
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()){
                    Cart cart = cartSnapshot.getValue(Cart.class);

                    price = Double.parseDouble(String.valueOf(cart.getPrice()));

//                    countCart();

//                    Toast.makeText(getApplicationContext(), "Price: " + price, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "Count Price: " + listViewCart.getCount(), Toast.LENGTH_SHORT).show();
//                    System.out.println("Count Price: " );

//                    countCart(listViewCart);


                    cartList.add(cart);
                }
                CartAdapter skillAdapter = new CartAdapter(CartActivity.this, (ArrayList<Cart>) cartList);
                listViewCart.setAdapter(skillAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public double countCart(ListView listViewCart){

        double sum = 0.0;
        for (int i = 0; i < listViewCart.getCount(); i++) {
            TextView myView = (TextView) findViewById(R.id.cart_price);
            sum = sum + Double.parseDouble(myView.getText().toString());
        }
        System.out.println("Count Price: " + sum);
        return sum;
    }
}
