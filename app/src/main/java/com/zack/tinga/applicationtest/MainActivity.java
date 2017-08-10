package com.zack.tinga.applicationtest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 1;
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_TYPE = "type";
    private EditText editTextName;
    private Button buttonAdd;
    private Spinner spinnerType;
    private ImageButton imageButton;
    private Button image_select;
    private ListView listViewProducts;
    private DatabaseReference mDatabase, mCartDatabaseReference;
    private StorageReference mStorageReference;

    private List<Product> products;
    private Uri imageUri;

    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_ID = "id";

    EditText editText_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference("products");
        mCartDatabaseReference = FirebaseDatabase.getInstance().getReference("cart");
        mStorageReference = FirebaseStorage.getInstance().getReference().child("product_images");

        products = new ArrayList<>();

        editTextName = (EditText) findViewById(R.id.editText_name);
        buttonAdd = (Button) findViewById(R.id.button_submit);
        spinnerType = (Spinner) findViewById(R.id.spinner);
        image_select = (Button) findViewById(R.id.image_select);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        listViewProducts = (ListView) findViewById(R.id.list);
        editText_Price = (EditText) findViewById(R.id.editText_price);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Product product = products.get(i);

                Intent addmoreIntent = new Intent(MainActivity.this, ViewProducts.class);
                addmoreIntent.putExtra(PRODUCT_NAME, product.getName());
                addmoreIntent.putExtra(PRODUCT_ID, product.getId());
                addmoreIntent.putExtra(PRODUCT_PRICE, product.getPrice());
                addmoreIntent.putExtra(PRODUCT_TYPE, product.getType());
                startActivity(addmoreIntent);
            }
        });

        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product d = products.get(i);
                showUpdateDialog(d.getId(), d.getName());
                return false;
            }
        });

        selectImage();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                products.clear();

                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()) {
                    Product product = productsSnapshot.getValue(Product.class);
                    products.add(product);
                }

                Toast.makeText(getApplicationContext(), "List Size: " + products.size(), Toast.LENGTH_SHORT).show();

                DetailsAdapter adapter = new DetailsAdapter(MainActivity.this, products);
                listViewProducts.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showUpdateDialog(final String person_id, String person_name){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        TextView textViewName = dialogView.findViewById(R.id.textView_name);
        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        final Spinner spinnerType = dialogView.findViewById(R.id.spinner_genres);
        Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
        Button buttonDelete = dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Update " + person_name);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDetails(person_id);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                double price = Double.parseDouble(editTextPrice.getText().toString().trim());
                String type = spinnerType.getSelectedItem().toString();

                if (TextUtils.isEmpty(name)){
                    editTextName.setError("Name required");
                    return;
                }
                updateDetails(person_id, name, type, price);

                alertDialog.dismiss();
            }
        });
    }

    private void deleteDetails(String productId) {

        DatabaseReference dProducts = FirebaseDatabase.getInstance().getReference("products").child(productId);
        DatabaseReference dSkills = FirebaseDatabase.getInstance().getReference("skill").child(productId);

        dProducts.removeValue();
        dSkills.removeValue();

        Toast.makeText(getApplicationContext(), "Product deleted", Toast.LENGTH_SHORT).show();

    }

    private boolean updateDetails(String id, String name, String type, double price){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("products").child(id);

        Product product = new Product(id, name, type, price);
        mDatabase.setValue(product);
        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void addProduct() {

        final String name = editTextName.getText().toString().trim();
        final double price = Double.parseDouble(editText_Price.getText().toString().trim());
        final String type = spinnerType.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name) && imageUri != null) {

            StorageReference filePath = mStorageReference.child(imageUri.getLastPathSegment());

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri img_download = taskSnapshot.getDownloadUrl();
                    String id = mDatabase.push().getKey();

                    Product product = new Product(id, name, type, img_download.toString(), price);
                    mDatabase.child(id).setValue(product);

                    Toast.makeText(getApplicationContext(), "Product Added!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "You should enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {

        image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }
    }
}
