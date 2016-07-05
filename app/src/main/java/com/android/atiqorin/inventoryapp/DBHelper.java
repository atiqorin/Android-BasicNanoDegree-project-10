package com.android.atiqorin.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by atiqorin on 7/4/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    ArrayList<Inventory> inventory;
    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_INVENTORY = "Inventory";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SALES = "sales";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGEPATH = "imagepath";
    private static final String TABLE_NAME = "inventorydata";

    public DBHelper(Context context) {
        super(context, DATABASE_INVENTORY, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE inventorydata ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " +
                "quantity TEXT," + "price TEXT," + "sales TEXT," + " email TEXT," + "imagepath TEXT  )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS inventorydata");

        onCreate(db);
    }

    void addInventoryData(Inventory Inventory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, Inventory.getName());
        values.put(KEY_QUANTITY, Inventory.getQuantity());
        values.put(KEY_PRICE, Inventory.getPrice());
        values.put(KEY_SALES, Inventory.getSales());
        values.put(KEY_EMAIL, Inventory.getEmail());
        values.put(KEY_IMAGEPATH, Inventory.getImagePath());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Inventory> getInventory() {
        ArrayList<Inventory> getAllData = new ArrayList<Inventory>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Inventory content = new Inventory();
                content.setId(Integer.parseInt(cursor.getString(0)));
                content.setName(cursor.getString(1));
                content.setQuantity(cursor.getString(2));
                content.setPrice(cursor.getString(3));
                content.setSales(cursor.getString(4));
                content.setEmail(cursor.getString(5));
                content.setImagePath(cursor.getString(6));
                getAllData.add(content);
            } while (cursor.moveToNext());
        }

        return getAllData;
    }

    public ArrayList<Inventory> getrowInventory(int id) {
        ArrayList<Inventory> getAllData = new ArrayList<Inventory>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE id=" + id + " LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Inventory content = new Inventory();
                content.setId(Integer.parseInt(cursor.getString(0)));
                content.setName(cursor.getString(1));
                content.setQuantity(cursor.getString(2));
                content.setPrice(cursor.getString(3));
                content.setSales(cursor.getString(4));
                content.setEmail(cursor.getString(5));
                content.setImagePath(cursor.getString(6));
                getAllData.add(content);
            } while (cursor.moveToNext());
        }
        return getAllData;
    }


    public int updateInventory(Inventory content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, content.getName());
        values.put(KEY_QUANTITY, content.getQuantity());
        values.put(KEY_PRICE, content.getPrice());
        values.put(KEY_SALES, content.getSales());
        values.put(KEY_EMAIL, content.getEmail());
        values.put(KEY_IMAGEPATH, content.getImagePath());

        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
    }

    public int updateSales(Inventory content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SALES, content.getSales());

        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
    }

    public int updateQuantity(Inventory content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, content.getQuantity());

        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
    }

    public int modifyitemsSalesStats(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int previousQunatity = getQuantity(id);
        int previousSales = gettingSales(id);
        ContentValues values = new ContentValues();
        if(previousQunatity - 1 >= 0) {
            values.put(KEY_QUANTITY, (previousQunatity - 1));
            values.put(KEY_SALES, (previousSales + 1));
            return db.update(TABLE_NAME, values, KEY_ID + " = " + id,
                    null);
        }
        return id;
    }

    public int getQuantity(int id) {

        String selectQuery = "SELECT  quantity FROM " + TABLE_NAME + " WHERE id=" + id + " LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int Quantity = -1;
        if (cursor.moveToFirst()) {
            do {

                Quantity = Integer.parseInt(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return Quantity;
    }

    public int subtarctQuantity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int previousQunatity = getQuantity(id);
        ContentValues values = new ContentValues();
        if(previousQunatity - 1 >=0) {
            values.put(KEY_QUANTITY, (previousQunatity - 1));
            return db.update(TABLE_NAME, values, KEY_ID + " = " + id,
                    null);
        }
        return id;
    }

    public void deleteInventory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = " + id,
                null);
        db.close();
    }
    public int gettingSales(int id) {

        String selectQuery = "SELECT  sales FROM " + TABLE_NAME + " WHERE id=" + id + " LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int sales = -1;
        if (cursor.moveToFirst()) {
            do {

                sales = Integer.parseInt(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return sales;
    }



    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int value = cursor.getCount();
        cursor.close();

        return value;
    }
    public Cursor getId(String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(
                "SELECT id  FROM " + TABLE_NAME + " WHERE name= '" + Name + "'", null);

    }
    public int addQuantity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int previousQunatity = getQuantity(id);
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, (previousQunatity + 1));
        return db.update(TABLE_NAME, values, KEY_ID + " = " + id,
                null);
    }
}
