package com.android.atiqorin.inventoryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by atiqorin on 7/4/16.
 */
public class InventoryAdapter extends BaseAdapter {
    ArrayList<Inventory> InvnetoryPOJO;
    Context context;
    File imgFile;


    public InventoryAdapter(Context context, ArrayList<Inventory> InvnetoryPOJO) {
        this.context = context;
        this.InvnetoryPOJO = InvnetoryPOJO;
    }

    public InventoryAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return InvnetoryPOJO.size();
    }

    @Override
    public Object getItem(int position) {
        return InvnetoryPOJO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_view, parent, false);
        }
        TextView Name = (TextView) row.findViewById(R.id.inventoryTitle);
        TextView quantity = (TextView) row.findViewById(R.id.inventoryQuantity);
        TextView price = (TextView) row.findViewById(R.id.inventoryPrice);
        TextView sales = (TextView) row.findViewById(R.id.inventorySales);
        TextView email = (TextView) row.findViewById(R.id.inventoryEmail);
        ImageView image = (ImageView) row.findViewById(R.id.inventoryImage);
        Button saleOne = (Button) row.findViewById(R.id.inventorysaleButton);

        final Inventory temp_obj = InvnetoryPOJO.get(position);
        imgFile = new File(temp_obj.getImagePath());
        if (checkImageExists()) {


            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(myBitmap);
        } else {
            image.setImageResource(R.drawable.placeholder);
        }
        DBHelper db = new DBHelper(context);
        int count = db.getCount();
        Name.setText("Item Name: " + temp_obj.getName());
        quantity.setText("Quantity: " + temp_obj.getQuantity());
        price.setText("Price: " + temp_obj.getPrice());
        sales.setText("Sales: " + temp_obj.getSales());
        email.setText("Email: " + temp_obj.getEmail());
        row.setTag(temp_obj.getId());
        saleOne.setTag(temp_obj.getId());
        row.setTag(temp_obj.getId());
        return row;
    }

    public boolean checkImageExists() {
        if (imgFile.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
