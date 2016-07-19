package com.android.atiqorin.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText name;
    EditText qunatity;
    EditText price;
    EditText emailID;
    File imageFile;
    Uri imageUri;
    String imageName = "Item_";
    Button imageCaputre;
    LinearLayout dialogLayout;
    ArrayList<Inventory> dataPOJO;
    DBHelper sqlHelper;
    ListView inventory;
    TextView info;
    RelativeLayout infoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper db = new DBHelper(this);
        int count = db.getCount();
        if(count != 0) {
            info = (TextView)findViewById(R.id.info);
            info.setVisibility(View.GONE);
        }
        sqlHelper = new DBHelper(MainActivity.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        inventory = (ListView) findViewById(R.id.appInventorLV);
        dataPOJO = sqlHelper.getInventory();
        inventory.setAdapter(new InventoryAdapter(MainActivity.this, dataPOJO));
        imageName += imageName + sqlHelper.getCount();
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLayout = new LinearLayout(MainActivity.this);
                dialogLayout.setOrientation(LinearLayout.VERTICAL);

                name = new EditText(MainActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                name.setHint("Enter product name");
                name.setLayoutParams(params);
                dialogLayout.addView(name);
                qunatity = new EditText(MainActivity.this);
                LinearLayout.LayoutParams QunatityParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                qunatity.setHint("Enter product qunatity");
                qunatity.setLayoutParams(QunatityParams);
                dialogLayout.addView(qunatity);
                price = new EditText(MainActivity.this);
                LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                price.setHint("Enter product price");
                price.setLayoutParams(priceParams);
                dialogLayout.addView(price);
                emailID = new EditText(MainActivity.this);
                final LinearLayout.LayoutParams emailIdParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                emailID.setHint("Enter Suppliers Email Id");
                emailID.setLayoutParams(emailIdParams);
                dialogLayout.addView(emailID);
                imageCaputre = new Button(MainActivity.this);
                LinearLayout.LayoutParams imgParameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageCaputre.setText("Capture photo for Item");
                imageCaputre.setLayoutParams(imgParameter);
                imageCaputre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePicture();
                    }
                });

                dialogLayout.addView(imageCaputre);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add Products")
                        .setMessage("Enter Details")
                        .setView(dialogLayout)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input_name = name.getText().toString();
                                String input_Quantity = qunatity.getText().toString();
                                String input_price = price.getText().toString();
                                String intput_email = emailID.getText().toString();
                                if (isValidEmail(intput_email) == true && isvaliditem(input_name) == true && isvaliditem(input_Quantity) && isvaliditem(input_price)) {
                                    if (isVaildInt(input_Quantity)) {
                                        if (imageFile != null) {
                                            sqlHelper.addInventoryData(new Inventory(input_name, input_Quantity, input_price, "0", intput_email, imageFile.getAbsolutePath()));
                                        } else {
                                            sqlHelper.addInventoryData(new Inventory(input_name, input_Quantity, input_price, "0", intput_email, ""));
                                        }
                                        refresh();
                                        Toast.makeText(MainActivity.this, "Entry added", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Not a Number", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Please Enter Details", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Entry Cancelled!", Toast.LENGTH_LONG).show();

                    }
                }).create().show();
            }
        });
        inventory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, InventoryActivity.class);
                int currrentItemId = Integer.parseInt(view.getTag().toString());
                i.putExtra("id", currrentItemId);
                startActivity(i);
            }
        });
    }

    public void refresh() {
        InventoryAdapter adapter = new InventoryAdapter(MainActivity.this, sqlHelper.getInventory());
        DBHelper db = new DBHelper(this);
        int count = db.getCount();
        info = (TextView)findViewById(R.id.info);
        if(count != 0) {
            info.setVisibility(View.GONE);
        } else{
            info.setVisibility(View.VISIBLE);
        }
        inventory.setAdapter(adapter);
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume (){
        super.onResume();
        refresh();
    }

    private void takePicture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageName);
        imageUri = Uri.fromFile(imageFile);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(i, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ;
            imageCaputre.setText(imageFile.getName());
        } else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

        }
    }

    public final static boolean isvaliditem(String text) {
        if (text == null | text.equals("")) {
            return false;
        } else {
            return true;

        }
    }
    public void modifyInventory(View v) {
        int id = Integer.parseInt(v.getTag().toString());
        sqlHelper.modifyitemsSalesStats(id);
        Toast.makeText(MainActivity.this, "Item Sold!", Toast.LENGTH_SHORT).show();
        refresh();
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public boolean isVaildInt(String data) {
        return TextUtils.isDigitsOnly(data);
    }
}