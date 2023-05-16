package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
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

public class MedicalCondition extends AppCompatActivity implements AdapterView.OnItemClickListener {

//    ListView l1;
    ArrayList<String> date,diabetes,cholestrol,pressure;
    Button b;
    WebView w;
    String url;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_condition);

//        l1=findViewById(R.id.listview);
        b=findViewById(R.id.button1);
        w=findViewById(R.id.web);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Toast.makeText(this, "++++++++"+getIntent().getStringExtra("t"), Toast.LENGTH_SHORT).show();
        w.getSettings().setJavaScriptEnabled(true);
        w.loadUrl("http://"+sh.getString("ip","")+":5000/graph/"+sh.getString("lid","0")+"-"+getIntent().getStringExtra("t"));




//        url = "http://" + sh.getString("ip", "") + ":5000/viewmedcond";
//        aprtmnttyp.setOnItemSelectedListener(Addbook.this);
//        RequestQueue queue = Volley.newRequestQueue(MedicalCondition.this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                // Display the response string.
//                Log.d("+++++++++++++++++", response);
//                try {
//                    JSONArray ar = new JSONArray(response);
//
//                    date=new ArrayList<>(ar.length());
//                    diabetes = new ArrayList<>(ar.length());
//                    cholestrol = new ArrayList<>(ar.length());
//                    pressure = new ArrayList<>(ar.length());
//
//                    for (int i = 0; i < ar.length(); i++) {
//
//                        JSONObject jo = ar.getJSONObject(i);
//                        date.add(jo.getString("date"));
//                        diabetes.add(jo.getString("diabetes"));
//                        cholestrol.add(jo.getString("cholestrol"));
//                        pressure.add(jo.getString("pressure"));
//
//                    }
//
//
//-------------------------------------------------
//
//                    l1.setAdapter(new custom3(MedicalCondition.this, date,diabetes,cholestrol,pressure));
//                    l1.setOnItemClickListener(MedicalCondition.this);
//
//-------------------------------------------------
//
//                    ArrayAdapter<String> add=new ArrayAdapter<String>(Addbookingcode.this,android.R.layout.simple_spinner_item,floors);
//                    floor.setAdapter(add);
//                    // l1.setAdapter(new custom2(Monitoring_signal.this,notification,date));
//                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("lid", sh.getString("lid", ""));
//                params.put("date", date1);
//
//                return params;
//            }
//        };
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),EditMedicalCondition.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}