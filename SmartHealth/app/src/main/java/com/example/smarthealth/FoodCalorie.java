package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodCalorie extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<String> food,image,quantity,calories,proteins,carbs,fats,fiber;
    String url;
    SharedPreferences sh;
    TextView t;
    ListView l1;

    SearchView sv;
    String name1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_calorie);

        l1=findViewById(R.id.lv1);

        sv=findViewById(R.id.search_bar);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url = "http://" + sh.getString("ip", "") + ":5000/foodcal";


        RequestQueue queue = Volley.newRequestQueue(FoodCalorie.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    food=new ArrayList<>(ar.length());
                    image = new ArrayList<>(ar.length());
                    quantity = new ArrayList<>(ar.length());
                    calories = new ArrayList<>(ar.length());
                    proteins = new ArrayList<>(ar.length());
                    carbs = new ArrayList<>(ar.length());
                    fats = new ArrayList<>(ar.length());
                    fiber = new ArrayList<>(ar.length());


                    for (int i = 0; i < ar.length(); i++) {

                        JSONObject jo = ar.getJSONObject(i);
                        food.add(jo.getString("food"));
                        image.add(jo.getString("image"));
                        quantity.add(jo.getString("quantity"));
                        calories.add(jo.getString("calories"));
                        proteins.add(jo.getString("proteins"));
                        carbs.add(jo.getString("carbs"));
                        fats.add(jo.getString("fats"));
                        fiber.add(jo.getString("fiber"));

                    }

                    l1.setAdapter(new custom5(FoodCalorie.this,food,image,quantity,calories,proteins,carbs,fats,fiber));
                    l1.setOnItemClickListener(FoodCalorie.this);

//                    l1.setAdapter(new custom4(Nutritionist.this,name,image,email,mobile,gender,qualification,experience,license));
//                    l1.setOnItemClickListener(Nutritionist.this);


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

        url = "http://" + sh.getString("ip", "") + ":5000/foodsearch";
//        aprtmnttyp.setOnItemSelectedListener(Addbook.this);
        RequestQueue queue = Volley.newRequestQueue(FoodCalorie.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    food=new ArrayList<>(ar.length());
                    image = new ArrayList<>(ar.length());
                    quantity = new ArrayList<>(ar.length());
                    calories = new ArrayList<>(ar.length());
                    proteins = new ArrayList<>(ar.length());
                    carbs = new ArrayList<>(ar.length());
                    fats = new ArrayList<>(ar.length());
                    fiber = new ArrayList<>(ar.length());


                    for (int i = 0; i < ar.length(); i++) {

                        JSONObject jo = ar.getJSONObject(i);
                        food.add(jo.getString("food"));
                        image.add(jo.getString("image"));
                        quantity.add(jo.getString("quantity"));
                        calories.add(jo.getString("calories"));
                        proteins.add(jo.getString("proteins"));
                        carbs.add(jo.getString("carbs"));
                        fats.add(jo.getString("fats"));
                        fiber.add(jo.getString("fiber"));

                    }

                    l1.setAdapter(new custom5(FoodCalorie.this,food,image,quantity,calories,proteins,carbs,fats,fiber));
                    l1.setOnItemClickListener(FoodCalorie.this);


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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}