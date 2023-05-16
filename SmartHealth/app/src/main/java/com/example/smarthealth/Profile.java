package com.example.smarthealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Profile extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, AdapterView.OnItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    int a;
    ListView lv;
    ArrayAdapter adapter;

    String name[]={"Basic Info","Health Info","Medical Conditions","Medicine Reminder","Feedback","Log out"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(Profile.this);

        lv=findViewById(R.id.listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,name);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(Profile.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
//                Toast.makeText(this, "hello1", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(getApplicationContext(), Home.class);
                startActivity(a);
                break;

            case R.id.nutritionist:
//                Toast.makeText(this, "hello2", Toast.LENGTH_SHORT).show();
                Intent b = new Intent(getApplicationContext(), Nutritionist.class);
                startActivity(b);

                break;

            case R.id.diet:
                Intent c = new Intent(getApplicationContext(), Diet.class);
                startActivity(c);
                break;


            case R.id.profile:
                Intent d = new Intent(getApplicationContext(), Profile.class);
                startActivity(d);
                break;

        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        a=i;
        if(a==0) {
            Intent a=new Intent(getApplicationContext(),EditProfile.class);
            startActivity(a);
        }
        else if(a==1){
            Intent b=new Intent(getApplicationContext(),EditHealth.class);
            startActivity(b);
        }
        else if(a==2){
            Intent c=new Intent(getApplicationContext(), SelectMedCond.class);
            startActivity(c);
        }
        else if(a==3){
            Intent d=new Intent(getApplicationContext(),Reminder.class);
            startActivity(d);
        }
        else if(a==4){
            Intent e=new Intent(getApplicationContext(), SendFeedback.class);
            startActivity(e);
        }
        else if (a==5) {
            Intent f=new Intent(getApplicationContext(),Login.class);
            startActivity(f);
        }
    }

}