package com.nextpurchases.jrodriguez.mynextpurchases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jrodriguez on 29/12/15.
 */
public class DB extends SQLiteOpenHelper {
    public String sqlCreateSalary = "CREATE TABLE SalaryBase (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    "year INTEGER, " +
                                                    "month INTEGER, " +
                                                    "salary INTEGER," +
                                                    "current INTEGER)";

    public String sqlCreateBudget = "CREATE TABLE Budget (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                            "name VARCHAR(255), " +
                                                            "repeat_monthly BOOLEAN);";


    public String sqlCreateBudgetUse = "CREATE TABLE BudgetUse(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                "id_budget INTEGER," +
                                                                "year INTEGER," +
                                                                "month INTEGER," +
                                                                "max_amount INTEGER," +
                                                                "spent INTEGER)";

    public String sqlCreateExpenses = "CREATE TABLE Expenses(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                            " year INTEGER, " +
                                                            "month INTEGER, " +
                                                            "id_budget INTEGER, " +
                                                            "created VARCHAR(100), " +
                                                            "spent INTEGER, " +
                                                            "comments TEXT);";



    public DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateSalary);
        db.execSQL(sqlCreateBudget);
        db.execSQL(sqlCreateBudgetUse);
        db.execSQL(sqlCreateExpenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS SalaryBase");
        db.execSQL("DROP TABLE IF EXISTS Budget");
        db.execSQL("DROP TABLE IF EXISTS BudgetUse");
        db.execSQL("DROP TABLE IF EXISTS Expenses");

        db.execSQL(sqlCreateSalary);
        db.execSQL(sqlCreateBudget);
        db.execSQL(sqlCreateBudgetUse);
        db.execSQL(sqlCreateExpenses);

    }
}
