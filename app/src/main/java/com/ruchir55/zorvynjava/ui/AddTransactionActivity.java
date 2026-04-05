package com.ruchir55.zorvynjava.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ruchir55.zorvynjava.R;
import com.ruchir55.zorvynjava.databinding.ActivityAddTransactionBinding;
import com.ruchir55.zorvynjava.db.DBHelper;

public class AddTransactionActivity extends AppCompatActivity {
    DBHelper db;
    ActivityAddTransactionBinding transactionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionBinding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(transactionBinding.getRoot());

        db = new DBHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Income", "Expense"}
        );
        transactionBinding.typeSpinner.setAdapter(adapter);

        int id = getIntent().getIntExtra("id", -1);
        //Edit/Delete
        if (id != -1) {
            transactionBinding.title.setText(getIntent().getStringExtra("title"));
            transactionBinding.amount.setText(String.valueOf(getIntent().getDoubleExtra("amount", 0)));

            String type = getIntent().getStringExtra("type");
            if (type.equals("Income")) transactionBinding.typeSpinner.setSelection(0);
            else transactionBinding.typeSpinner.setSelection(1);
        }

        transactionBinding.saveBtn.setOnClickListener(v -> {

            //Edit/Delete
            String title = transactionBinding.title.getText().toString();
            String amountStr = transactionBinding.amount.getText().toString();
            String type = transactionBinding.typeSpinner.getSelectedItem().toString();

            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double balance = db.getBalance();

            //  HANDLE EDIT CASE
            if (id != -1) {
                String oldType = getIntent().getStringExtra("type");
                double oldAmount = getIntent().getDoubleExtra("amount", 0);

                // Remove old transaction effect from balance
                if (oldType.equals("Income")) balance -= oldAmount;
                else balance += oldAmount;
            }

            //  VALIDATION
            if (type.equals("Expense") && amount > balance) {
                Toast.makeText(this, "Insufficient Balance! Available: ₹" + balance, Toast.LENGTH_SHORT).show();
                return;
            }

            if (id == -1) {
                db.insertData(title, amount, type);
            } else {
                db.updateTransaction(id, title, amount, type);
            }
            finish();
        });

        transactionBinding.toolbar.setOnMenuItemClickListener(item -> {

            if(item.getItemId() == R.id.settingsItem) {
                Intent intent = new Intent(AddTransactionActivity.this, ChangeThemeActivity.class);
                startActivity(intent);
                return true;
            }
            else {
                return false;
            }
        });
    }
}