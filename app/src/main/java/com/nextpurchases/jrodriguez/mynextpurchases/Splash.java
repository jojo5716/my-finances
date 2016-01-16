package com.nextpurchases.jrodriguez.mynextpurchases;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Calendar;

public class Splash extends AppCompatActivity {

    public static final int seconds = 1;
    public static final int miliseconds_finish = seconds * 1000;
    public static final int DELAY = 2;

    private ProgressBar progressbar;

    private String db_name = "DBSalaryBase2";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressbar = (ProgressBar)findViewById(R.id.progressBar);
        progressbar.setMax(MaxProgressBar());

        StartAnimation();
    }

    public void StartAnimation() {

        new CountDownTimer(miliseconds_finish, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                progressbar.setProgress(SetProgressbar(millisUntilFinished));
            }

            @Override
            public void onFinish() {

                DB salary_db = new DB(getApplicationContext(), db_name, null, 1);
                SQLiteDatabase db = salary_db.getWritableDatabase();
                Intent principal = new Intent(Splash.this, AddSalary.class);

                if(db != null){

                    int year =  Calendar.getInstance().get(Calendar.YEAR);
                    int month =  Calendar.getInstance().get(Calendar.MONTH);

                    Cursor c = db.rawQuery("SELECT * FROM SalaryBase WHERE year=" + String.valueOf(year) + " AND month=" + String.valueOf(month), null);
                    if(c.moveToFirst()){
                        principal = new Intent(Splash.this, Dashboard.class);
                    }

                }

                startActivity(principal);
                finish();
            }
        }.start();
    }

    public int SetProgressbar(long milliseg){
        return (int)((miliseconds_finish - milliseg) / 100);
    }

    public int MaxProgressBar(){
        return seconds - DELAY;
    }

}
