package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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

public class EditHealth extends AppCompatActivity {

    EditText e1, e2, e3,e4,e5;
    Button b1;
//    RadioButton r1, r2;
    String dob,height,cweight,url,url1;
    SharedPreferences sh;

//    Spinner s;
//    String medcond,medcond1;
//    String [] medcon={"Pick one","Cholestrol","Diabetes","Pre-diabetes","None"};
//    String value;  // value to select

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_health);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=findViewById(R.id.editText1);
        e2=findViewById(R.id.editText2);
        e3=findViewById(R.id.editText3);
//        e4=findViewById(R.id.editText4);
        e5=findViewById(R.id.editText5);
        b1=findViewById(R.id.button1);
//        r1 = findViewById(R.id.yes);
//        r2 = findViewById(R.id.no);
        e5.setEnabled(false);

//        s=findViewById(R.id.spinner);

//        ArrayAdapter<String> ad=new ArrayAdapter<>(EditHealth.this,android.R.layout.simple_list_item_1,medcon);
//        s.setAdapter(ad);



        url1 = "http://" + sh.getString("ip", "") + ":5000/viewhealth";

        RequestQueue queue = Volley.newRequestQueue(EditHealth.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    {
                        JSONObject jo=ar.getJSONObject(0);
                        e1.setText(jo.getString("dob"));
                        e2.setText(jo.getString("height"));
                        e3.setText(jo.getString("cweight"));
                        e5.setText(jo.getString("bmi"));
//                        hd=jo.getString("hd");

//                        medcond=jo.getString("medcond");
//                        int spinnerPosition = ad.getPosition(medcond);
//                        s.setSelection(spinnerPosition);


//                        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        s.setAdapter(ad);
//                        int position = ad.getPosition(value);  // get position of value
//                        s.setSelection(position);  // set selected item by position

//                        if(hd.equals("yes"))
//                        {
//                            r1.setChecked(true);
//                        }
//                        else
//                        {
//                            r2.setChecked(true);
//                        }
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
                dob = e1.getText().toString();
                height = e2.getText().toString();
                cweight = e3.getText().toString();
//                if (r1.isChecked())
//                {
//                    hd = r1.getText().toString();
//                }
//                else
//                {
//                    hd = r2.getText().toString();
//                }
//                medcond1=s.getSelectedItem().toString();


                if (dob.equalsIgnoreCase("")) {
                    e1.setError("Enter Date");
                    e1.requestFocus();
                }
                else if (dob.length() != 10) {
                    e1.setError("Invalid Dob");
                    e1.requestFocus();
                }
                else if (height.equalsIgnoreCase("")) {
                    e2.setError("Enter Height");
                    e2.requestFocus();
                }
                else if (height.length() != 3) {
                    e2.setError("Enter Height");
                    e2.requestFocus();
                }
                else if (cweight.equalsIgnoreCase("")) {
                    e3.setError("Enter current Weight");
                    e3.requestFocus();
                }
                else if (cweight.length() !=2 ) {
                    e3.setError("Enter current Weight");
                    e3.requestFocus();
                }
//                else if (s.getSelectedItem().toString().trim().equals("Pick one")) {
//                    Toast.makeText(EditHealth.this, "Pick medical condition", Toast.LENGTH_SHORT).show();
//                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(EditHealth.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/updatehealth";

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
                                    Toast.makeText(EditHealth.this, " updated successfully", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Profile.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(EditHealth.this, "failed", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditHealth.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                            params.put("dob", dob);
                            params.put("height", height);
                            params.put("cweight", cweight);
//                            params.put("hd", hd);
//                            params.put("gweight", gweight);
//                            params.put("medcond", medcond1);

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