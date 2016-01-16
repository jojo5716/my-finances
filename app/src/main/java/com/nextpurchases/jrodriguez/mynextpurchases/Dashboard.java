package com.nextpurchases.jrodriguez.mynextpurchases;

import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by root on 1/01/16.
 */
public class Dashboard extends AppCompatActivity {

    TextView lb_salary;
    TextView lb_current;
    TextView lb_current_date;
    TextView lb_total_spent;
    TextView lb_total_spent_percent;
    ListView list_months;
    ListView list_data_current_month;
    Button btn_add_budget;


    private int current_year =  Calendar.getInstance().get(Calendar.YEAR);
    private int current_month =  Calendar.getInstance().get(Calendar.MONTH);
    private int total_spent = 0;
    private double total_budget = 0.0;
    private double salary = 0.0;

    private String db_name = "DBSalaryBase2";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_salary);

        lb_salary = (TextView)findViewById(R.id.lb_salary);
        lb_current = (TextView)findViewById(R.id.lb_current);
        lb_current_date = (TextView)findViewById(R.id.current_date);
        lb_total_spent = (TextView)findViewById(R.id.lb_total_spent);
        lb_total_spent_percent = (TextView)findViewById(R.id.lb_total_spent_percent);
        list_months = (ListView)findViewById(R.id.listView);
        list_data_current_month = (ListView)findViewById(R.id.data_current_month);
        btn_add_budget = (Button)findViewById(R.id.btn_add_budget);



        TabHost tabs_host =(TabHost)findViewById(R.id.tabHost);
        tabs_host.setup();

        TabHost.TabSpec tab1 = tabs_host.newTabSpec("tab1");
        TabHost.TabSpec tab2 = tabs_host.newTabSpec("tab1");
        TabHost.TabSpec tab3 = tabs_host.newTabSpec("tab1");

        tab1.setIndicator("Meses anteriores");
        tab1.setContent(R.id.linearLayout);

        tab2.setIndicator(getMonthYearName());
                tab2.setContent(R.id.linearLayout2);

        tabs_host.addTab(tab1);
        tabs_host.addTab(tab2);


        Initial();


    } // END onCreate

    public String getMonthYearName(){

        return getMonthName(current_month) + " " + current_year;
    }

    public void Initial(){

        DB salary_db = new DB(getApplicationContext(), db_name, null, 1);
        final SQLiteDatabase  db = salary_db.getWritableDatabase();

        if(db != null){
            Cursor c = db.rawQuery("SELECT * FROM SalaryBase WHERE year=" + String.valueOf(current_year) + " AND month=" + String.valueOf(current_month), null);
            if(c.moveToFirst()){
                // Save the salary for calculate the spent percent.
                salary = Double.parseDouble(c.getString(3));

                lb_salary.setText(c.getString(3) + "€");
                lb_current.setText(c.getString(4) + "€");
            }

            // Get before months.
            List<String> values = getMonths();

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            list_months.setAdapter(adapter);

            // END Get before months.

            // Get Actual budget list
            List<Budget> budgets = getBudgets();

            // Set total spent label
            lb_total_spent.setText(String.valueOf(total_budget) + "€");
            DecimalFormat f = new DecimalFormat("##.00");
            lb_total_spent_percent.setText(f.format((total_budget * 100)/ salary) + "%");

            lb_current.setText(String.valueOf(salary - total_spent) + "€");

            final ArrayAdapter<Budget> adapter_budgets = new ArrayAdapter<Budget>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, budgets);

            list_data_current_month.setAdapter(adapter_budgets);

            list_data_current_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = ((TextView) view).getText().toString();

                    Budget budget_item = (Budget) list_data_current_month.getAdapter().getItem(position);

                    if (item.equals(getApplicationContext().getResources().getString(R.string.txt_dont_have_budget))) {
                        Intent add_budget = new Intent(Dashboard.this, AddBudget.class);
                        startActivity(add_budget);
                        finish();
                    } else {
                        // If click on te budget element, we have to open a intent for
                        // register a spent.
                        Intent register_spend = new Intent(Dashboard.this, RegisterSpent.class);
                        register_spend.putExtra("id_budget", budget_item.getId());
                        register_spend.putExtra("budget_name", budget_item.getName());

                        startActivity(register_spend);
                        Toast.makeText(getBaseContext(), "Register a spend for: " + budget_item.getName(), Toast.LENGTH_LONG).show();
                    }


                }


            }); // END setOnClickListener

            list_data_current_month.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                    final Budget budget_item = (Budget) list_data_current_month.getAdapter().getItem(position);
                    final Integer position_item = position;

                    final Dialog dialog = new Dialog(Dashboard.this);
                    dialog.setTitle("Options (" + budget_item.getName() + ")");
                    dialog.setContentView(R.layout.menu);
                    dialog.show();

                    final Button btn_edit = (Button)dialog.findViewById(R.id.btn_edit);
                    btn_edit.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Intent add_budget = new Intent(Dashboard.this, AddBudget.class);
                            add_budget.putExtra("budget_id", budget_item.getId());
                            add_budget.putExtra("budget_name", budget_item.getName());
                            add_budget.putExtra("budget_maxamount", budget_item.getMax_amount());

                            startActivity(add_budget);
                        }
                    });

                    final Button btn_delete = (Button)dialog.findViewById(R.id.btn_delete);
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (db!=null){
                                String SQL = "DELETE FROM BudgetUse WHERE _id=" + budget_item.getId();
                                db.execSQL(SQL);

                                adapter_budgets.remove(adapter_budgets.getItem(position_item));
                                adapter_budgets.setNotifyOnChange(true);

                                dialog.hide();
                            }
                        }
                    });




                    return true;
                }
            });
            //END Get Actual budget list;

        }

        //Get current month name.
        lb_current_date.setText("Current date: " + getMonthYearName());

        // End Get current month name;

        btn_add_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_budget = new Intent(Dashboard.this, AddBudget.class);
                startActivity(add_budget);

            }
        });
    }

    public List<Budget>  getBudgets(){
        List<Budget> budgets = new ArrayList<Budget>();

        DB salary_db = new DB(getApplicationContext(), db_name, null, 1);
        SQLiteDatabase db = salary_db.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT  BudgetUse.id_budget, Budget.name, BudgetUse.max_amount, SUM(Expenses.spent) FROM BudgetUse " +
                                "LEFT JOIN Expenses ON BudgetUse.id_budget = Expenses.id_budget " +
                                "INNER JOIN Budget ON Budget._id = BudgetUse.id_budget " +
                                "WHERE BudgetUse.year = 2016 AND BudgetUse.month = 0 " +
                                "GROUP BY BudgetUse.id_budget", null);
        if (c.moveToFirst()){
            do {
                Integer id_budget = c.getInt(0);
                String name = c.getString(1);
                Double max_amount = c.getDouble(2);
                Integer spent = c.getInt(3);

                total_budget += max_amount;
                total_spent += spent;

                Budget b = new Budget(id_budget, name, max_amount, spent);

                budgets.add(b);

            }while (c.moveToNext());
        }

        if (budgets.size() == 0){
            budgets.add(new Budget());
        }



        return budgets;
    }
    public List<String> getMonths(){
        // This method gives us a list of previous months
        List<String> months_list = new ArrayList<String>();

        DB salary_db = new DB(getApplicationContext(), db_name, null, 1);
        SQLiteDatabase db = salary_db.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM SalaryBase", null);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Integer year= c.getInt(1);
                Integer month = c.getInt(2);
                Float salary = c.getFloat(3);
                Float current_salary = c.getFloat(4);

                if(year !=  current_year&& month != current_month){
                    String month_name = getMonthName(month);

                    months_list.add(month_name + "(" + year + ")" + ": " + current_salary + " of " + salary);
                }
            } while(c.moveToNext());
        }

        if(months_list.size() == 0){
            months_list.add("No data.");
        }

        return months_list;
    }


    public String getMonthName(int month){
        String name = "Wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        if(month >= 0 && month<=11){
            name = months[month];
        }
        return name;
    }




}
