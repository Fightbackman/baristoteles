package de.arsartificia.dev.baristoteles;

import android.util.Log;

import java.util.Locale;

public class CoffeeLog {
    public String ID;
    public String Name;
    public float Weight;
    public float Time;
    public String Timestamp;
    public int Rating;
    public String Comment;

    public CoffeeLog(String id, String name, float weight, float time, String timestamp, int rating, String comment) {
        ID = id;
        Name = name;
        Weight = weight;
        Time = time;
        Timestamp = timestamp;
        Rating = rating;
        Comment = comment;
    }

    public String getDetails() {
        return String.format(Locale.ENGLISH, "%.1f g %.1f s", Weight, Time);
    }

    public String getRating() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; ++i) {
            if (i < Rating) {
                sb.append("\u2605");
            } else {
                sb.append("\u2606");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s %.1f g %.1f s %s Rating : %d Comment : %s", Name, Weight, Time, Timestamp, Rating, Comment);
    }
}
