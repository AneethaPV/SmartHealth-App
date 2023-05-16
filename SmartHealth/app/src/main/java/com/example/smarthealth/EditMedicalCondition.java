package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class EditMedicalCondition extends AppCompatActivity {

    EditText e1, e2, e3;
    Button b1;
    String diabetes,cholestrol,pressure,url,url1;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medical_condition);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=findViewById(R.id.editText1);
        e2=findViewById(R.id.editText2);
        e3=findViewById(R.id.editText3);
        b1=findViewById(R.id.button1);

        url1 = "http://" + sh.getString("ip", "") + ":5000/medcond";

        RequestQueue queue = Volley.newRequestQueue(EditMedicalCondition.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    {
                        JSONObject jo=ar.getJSONObject(0);
                        e1.setText(jo.getString("diabetes"));
                        e2.setText(jo.getString("cholestrol"));
                        e3.setText(jo.getString("pressure"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("lid", sh.getString("lid", ""));

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diabetes = e1.getText().toString();
                cholestrol = e2.getText().toString();
                pressure = e3.getText().toString();


                if (diabetes.equalsIgnoreCase("")) {
                    e1.setError("Enter Date");
                    e1.requestFocus();
                }
//                else if (dob.length() != 10) {
//                    e1.setError("Invalid Dob");
//                    e1.requestFocus();
//                }
                else if (cholestrol.equalsIgnoreCase("")) {
                    e2.setError("Enter Height");
                    e2.requestFocus();
                }
//                else if (height.length() != 3) {
//                    e2.setError("Enter Height");
//                    e2.requestFocus();
//                }
                else if (pressure.equalsIgnoreCase("")) {
                    e3.setError("Enter current Weight");
                    e3.requestFocus();
                }
//                else if (cweight.length() !=2 ) {
//                    e3.setError("Enter current Weight");
//                    e3.requestFocus();
//                }
//                else if (gweight.equalsIgnoreCase("")) {
//                    e4.setError("Enter goal Weight");
//                    e4.requestFocus();
//                }
//                else if (gweight.length() !=2 ) {
//                    e4.setError("Enter current Weight");
//                    e4.requestFocus();
//                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(EditMedicalCondition.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/updatemedcond";

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
                                    Toast.makeText(EditMedicalCondition.this, " updated successfully", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), MedicalCondition.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(EditMedicalCondition.this, "failed", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditMedicalCondition.this, "or " + e, Toast.LENGTH_SHORT).show();

                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("diabetes", diabetes);
                            params.put("cholestrol", cholestrol);
                            params.put("pressure", pressure);

                            params.put("lid", sh.getString("lid", ""));

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }

            }
        });
    }
}