package de.arsartificia.dev.baristoteles;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryEntry extends RelativeLayout {
    private CoffeeLog coffeeLog;

    public HistoryEntry(Context context, CoffeeLog coffeeLog) {
        super(context);

        LayoutInflater.from(getContext()).inflate(R.layout.history_entry, this);
        this.coffeeLog = coffeeLog;

        TextView coffeeNameTextView = (TextView) findViewById(R.id.coffeeNameTextView);
        coffeeNameTextView.setText(coffeeLog.Name);
        TextView detailTextView = (TextView) findViewById(R.id.detailTextView);
        detailTextView.setText(coffeeLog.getDetails());
        TextView ratingTextView = (TextView) findViewById(R.id.ratingTextView);
        ratingTextView.setText(coffeeLog.getRating());
    }
}
