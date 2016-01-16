package com.nextpurchases.jrodriguez.mynextpurchases;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 2/01/16.
 */
public class RegisterSpent extends AppCompatActivity{
    Button btn_save;
    EditText txt_spent;
    EditText txt_comment;
    TextView lb_budget_name;
    ListView lt_expenses;

    private int current_year =  Calendar.getInstance().get(Calendar.YEAR);
    private int current_month =  Calendar.getInstance().get(Calendar.MONTH);
    private String db_name = "DBSalaryBase2";
    private int id_budget;
    private String budget_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_spent);

        txt_spent = (EditText)findViewById(R.id.txt_spent);
        txt_comment = (EditText)findViewById(R.id.txt_comment);
        btn_save = (Button)findViewById(R.id.btn_save);
        lb_budget_name = (TextView)findViewById(R.id.lb_buget_name);
        lt_expenses = (ListView)findViewById(R.id.lt_expenses);

        DB salary_db = new DB(getApplicationContext(), db_name, null, 1);

        final SQLiteDatabase db = salary_db.getWritableDatabase();
        final Bundle parameters = getIntent().getExtras();

        List<String> list_expenses = new ArrayList<>();


        if(parameters != null){
            id_budget = parameters.getInt("id_budget");
            budget_name = parameters.getString("budget_name");

            lb_budget_name.setText(budget_name);

            Cursor c = db.rawQuery("SELECT  spent, created FROM Expenses CROSS JOIN Budget WHERE Expenses.id_budget = Budget._id AND " +
                                                                                                " id_budget=" + id_budget, null);

            if(c.moveToFirst()){
                do{
                    list_expenses.add(c.getString(1) + "  --> -" + c.getString(0) + "€");
                }while (c.moveToNext());
            }

            ArrayAdapter<String> adapter_expenses
                    = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, list_expenses);

            lt_expenses.setAdapter(adapter_expenses);



        }

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parameters != null){



                    int spent = Integer.parseInt(txt_spent.getText().toString());

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    System.out.println(dateFormat.format(date));

                    db.execSQL("INSERT INTO Expenses (year, month, id_budget, created, spent, comments) " +
                                "VALUES (" + current_year + "," + current_month + "," + id_budget + ",'" +
                                dateFormat.format(date) + "'," + spent + ",'" + txt_comment.getText().toString() + "')");
                }

                Intent dashboard = new Intent(RegisterSpent.this, Dashboard.class);
                startActivity(dashboard);
                finish();



            }
        });


    }
}
