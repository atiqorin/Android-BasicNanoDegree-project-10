package com.android.atiqorin.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by atiqorin on 7/4/16.
 */
public class InventoryActivity extends AppCompatActivity {
    int id = -1;
    TextView title, quantity, price, sale;
    ImageView productImage;
    DBHelper sqlHelper;
    Inventory data;
    File imgFile;
    ArrayList<Inventory> dataFromObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_detail);
        sqlHelper = new DBHelper(this);
        Intent i = getIntent();
        if (i.hasExtra("id")) {
            id = i.getExtras().getInt("id");
            title = (TextView) findViewById(R.id.detailsTitle);
            quantity = (TextView) findViewById(R.id.detailsQuantity);
            price = (TextView) findViewById(R.id.detailsPrice);
            sale = (TextView) findViewById(R.id.detailsSales);
            productImage = (ImageView) findViewById(R.id.detailsImage);
            reSyncView();
            if (checkImageExists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                productImage.setImageBitmap(myBitmap);
            } else {
                productImage.setImageResource(R.drawable.placeholder);
            }

        } else {
            Toast.makeText(InventoryActivity.this, "Item not present", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void reSyncView() {
        dataFromObject = sqlHelper.getrowInventory(id);
        data = dataFromObject.get(0);

        title.setText("Name: " + data.getName());
        quantity.setText("Quantity: " + data.getQuantity());
        price.setText("Price: " + data.getPrice());
        sale.setText("Sales: " + data.getSales());
        imgFile = new File(data.getImagePath());
    }
    public void quantityPlus(View v) {
        sqlHelper.addQuantity(id);
        reSyncView();
    }
    public void quantityMinus(View v) {
        sqlHelper.subtarctQuantity(id);
        reSyncView();
    }
    public void orderMore(View v) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{data.getEmail()});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Order More item");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please add some more items" + data.getName());

        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    public void deleteThisItem(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Add Products")
                .setMessage("Enter Specific Details")
                .setPositiveButton("Delete Current Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlHelper.deleteInventory(id);
                        Toast.makeText(InventoryActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(InventoryActivity.this, "Item delete cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }
    public boolean checkImageExists() {
        if (imgFile.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
