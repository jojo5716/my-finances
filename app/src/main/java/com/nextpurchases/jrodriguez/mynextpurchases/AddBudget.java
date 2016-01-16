package com.nextpurchases.jrodriguez.mynextpurchases;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Calendar;

/*
 * Created by root on 2/01/16.
 */
public class AddBudget extends AppCompatActivity{
    Button btn_save;
    EditText txt_name;
    EditText txt_max_amount;
    CheckBox cb_repeat;

    private String db_name = "DBSalaryBase2";
    private int current_year =  Calendar.getInstance().get(Calendar.YEAR);
    private int current_month =  Calendar.getInstance().get(Calendar.MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbudget);

        txt_name = (EditText)findViewById(R.id.txt_name);
        txt_max_amount = (EditText)findViewById(R.id.txt_max_amount);
        cb_repeat = (CheckBox)findViewById(R.id.cb_repeat);
        btn_save = (Button)findViewById(R.id.btn_save);

        final Bundle parameters = getIntent().getExtras();

        if(parameters != null){
            txt_name.setText(parameters.getString("budget_name"));
            txt_max_amount.setText(parameters.getString("budget_maxamount"));
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DB salary_db = new DB(getApplicationContext(), db_name, null, 1);
                SQLiteDatabase db = salary_db.getWritableDatabase();



                if(db != null){
                    ContentValues vals = new ContentValues();
                    String budget_name = txt_name.getText().toString();
                    Integer max_amount = Integer.parseInt(txt_max_amount.getText().toString());

                    vals.put("name", budget_name);
                    vals.put("repeat_monthly", cb_repeat.isChecked());

                    String sql_second = "";
                    String sql;
                    long id_budget;


                    if(parameters != null){
                        System.out.println("?????????????????");

                        id_budget = parameters.getLong("id_budget");
                        sql = "UPDATE Budget SET name='"+ budget_name +"' WHERE _id=" + String.valueOf(id_budget);
                        sql_second = "UPDATE BudgetUse SET max_amount=" + max_amount + " WHERE _id=" + id_budget;

                    }else{
                        id_budget = db.insert("Budget", null, vals);

                        sql = "INSERT INTO BudgetUse (id_budget, year, month, max_amount, spent) " +
                                 "VALUES (" + id_budget + "," + current_year + "," + current_month + "," + max_amount + "," + 0 +  ")";
                    }


                    db.execSQL(sql);

                    if(sql_second != ""){
                        db.execSQL(sql_second);

                    }
                    db.close();

                    Intent dashboard = new Intent(AddBudget.this, Dashboard.class);
                    startActivity(dashboard);
                    finish();




                }
            }
        });

    }
}
