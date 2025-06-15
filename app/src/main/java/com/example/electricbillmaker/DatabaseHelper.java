package com.example.electricbillmaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "ElectricityBillDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bills (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT, units INTEGER, total REAL, rebate REAL, final REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS bills");
        onCreate(db);
    }

    public void insertBill(String month, int units, double total, int rebate, double finalCost) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month", month);
        values.put("units", units);
        values.put("total", total);
        values.put("rebate", rebate);
        values.put("final", finalCost);
        db.insert("bills", null, values);
    }

    public void deleteBill(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("bills", "id = ?", new String[]{String.valueOf(id)});
    }


    public Cursor getAllBills() {
        return getReadableDatabase().rawQuery("SELECT * FROM bills", null);
    }
}
