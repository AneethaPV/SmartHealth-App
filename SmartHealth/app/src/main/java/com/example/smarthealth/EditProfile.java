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

public class EditProfile extends AppCompatActivity {

    EditText e1, e2, e3;
    RadioButton r1, r2;
    Button b1;
    String name,gender,email,mobile,url, url1;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1 = findViewById(R.id.editText1);
        e2 = findViewById(R.id.editText2);
        e3 = findViewById(R.id.editText3);
        r1 = findViewById(R.id.male);
        r2 = findViewById(R.id.female);
        b1 = findViewById(R.id.button1);

        url1 = "http://" + sh.getString("ip", "") + ":5000/viewprofile";

        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    {
                        JSONObject jo=ar.getJSONObject(0);
                        e1.setText(jo.getString("name"));
                        e2.setText(jo.getString("email"));
                        e3.setText(jo.getString("mobile"));
                        gender=jo.getString("gender");

                        if(gender.equals("Male"))
                        {
                            r1.setChecked(true);
                        }
                        else
                        {
                            r2.setChecked(true);
                        }
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
                name = e1.getText().toString();
                email = e2.getText().toString();
                mobile = e3.getText().toString();
                if (r1.isChecked())
                {
                    gender = r1.getText().toString();
                }
                else
                {
                    gender = r2.getText().toString();
                }


                if (name.equalsIgnoreCase(""))
                {
                    e1.setError("Enter first Name");
                    e1.requestFocus();
                }
                else if (!name.matches("^[a-z,A-Z]*$")) {
                    e1.setError("Characters Allowed");
                    e1.requestFocus();
                }
                else if (email.equalsIgnoreCase("")) {
                    e2.setError("Enter E-mail");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    e2.setError("Enter Valid E-mail");
                    e2.requestFocus();
                }
                else if (mobile.equalsIgnoreCase("")) {
                    e3.setError("Enter Phone Number");
                }
                else if (mobile.length() != 10) {
                    e3.setError("Minimum 10 No.s Required");
                    e3.requestFocus();
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(EditProfile.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/updateprofile";

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
                                    Toast.makeText(EditProfile.this, " updated successfully", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Profile.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(EditProfile.this, "failed", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditProfile.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                            params.put("name", name);
                            params.put("email", email);
                            params.put("mobile", mobile);
                            params.put("gender", gender);

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