package com.nextpurchases.jrodriguez.mynextpurchases;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;


/**
 * Created by jrodriguez on 29/12/15.
 */
public class AddSalary extends AppCompatActivity {
    Button btn_add_salary;
    EditText txt_salary;

    private String db_name = "DBSalaryBase2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_salary);

        btn_add_salary = (Button)findViewById(R.id.btn_add_salary);
        txt_salary = (EditText)findViewById(R.id.txt_salary);


        btn_add_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year =  Calendar.getInstance().get(Calendar.YEAR);
                int salary = Integer.parseInt(txt_salary.getText().toString());
                int month =  Calendar.getInstance().get(Calendar.MONTH);

                DB salary_db = new DB(getApplicationContext(), db_name, null, 1);
                SQLiteDatabase db = salary_db.getWritableDatabase();

                if (db != null) {
                    db.execSQL("INSERT INTO SalaryBase (year, month, salary, current) " +
                            "VALUES (" + year + "," + month + "," + salary + "," + salary + ")");
                }
                db.close();

                Intent dashboard = new Intent(AddSalary.this, Dashboard.class);
                startActivity(dashboard);
                finish();


            }
        });



    }
}
