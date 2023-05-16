package com.example.smarthealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class Reminder extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l1;
    SharedPreferences sh;
    String url;
    ArrayList<String> medicine,startdate,enddate,num,mid;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        b = findViewById(R.id.button);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1 = findViewById(R.id.listview);
        url = "http://" + sh.getString("ip", "") + ":5000/viewreminder";
        RequestQueue queue = Volley.newRequestQueue(Reminder.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);
                    medicine = new ArrayList<>();
                    startdate = new ArrayList<>();
                    enddate = new ArrayList<>();
                    num = new ArrayList<>();
                    mid=new ArrayList<>();


                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        medicine.add(jo.getString("medicine"));
                        startdate.add(jo.getString("startdate"));
                        enddate.add(jo.getString("enddate"));
                        num.add(jo.getString("num"));
                        mid.add(jo.getString("mid"));

                    }

                    ArrayAdapter<String> ad=new ArrayAdapter<>(Reminder.this,android.R.layout.simple_list_item_1,medicine);
                    l1.setAdapter(ad);
                    l1.setOnItemClickListener(Reminder.this);


//                    l1.setAdapter(new custom(Reminder.this, medicine));
//                    l1.setOnItemClickListener(view_menu.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Reminder.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("lid", ""));
                return params;
            }
        };
        queue.add(stringRequest);



        b.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
        Intent i = new Intent(getApplicationContext(), AddReminder.class);
        startActivity(i);
    }
    });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int im, long l) {


        AlertDialog.Builder ald=new AlertDialog.Builder(Reminder.this);
        ald.setTitle("File")
                .setPositiveButton(" DELETE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        RequestQueue queue = Volley.newRequestQueue(Reminder.this);
                        url = "http://" + sh.getString("ip", "") + ":5000/delreminder";
//                    url = "http://192.168.29.166/farmerreg1";

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                Log.d("+++++++++++++++++", response);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    String res = json.getString("task");

                                    if (res.equalsIgnoreCase("success")) {
                                        Toast.makeText(Reminder.this, "Deleted successfully", Toast.LENGTH_SHORT).show();

                                        Intent ik = new Intent(getApplicationContext(), Reminder.class);

                                        startActivity(ik);

                                    }
                                    else {
                                        Toast.makeText(Reminder.this, res, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Reminder.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                                params.put("mid", mid.get(im));

                                return params;
                            }
                        };
                        queue.add(stringRequest);


                    }
                })
                .setNegativeButton(" Edit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent i=new Intent(getApplicationContext(),EditReminder.class);
                        i.putExtra("medicine", medicine.get(im));
                        i.putExtra("sdate", startdate.get(im));
                        i.putExtra("edate", enddate.get(im));
                        i.putExtra("num", num.get(im));
                        i.putExtra("mid",mid.get(im));
                        SharedPreferences.Editor edp = sh.edit();
                        edp.putString("mid", mid.get(im));
                        edp.commit();
                        startActivity(i);
                    }
                });

        AlertDialog al=ald.create();
        al.show();
    }
}