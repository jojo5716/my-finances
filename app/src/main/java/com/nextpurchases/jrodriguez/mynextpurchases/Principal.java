package com.nextpurchases.jrodriguez.mynextpurchases;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by jrodriguez on 24/12/15.
 */
public class Principal extends AppCompatActivity {
    Button btn_add_salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        btn_add_salary = (Button)findViewById(R.id.btn_go_salary);

        // Add the buttom listener to open
        // a new intent
        btn_add_salary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent add_salary_intent = new Intent(Principal.this, AddSalary.class);
                startActivity(add_salary_intent);
                finish();
            }
        });





    }
}