package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DietPlan extends AppCompatActivity {

    EditText e1,e2,e3,e4;
    Button b1;
    RadioGroup r;
    RadioButton r1,r2;
    String gender,age,height,cweight,fweight,url;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=findViewById(R.id.age);
        e2=findViewById(R.id.height);
        e3=findViewById(R.id.cweight);
        e4=findViewById(R.id.fweight);
        r=findViewById(R.id.radiogroup1);
        r1=findViewById(R.id.male);
        r2=findViewById(R.id.female);
        b1=findViewById(R.id.button1);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                age = e1.getText().toString();
                height = e2.getText().toString();
                cweight = e3.getText().toString();
                fweight=e4.getText().toString();
                if(r1.isChecked())
                {
                    gender=r1.getText().toString();
                }
                else
                {
                    gender=r2.getText().toString();
                }


                if (age.equalsIgnoreCase("")) {
                    e1.setError("Enter Age");
                    e1.requestFocus();
                }
                else if (age.length() <= 1) {
                    e1.setError("Invalid Age");
                    e1.requestFocus();
                }
                else if (age.length() >= 3) {
                    e1.setError("Invalid Age");
                    e1.requestFocus();
                }
                else if (height.equalsIgnoreCase("")) {
                    e2.setError("Enter height");
                    e2.requestFocus();
                }
                else if (height.length() <= 1) {
                    e2.setError("Invalid Height");
                    e2.requestFocus();
                }
                else if (height.length() >= 4) {
                    e2.setError("Invalid Height");
                    e2.requestFocus();
                }
                else if (cweight.equalsIgnoreCase("")) {
                    e3.setError("Enter weight");
                    e3.requestFocus();
                }
                else if (cweight.length() <= 1) {
                    e3.setError("Invalid weight");
                    e3.requestFocus();
                }
                else if (cweight.length() >= 4) {
                    e3.setError("Invalid weight");
                    e3.requestFocus();
                }
                else if (fweight.equalsIgnoreCase("")) {
                    e4.setError("Enter weight");
                    e4.requestFocus();
                }
                else if (fweight.length() <= 1) {
                    e4.setError("Invalid weight");
                    e4.requestFocus();
                }
                else if (fweight.length() >= 4) {
                    e4.setError("Invalid weight");
                    e4.requestFocus();
                }
                else if(r.getCheckedRadioButtonId() == -1){
                    Toast.makeText(DietPlan.this, "Select gender", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(DietPlan.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/predict1";
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
                                String img = json.getString("res");
                                SharedPreferences.Editor edd= sh.edit();
                                edd.putString("img",img);
                                edd.commit();
                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(DietPlan.this, "success", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Predict.class);
                                    ik.putExtra("res",img);
                                    startActivity(ik);

                                }
                                else {
                                    Toast.makeText(DietPlan.this, res, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(DietPlan.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                            params.put("age", age);
                            params.put("height", height);
                            params.put("cweight", cweight);
                            params.put("fweight", fweight);
                            params.put("gender", gender);
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });


    }
}