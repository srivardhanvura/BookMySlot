package com.itt.bookmyslot.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itt.bookmyslot.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ValidateActivity extends AppCompatActivity {

    Button submit;
    RadioGroup rg1, rg2, rg3;
    CalendarView cal;
    TimePicker time;
    String dateSel, timeSel;
    int dateSelected=0, timeSelected=0;
    int dateActual, timeActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        ArrayList<String> selected = getIntent().getStringArrayListExtra("selected");
        final String reg = getIntent().getStringExtra("regno");
        String pass = getIntent().getStringExtra("pass");
        final ScrollView scrollView = findViewById(R.id.validateRoot);

        submit = findViewById(R.id.validate_btn);
        rg1 = findViewById(R.id.rg_1);
        rg2 = findViewById(R.id.rg_2);
        rg3 = findViewById(R.id.rg_3);
        cal = findViewById(R.id.calendar);
        time = findViewById(R.id.time);

        getSupportActionBar().setTitle("Select slot");

        final Calendar calActual = Calendar.getInstance();

//        dateSelected = calActual.get(Calendar.YEAR) * 10000 + calActual.get(Calendar.MONTH) * 100 + calActual.get(Calendar.DATE);
//        timeSelected = calActual.get(Calendar.HOUR_OF_DAY) * 100 + calActual.get(Calendar.MINUTE) + 1;

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
                dateSelected = year * 10000 + (month + 1) * 100 + date;
                dateSel = "" + date + "/" + (month + 1) + "/" + year;
            }
        });

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hours, int mins) {
                timeSelected = hours * 100 + mins;
                timeSel = "" + hours + ":" + mins;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateActual = calActual.get(Calendar.YEAR) * 10000 + (calActual.get(Calendar.MONTH)+1) * 100 + calActual.get(Calendar.DATE);
                timeActual = calActual.get(Calendar.HOUR_OF_DAY) * 100 + calActual.get(Calendar.MINUTE);
                int select1 = rg1.getCheckedRadioButtonId();
                int select2 = rg2.getCheckedRadioButtonId();
                int select3 = rg3.getCheckedRadioButtonId();

                System.out.println("Data is "+dateSelected+timeSelected);
                if (dateSelected == 0){
                    dateSelected = dateActual;
                    dateSel = "" + calActual.get(Calendar.DATE) + "/" + (calActual.get(Calendar.MONTH)+1) + "/" + calActual.get(Calendar.YEAR);
                }
                if (timeSelected == 0){
                    timeSelected = timeActual;
                    timeSel = "" + calActual.get(Calendar.HOUR_OF_DAY)+ ":" + calActual.get(Calendar.MINUTE);
                }
                System.out.println("Data is "+dateSelected+timeSelected);
                System.out.println("Data is "+dateSel+timeSel);

                if (select1 == -1 || select2 == -1 || select3 == -1)
                    Snackbar.make(scrollView, "Select all the fields", Snackbar.LENGTH_SHORT).show();

                else if (dateSelected < dateActual) {
                    Snackbar.make(scrollView, "Select a date in the future", Snackbar.LENGTH_SHORT).show();
                } else if (dateSelected == dateActual && timeSelected <= timeActual) {
                    Snackbar.make(scrollView, "Select a date in the future", Snackbar.LENGTH_SHORT).show();
                } else {
                    RadioButton rb1 = findViewById(select1);
                    RadioButton rb2 = findViewById(select2);
                    RadioButton rb3 = findViewById(select3);

                    int count = 0;
                    if (rb1.getText().equals("No"))
                        count++;
                    if (rb2.getText().equals("No"))
                        count++;
                    if (rb3.getText().equals("No"))
                        count++;

                    if (count >= 2) {
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child("prev_bookings").child(reg).push().child("date").setValue(dateSel + ":" + timeSel);
                        Toast.makeText(ValidateActivity.this, "Booking done", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ValidateActivity.this, "Booking cancelled. It is highly recommended that you stay isolated.", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(ValidateActivity.this, HomeActivity.class));
                }
            }
        });
    }
}