package com.zack.tinga.applicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewProducts extends AppCompatActivity {

    private ImageView imageViewPerson;
    private TextView textViewPrice, textViewName;

    String id, name, img;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseReference;
    private Button mCart;
    String type;
    double price;
    double defaultValue = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        id = getIntent().getStringExtra(MainActivity.PRODUCT_ID);
        name = getIntent().getStringExtra(MainActivity.PRODUCT_NAME);
        price = getIntent().getDoubleExtra(MainActivity.PRODUCT_PRICE, defaultValue);
        type = getIntent().getStringExtra(MainActivity.PRODUCT_TYPE);

        mCart = (Button) findViewById(R.id.add_to_cart);
        mDatabase = FirebaseDatabase.getInstance().getReference("products");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("cart").child(id);

        Toast.makeText(getApplicationContext(), "Name: " + name, Toast.LENGTH_SHORT).show();

        imageViewPerson = (ImageView) findViewById(R.id.imageViewPerson);
        textViewName = (TextView) findViewById(R.id.txtName);
        textViewPrice = (TextView) findViewById(R.id.txt_Price);
        textViewPrice.setText(String.valueOf(price));
        textViewName.setText(name);
//        textViewRating.setText();

        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cart cart = new Cart(id, name, type, img, price);
                mDatabaseReference.push().setValue(cart);
                Toast.makeText(getApplicationContext(), "Successfully Added.", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                img = (String) dataSnapshot.child("img").getValue();
                type = (String) dataSnapshot.child("type").getValue();
                price = (Double) dataSnapshot.child("price").getValue();
                Picasso.with(getApplicationContext()).load(img).into(imageViewPerson);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart){
            Intent nextIntent = new Intent(getApplicationContext(), CartActivity.class);
            nextIntent.putExtra("id", id);
            startActivity(nextIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
