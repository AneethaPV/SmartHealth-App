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

public class Diet extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    BottomNavigationView bottomNavigationView;

    int a;
    ListView lv;
    ArrayAdapter adapter;

    String name[]={"Food Calorie","Diet Plan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.diet);
        bottomNavigationView.setOnNavigationItemSelectedListener(Diet.this);

        lv=findViewById(R.id.listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,name);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(Diet.this);

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
//                Toast.makeText(this, "hello3", Toast.LENGTH_SHORT).show();

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
            Intent a=new Intent(getApplicationContext(),FoodCalorie.class);
            startActivity(a);
        }
        else if(a==1){
            Intent b=new Intent(getApplicationContext(),DietPlan.class);
            startActivity(b);
        }
    }
}