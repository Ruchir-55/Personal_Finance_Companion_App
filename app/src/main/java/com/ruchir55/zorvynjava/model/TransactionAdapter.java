package com.ruchir55.zorvynjava.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ruchir55.zorvynjava.R;
import com.ruchir55.zorvynjava.db.DBHelper;
import com.ruchir55.zorvynjava.ui.AddTransactionActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TransactionAdapter extends BaseAdapter {

    Context context;
    ArrayList<Transaction> list;
    DBHelper db;

    public TransactionAdapter(Context context, ArrayList<Transaction> list) {
        this.context = context;
        this.list = list;
        db = new DBHelper(context);
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int i) { return list.get(i); }

    @Override
    public long getItemId(int i) { return list.get(i).getId(); }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        }

        TextView title = view.findViewById(R.id.titleText);
        TextView amount = view.findViewById(R.id.amountText);
        Button editBtn = view.findViewById(R.id.editBtn);
        Button deleteBtn = view.findViewById(R.id.deleteBtn);
        TextView timeText = view.findViewById(R.id.timeText);

        Transaction t = list.get(position);

        //Simple Timestamp Code
        /*SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        String formattedDate = sdf.format(new Date(t.getTimestamp()));
        timeText.setText(formattedDate);*/

        //Timestamp Code with Today/Yesterday filter
        long timestamp = t.getTimestamp();

        Calendar now = Calendar.getInstance();
        Calendar txTime = Calendar.getInstance();
        txTime.setTimeInMillis(timestamp);

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat fullFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

        String displayText;

        if (now.get(Calendar.YEAR) == txTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == txTime.get(Calendar.DAY_OF_YEAR)) {

            // TODAY
            displayText = "Today, " + timeFormat.format(new Date(timestamp));

        } else if (now.get(Calendar.YEAR) == txTime.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - txTime.get(Calendar.DAY_OF_YEAR) == 1) {

            // YESTERDAY
            displayText = "Yesterday, " + timeFormat.format(new Date(timestamp));

        } else {
            // FULL DATE
            displayText = fullFormat.format(new Date(timestamp));
        }

        //Piechart
        if (t.getType().equals("Income")) {
            amount.setTextColor(Color.GREEN);
        } else {
            amount.setTextColor(Color.RED);
        }

        timeText.setText(displayText);

        title.setText(t.getTitle());
        amount.setText("₹" + t.getAmount() + " (" + t.getType() + ")");

        //  DELETE
        deleteBtn.setOnClickListener(v -> {
            db.deleteTransaction(t.getId());
            list.remove(position);
            notifyDataSetChanged();
        });

        //  EDIT
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddTransactionActivity.class);
            intent.putExtra("id", t.getId());
            intent.putExtra("title", t.getTitle());
            intent.putExtra("amount", t.getAmount());
            intent.putExtra("type", t.getType());
            context.startActivity(intent);
        });

        return view;
    }
}