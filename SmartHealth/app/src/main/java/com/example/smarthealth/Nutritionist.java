package com.example.smarthealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Nutritionist extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    BottomNavigationView bottomNavigationView;
    ArrayList<String> name,image,email,mobile,gender,qualification,experience,license,nid;
    String url;
    SharedPreferences sh;
    SearchView sv;
    ListView l1;
    String name1;

    public static boolean[] chk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritionist);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nutritionist);
        bottomNavigationView.setOnNavigationItemSelectedListener(Nutritionist.this);

        l1=findViewById(R.id.lv1);
        sv=findViewById(R.id.search_bar);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url = "http://" + sh.getString("ip", "") + ":5000/nutritionistdata";
//        aprtmnttyp.setOnItemSelectedListener(Addbook.this);
        RequestQueue queue = Volley.newRequestQueue(Nutritionist.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    name=new ArrayList<>(ar.length());
                    image = new ArrayList<>(ar.length());
                    email = new ArrayList<>(ar.length());
                    mobile = new ArrayList<>(ar.length());
                    gender=new ArrayList<>(ar.length());
                    qualification = new ArrayList<>(ar.length());
                    experience = new ArrayList<>(ar.length());
                    license = new ArrayList<>(ar.length());
                    nid = new ArrayList<>(ar.length());


                    for (int i = 0; i < ar.length(); i++) {

                        JSONObject jo = ar.getJSONObject(i);
                        name.add(jo.getString("name"));
                        image.add(jo.getString("image"));
                        email.add(jo.getString("email"));
                        mobile.add(jo.getString("mobile"));
                        gender.add(jo.getString("gender"));
                        qualification.add(jo.getString("qualification"));
                        experience.add(jo.getString("experience"));
                        license.add(jo.getString("license"));
                        nid.add(jo.getString("nid"));

                    }

                    l1.setAdapter(new custom4(Nutritionist.this,name,image,email,mobile,gender,qualification,experience,license));
                    l1.setOnItemClickListener(Nutritionist.this);


//                    ArrayAdapter<String> add=new ArrayAdapter<String>(Addbookingcode.this,android.R.layout.simple_spinner_item,floors);
//                    floor.setAdapter(add);
                    // l1.setAdapter(new custom2(Monitoring_signal.this,notification,date));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lid", sh.getString("lid", ""));
//                params.put("date", date1);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;

            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                name1=arg0;
//                Toast.makeText(getApplicationContext(),"okkkk "+name,Toast.LENGTH_LONG).show();
                search(name1);
                return true;
            }


        });
    }

    private void search(String name1) {

        url = "http://" + sh.getString("ip", "") + ":5000/nutritionistsearch";
//        aprtmnttyp.setOnItemSelectedListener(Addbook.this);
        RequestQueue queue = Volley.newRequestQueue(Nutritionist.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    name=new ArrayList<>(ar.length());
                    image = new ArrayList<>(ar.length());
                    email = new ArrayList<>(ar.length());
                    mobile = new ArrayList<>(ar.length());
                    gender=new ArrayList<>(ar.length());
                    qualification = new ArrayList<>(ar.length());
                    experience = new ArrayList<>(ar.length());
                    license = new ArrayList<>(ar.length());


                    for (int i = 0; i < ar.length(); i++) {

                        JSONObject jo = ar.getJSONObject(i);
                        name.add(jo.getString("name"));
                        image.add(jo.getString("image"));
                        email.add(jo.getString("email"));
                        mobile.add(jo.getString("mobile"));
                        gender.add(jo.getString("gender"));
                        qualification.add(jo.getString("qualification"));
                        experience.add(jo.getString("experience"));
                        license.add(jo.getString("license"));

                    }

                    l1.setAdapter(new custom4(Nutritionist.this,name,image,email,mobile,gender,qualification,experience,license));
                    l1.setOnItemClickListener(Nutritionist.this);


//                    ArrayAdapter<String> add=new ArrayAdapter<String>(Addbookingcode.this,android.R.layout.simple_spinner_item,floors);
//                    floor.setAdapter(add);
                    // l1.setAdapter(new custom2(Monitoring_signal.this,notification,date));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lid", sh.getString("lid", ""));
                params.put("name1", name1);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

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
        Intent in=new Intent(getApplicationContext(),ViewNutritionist.class);

        in.putExtra("name",name.get(i));
        in.putExtra("image",image.get(i));
        in.putExtra("email",email.get(i));
        in.putExtra("mobile",mobile.get(i));
        in.putExtra("gender",gender.get(i));
        in.putExtra("qualification",qualification.get(i));
        in.putExtra("experience",experience.get(i));
        in.putExtra("license",license.get(i));
        in.putExtra("nid",nid.get(i));

        startActivity(in);

    }
}