package de.arsartificia.dev.baristoteles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LogDbHelper ldbHelper;
    AutoCompleteTextView coffeeName;
    TextView espressoText;
    TextView cappuccinoText;
    TextView commentText;
    Switch coffeeTypeSwitch;
    Button saveButton;
    SeekBar seekBarTime;
    SeekBar seekBarWeight;
    SeekBar seekBarGrind;
    TextView textTimeValue;
    TextView textWeightValue;
    TextView textGrindValue;
    RatingBar ratingBar;
    LinearLayout historyLayout;
    String type = "Espresso";
    SQLiteDatabase readDB;
    SQLiteDatabase writeDB;
    ArrayList<CoffeeLog> storedLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        espressoText = (TextView) findViewById(R.id.textEspresso);
        cappuccinoText = (TextView) findViewById(R.id.textCappuccino);
        commentText = (TextView) findViewById(R.id.commentText);
        coffeeTypeSwitch = (Switch) findViewById(R.id.coffeeTypeSwitch);
        saveButton = (Button) findViewById(R.id.saveButton);
        seekBarTime = (SeekBar) findViewById(R.id.seekBarTime);
        seekBarWeight = (SeekBar) findViewById(R.id.seekBarWeight);
        seekBarGrind = (SeekBar) findViewById(R.id.seekBarGrind);
        textTimeValue = (TextView) findViewById(R.id.textTimeValue);
        textWeightValue = (TextView) findViewById(R.id.textWeightValue);
        textGrindValue = (TextView) findViewById(R.id.textGrindValue);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        historyLayout = (LinearLayout) findViewById(R.id.historyLayout);
        setTitle("Welcome to Baristoteles !");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ldbHelper = new LogDbHelper(getApplicationContext());
        readDB = ldbHelper.getReadableDatabase();
        writeDB = ldbHelper.getWritableDatabase();

        fillHistory(true);

        // Disable drawer menu for now
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        setupSeekbars();
        setupSwitch();
        setupSaveButton();

        if (storedLogs.size()>=1) {
            setToValues(storedLogs.get(0));
        }
    }

    public void fillHistory(boolean update) {
        historyLayout.removeAllViews();

        if (update) {
            update();
        }

        for (CoffeeLog log:storedLogs) {
            historyLayout.addView(new HistoryEntry(getApplicationContext(), log, this));
        }
    }

    public void update() {
        storedLogs = readLog();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, getCoffeeNames());
        coffeeName = (AutoCompleteTextView) findViewById(R.id.coffeeName);
        coffeeName.setAdapter(adapter);
    }

    public void setToValues(final CoffeeLog coffeeLog) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                coffeeName.setText(coffeeLog.Name);
                seekBarWeight.setProgress(Math.round(coffeeLog.Weight*10), true);
                seekBarTime.setProgress(Math.round(coffeeLog.Time*10), true);
                seekBarGrind.setProgress(coffeeLog.Grind, true);
                ratingBar.setRating(coffeeLog.Rating);
                //commentText.setText(coffeeLog.Comment);
            }
        }, 500);
    }

    public ArrayList<String> getCoffeeNames() {
        ArrayList<String> coffeeNames = new ArrayList<String>();
        for (CoffeeLog log : storedLogs) {
            coffeeNames.add(log.Name);
        }
        return coffeeNames;
    }

    public ArrayList<CoffeeLog> readLog() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                LogContract.LogEntry._ID,
                LogContract.LogEntry.COLUMN_NAME_TITLE,
                LogContract.LogEntry.COLUMN_TYPE_TITLE,
                LogContract.LogEntry.COLUMN_WEIGHT_TITLE,
                LogContract.LogEntry.COLUMN_TIME_TITLE,
                LogContract.LogEntry.COLUMN_GRIND_TITLE,
                LogContract.LogEntry.COLUMN_TIMESTAMP_TITLE,
                LogContract.LogEntry.COLUMN_RATING_TITLE,
                LogContract.LogEntry.COLUMN_COMMENT_TITLE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                LogContract.LogEntry._ID + " DESC";

        Cursor cursor = readDB.query(
                LogContract.LogEntry.TABLE_NAME,                     // The table to query
                projection,                                          // The columns to return
                null,                                                // no WHERE
                null,                                                // no WHERE args
                null,                                                // don't group the rows
                null,                                                // don't filter by row groups
                sortOrder                                            // The sort order
        );

        ArrayList<CoffeeLog> entries = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            float weight = cursor.getFloat(3);
            float time = cursor.getFloat(4);
            int grind = cursor.getInt(5);
            String timestamp = cursor.getString(6);
            int rating = cursor.getInt(7);
            String comment = cursor.getString(8);
            entries.add(new CoffeeLog(id, name, type, weight, time, grind, timestamp, rating, comment));
        }
        cursor.close();
        return entries;
    }

    public void setupSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = coffeeName.getText().toString();
                Float weight = Float.valueOf(Util.removeLastChar(textWeightValue.getText().toString()));
                Float time = Float.valueOf(Util.removeLastChar(textTimeValue.getText().toString()));
                int grind = Integer.valueOf(textGrindValue.getText().toString());
                int rating = Math.round(ratingBar.getRating());
                String comment = commentText.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.GERMAN);
                String timestamp = simpleDateFormat.format(new Date());
                Log.d("save", String.format(Locale.ENGLISH, "%s %s %s %d %s %s", name, weight, time, rating, comment, timestamp));
                ContentValues values = new ContentValues();
                values.put(LogContract.LogEntry.COLUMN_NAME_TITLE, name);
                values.put(LogContract.LogEntry.COLUMN_WEIGHT_TITLE, weight);
                values.put(LogContract.LogEntry.COLUMN_TIME_TITLE, time);
                values.put(LogContract.LogEntry.COLUMN_GRIND_TITLE, grind);
                values.put(LogContract.LogEntry.COLUMN_TIMESTAMP_TITLE, timestamp);
                values.put(LogContract.LogEntry.COLUMN_RATING_TITLE, rating);
                values.put(LogContract.LogEntry.COLUMN_COMMENT_TITLE, comment);
                writeDB.insert(LogContract.LogEntry.TABLE_NAME, null, values);
                fillHistory(true);
            }
        });
    }

    public void setupSwitch() {
        coffeeTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if (on) {
                    espressoText.setTextSize(16);
                    cappuccinoText.setTextSize(20);
                    cappuccinoText.setTypeface(Typeface.DEFAULT_BOLD);
                    espressoText.setTypeface(Typeface.DEFAULT);
                    type="Espresso";
                } else {
                    espressoText.setTextSize(20);
                    cappuccinoText.setTextSize(16);
                    cappuccinoText.setTypeface(Typeface.DEFAULT);
                    espressoText.setTypeface(Typeface.DEFAULT_BOLD);
                    type="Cappuccino";
                }
            }
        });
    }

    public void setupSeekbars() {
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textTimeValue.setText(String.format(Locale.ENGLISH, "%d s", i/10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textWeightValue.setText(String.format(Locale.ENGLISH, "%.1f g", i/10.));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarGrind.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textGrindValue.setText(String.format(Locale.ENGLISH, "%d", i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        ldbHelper.close();
        super.onDestroy();
    }
}
