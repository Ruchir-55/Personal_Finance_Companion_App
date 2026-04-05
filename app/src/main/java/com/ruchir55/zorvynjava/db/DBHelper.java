package com.ruchir55.zorvynjava.db;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

import com.ruchir55.zorvynjava.model.Transaction;
import java.util.*;
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "ExpenseDB", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE transactions(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "amount REAL," +
                "type TEXT," +
                "timestamp LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {}

    public void insertData(String title, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("amount", amount);
        cv.put("type", type);
        cv.put("timestamp", System.currentTimeMillis());
        db.insert("transactions", null, cv);
    }

    //Edit/Delete
    public void updateTransaction(int id, String title, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("amount", amount);
        cv.put("type", type);

        db.update("transactions", cv, "id=?", new String[]{String.valueOf(id)});
    }

    //Edit/Delete
    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("transactions", "id=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Transaction> getAllData() {
        ArrayList<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions ORDER BY timestamp DESC", null);

        while (cursor.moveToNext()) {
            list.add(new Transaction(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getLong(4)
            ));
        }
        cursor.close();
        return list;
    }

    public double getBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions", null);

        double balance = 0;

        while (cursor.moveToNext()) {
            String type = cursor.getString(3); // type column
            double amount = cursor.getDouble(2); // amount column

            if (type.equals("Income")) balance += amount;
            else balance -= amount;
        }

        cursor.close();
        return balance;
    }
}
