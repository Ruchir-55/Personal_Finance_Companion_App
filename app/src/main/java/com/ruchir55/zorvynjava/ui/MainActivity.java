package com.ruchir55.zorvynjava.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ruchir55.zorvynjava.R;
import com.ruchir55.zorvynjava.model.Transaction;
import com.ruchir55.zorvynjava.databinding.ActivityMainBinding;
import com.ruchir55.zorvynjava.db.DBHelper;
import com.ruchir55.zorvynjava.model.TransactionAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHelper db;
    ActivityMainBinding mainBinding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.addBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AddTransactionActivity.class)));

        mainBinding.insightBtn.setOnClickListener(v ->
                startActivity(new Intent(this, InsightsActivity.class)));

        db = new DBHelper(this);

        mainBinding.addBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AddTransactionActivity.class)));

        mainBinding.insightBtn.setOnClickListener(v ->
                startActivity(new Intent(this, InsightsActivity.class)));

        mainBinding.toolbar.setOnMenuItemClickListener(item -> {

            if(item.getItemId() == R.id.settingsItem) {
                Intent intent = new Intent(MainActivity.this, ChangeThemeActivity.class);
                startActivity(intent);
                return true;
            }
            else {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        boolean isDarkMode = sharedPreferences.getBoolean("switch", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }*/

        ArrayList<Transaction> list = db.getAllData();
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        //Edit/Delete
        TransactionAdapter adapter = new TransactionAdapter(this, list);
        mainBinding.listView.setAdapter(adapter);

        double total = 0;

        for (Transaction t : list) {
            //adapter.add(t.getTitle() + " - ₹" + t.getAmount() + " (" + t.getType() + ")");
            if (t.getType().equals("Income")) total += t.getAmount();
            else total -= t.getAmount();
        }

        //mainBinding.listView.setAdapter(adapter);
        mainBinding.totalText.setText("Balance: ₹" + total);
    }
}