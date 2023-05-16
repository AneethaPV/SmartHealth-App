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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendFeedback extends AppCompatActivity {

    EditText e1;
    Button b;
    String feedback,url;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        e1=findViewById(R.id.editText1);
        b=findViewById(R.id.button1);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedback = e1.getText().toString();

                if (feedback.equalsIgnoreCase("")) {
                    e1.setError("Enter SendFeedback");
                    e1.requestFocus();
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(SendFeedback.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/addfeedback";
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
                                    Toast.makeText(SendFeedback.this, "Thanks for the Feedback", Toast.LENGTH_SHORT).show();
                                    Intent ik = new Intent(getApplicationContext(), Profile.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(SendFeedback.this, res, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SendFeedback.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                            params.put("feedback", feedback);
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