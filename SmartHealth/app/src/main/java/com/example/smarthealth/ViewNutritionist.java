package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import java.util.Objects;

public class ViewNutritionist extends AppCompatActivity {
    RatingBar r1;
    String name,image,email,mobile,gender,qualification,experience,license,nid;
    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    ImageView i;
    Button b,b2,b3;
//    ListView l1;
    String url;
    ArrayList<String> name1,date,review,rating;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nutritionist);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t1 = findViewById(R.id.textView1);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        t4 = findViewById(R.id.textView4);
        t5 = findViewById(R.id.textView5);
        t6 = findViewById(R.id.textView6);
        t7 = findViewById(R.id.textView7);
        t8=findViewById(R.id.textView8);
        i = findViewById(R.id.imageView);
        b = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        r1=findViewById(R.id.ratingBar1);

        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        email = getIntent().getStringExtra("email");
        mobile = getIntent().getStringExtra("mobile");
        gender = getIntent().getStringExtra("gender");
        qualification = getIntent().getStringExtra("qualification");
        experience = getIntent().getStringExtra("experience");
        license = getIntent().getStringExtra("license");
        nid = getIntent().getStringExtra("nid");

        t1.setText(name);
        t2.setText(email);
        t3.setText(mobile);
        t4.setText(gender);
        t5.setText(qualification);
        t6.setText(experience);
        t7.setText(license);

        view_rating();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://" + sh.getString("ip", "") + ":5000/media/" + image);
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            i.setImageDrawable(thumb_d);

        } catch (Exception e) {
            Log.d("errsssssssssssss", "" + e);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String phone = "+34666777888";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobile, null));
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RatingReview.class);
                i.putExtra("nid", nid);
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getApplicationContext(), Chat.class);
                ii.putExtra("nid", nid);
                ii.putExtra("name",name);
                ii.putExtra("img",image);
                startActivity(ii);
            }
        });

    }
    private void view_rating() {
        RequestQueue queue = Volley.newRequestQueue(ViewNutritionist.this);
        url = "http://" + sh.getString("ip", "") + ":5000/nutrreview";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);

                try {
                    JSONObject json = new JSONObject(response);
                    String rating = json.getString("rating");
                    r1.setRating(Float.parseFloat(rating));
                    r1.setIsIndicator(true);
                    t8.setText(rating);

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
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nid", nid);
//                params.put("password", pw);
                return params;
            }
        };
        queue.add(stringRequest);

    }

}