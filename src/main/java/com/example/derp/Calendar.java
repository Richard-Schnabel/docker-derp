package com.example.derp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

public class Calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_calendar);

        //získej kde byl Calendar vyvolán
        Intent incomingIntent = getIntent();
        String odkud = incomingIntent.getStringExtra("odkud");
        String id = incomingIntent.getStringExtra("id");

        //najdi kalendář
        CalendarView kalendar = findViewById(R.id.calendarViewInput);

        //po kliknutí na datum
        kalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {

                //zformátuj datum
                String date = d + "/" + (m + 1) + "/" + y;

                //vrať se zpět odkud is přišel a datum si pamatuj datum
                Intent intent;
                if (Objects.equals(odkud, "edit")) {
                    intent = new Intent(Calendar.this, Edit.class);
                    intent.putExtra("id", id);
                } else
                {
                    intent = new Intent(Calendar.this, AddNew.class);
                }
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }
}
