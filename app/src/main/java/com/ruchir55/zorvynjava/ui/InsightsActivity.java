package com.ruchir55.zorvynjava.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.ruchir55.zorvynjava.R;
import com.ruchir55.zorvynjava.model.Transaction;
import com.ruchir55.zorvynjava.databinding.ActivityInsightsBinding;
import com.ruchir55.zorvynjava.db.DBHelper;

import java.util.ArrayList;
import androidx.core.content.ContextCompat;

public class InsightsActivity extends AppCompatActivity {
    ActivityInsightsBinding insightsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insightsBinding = ActivityInsightsBinding.inflate(getLayoutInflater());
        setContentView(insightsBinding.getRoot());

        DBHelper db = new DBHelper(this);
        ArrayList<Transaction> list = db.getAllData();

        double income = 0, expense = 0;

        for (Transaction t : list) {
            if (t.getType().equals("Income")) income += t.getAmount();
            else expense += t.getAmount();
        }

        float savings = (float) (income - expense);

        insightsBinding.incomeText.setText("Total Income: ₹" + income);
        insightsBinding.expenseText.setText("Total Expense: ₹" + expense);
        insightsBinding.balanceText.setText("Balance: ₹" + (savings));

        //Piechart for Income v/s Expense
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) income, "Income"));
        entries.add(new PieEntry((float) expense, "Expense"));
        entries.add(new PieEntry((float) savings, "Savings"));

        PieDataSet dataSet = new PieDataSet(entries, "Finance Summary");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.incomeGreen));
        colors.add(ContextCompat.getColor(this, R.color.expenseRed));
        colors.add(ContextCompat.getColor(this, R.color.savingsBlue));
        dataSet.setColors(colors);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        insightsBinding.pieChart.setData(data);

        insightsBinding.pieChart.invalidate();

        insightsBinding.toolbar.setOnMenuItemClickListener(item -> {

            if(item.getItemId() == R.id.settingsItem) {
                Intent intent = new Intent(InsightsActivity.this, ChangeThemeActivity.class);
                startActivity(intent);
                return true;
            }
            else {
                return false;
            }
        });
    }
}