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

public class Register extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5;
    Button b1;
    RadioGroup r;
    RadioButton r1,r2;
    SharedPreferences sh;
    String name,email,mobile,username,password,gender,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=findViewById(R.id.name);
        e2=findViewById(R.id.email);
        e3=findViewById(R.id.mobile);
        e4=findViewById(R.id.username);
        e5=findViewById(R.id.password);
        r=findViewById(R.id.radiogroup);
        r1=findViewById(R.id.male);
        r2=findViewById(R.id.female);
        b1=findViewById(R.id.button1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = e1.getText().toString();
                email = e2.getText().toString();
                mobile = e3.getText().toString();
                username=e4.getText().toString();
                password=e5.getText().toString();
                if(r1.isChecked())
                {
                    gender=r1.getText().toString();
                }
                else
                {
                    gender=r2.getText().toString();
                }

                if (name.equalsIgnoreCase("")) {
                    e1.setError("Enter Name");
                    e1.requestFocus();
                }
                else if ((name.length() <= 3)) {
                    e1.setError("Length too short");
                    e1.requestFocus();
                }
                else if (!name.matches("^[a-zA-Z\\s]+$")) {
                    e1.setError("Only Characters Allowed");
                    e1.requestFocus();
                }
                else if (email.equalsIgnoreCase("")) {
                    e2.setError("Enter email");
                    e2.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    e2.setError("Invalid email");
                    e2.requestFocus();
                }
                else if (mobile.equalsIgnoreCase("")) {
                    e3.setError("Enter mobile");
                    e3.requestFocus();
                }
                else if (mobile.length() != 10) {
                    e3.setError("Invalid mobile");
                    e3.requestFocus();
                }
                else if ((username.length() <= 3)) {
                    e4.setError("Length too short");
                    e4.requestFocus();
                }
                else if (username.equalsIgnoreCase("")) {
                    e4.setError("Enter username");
                    e4.requestFocus();
                }
                else if (!username.matches(("^[a-z,A-Z]*$"))) {
                    e4.setError(" characters  allowed");
                    e4.requestFocus();
                }
                else  if (password.equalsIgnoreCase("")) {
                    e5.setError("enter your  password");
                    e5.requestFocus();
                }
//                else if (!password.matches(("^[a-z,A-Z]*$"))) {
//                    e5.setError(" characters  allowed");
//                    e5.requestFocus();
//                }
                else if (r.getCheckedRadioButtonId() == -1){
                    Toast.makeText(Register.this, "Select gender", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/reg";
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
                                    String lid = json.getString("lid");
                                    SharedPreferences.Editor edd= sh.edit();
                                    edd.putString("lid",lid);
                                    edd.commit();
                                    if (res.equalsIgnoreCase("success")) {
                                        Toast.makeText(Register.this, "success", Toast.LENGTH_SHORT).show();

                                        Intent ik = new Intent(getApplicationContext(), Healthinfo.class);
                                        ik.putExtra("lid",lid);

                                        startActivity(ik);

                                    }
                                    else {
                                        Toast.makeText(Register.this, res, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(Register.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                                params.put("username", username);
                                params.put("password", password);
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