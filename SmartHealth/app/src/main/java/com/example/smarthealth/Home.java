package com.example.smarthealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class Home extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    BottomNavigationView bottomNavigationView;
    ArrayList<String> caption,image,date,blog;
    String url;
    SharedPreferences sh;
    ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(Home.this);
        l1=findViewById(R.id.lv);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        url = "http://" + sh.getString("ip", "") + ":5000/pics";
//        aprtmnttyp.setOnItemSelectedListener(Addbook.this);
        RequestQueue queue = Volley.newRequestQueue(Home.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    image=new ArrayList<>(ar.length());
                    caption = new ArrayList<>(ar.length());
                    date = new ArrayList<>(ar.length());
                    blog = new ArrayList<>(ar.length());


                    for (int i = 0; i < ar.length(); i++) {

                        JSONObject jo = ar.getJSONObject(i);
                        image.add(jo.getString("image"));
                        caption.add(jo.getString("caption"));
                        date.add(jo.getString("date"));
                        blog.add(jo.getString("blog"));

                    }
                    l1.setAdapter(new custom2(Home.this,caption,image));
                    l1.setOnItemClickListener(Home.this);

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
        Intent in=new Intent(getApplicationContext(),HealthBlog.class);

        in.putExtra("caption",caption.get(i));
        in.putExtra("image",image.get(i));
        in.putExtra("date",date.get(i));
        in.putExtra("blog",blog.get(i));
        startActivity(in);

    }
}