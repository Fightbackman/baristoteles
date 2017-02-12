package de.arsartificia.dev.baristoteles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryEntry extends LinearLayout {
    private CoffeeLog coffeeLog;
    private MainActivity mainActivity;

    public HistoryEntry(Context context, final CoffeeLog coffeeLog, final MainActivity mainActivity) {
        super(context);

        LayoutInflater.from(getContext()).inflate(R.layout.history_entry, this);
        this.coffeeLog = coffeeLog;
        this.mainActivity = mainActivity;

        TextView coffeeNameTextView = (TextView) findViewById(R.id.coffeeNameTextView);
        coffeeNameTextView.setText(coffeeLog.Name);
        TextView detailTextView = (TextView) findViewById(R.id.detailTextView);
        detailTextView.setText(coffeeLog.getDetails());
        TextView ratingTextView = (TextView) findViewById(R.id.ratingTextView);
        ratingTextView.setText(coffeeLog.getRating());
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String selection = LogContract.LogEntry._ID + " LIKE ?";
                String[] selectionArgs = { coffeeLog.ID };
                mainActivity.writeDB.delete(LogContract.LogEntry.TABLE_NAME, selection, selectionArgs);
                mainActivity.fillHistory(true);
            }
        });
    }
}
