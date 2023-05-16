package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectMedCond extends AppCompatActivity {

    Spinner s;
    Button b;
    String [] medcon={"Daily","Monthly","Yearly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_med_cond);


        s=findViewById(R.id.spinner);
        b=findViewById(R.id.button);

        ArrayAdapter<String> ad=new ArrayAdapter<>(SelectMedCond.this,android.R.layout.simple_list_item_1,medcon);
        s.setAdapter(ad);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ik = new Intent(getApplicationContext(), MedicalCondition.class);
                ik.putExtra("t",s.getSelectedItem().toString());
//                Toast.makeText(SelectMedCond.this, "==="+s.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                startActivity(ik);
            }
        });
    }
}