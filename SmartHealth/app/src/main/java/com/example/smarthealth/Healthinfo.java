package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Healthinfo extends AppCompatActivity {

    EditText e1,e2,e3,e5,e6,e7;
    Button b1;
//    RadioGroup r;
//    RadioButton r1,r2;
    SharedPreferences sh;
    String dob,height,cweight,diabetes,cholestrol,pressure,url;
//    Spinner s;
//    String medcond;
//    String [] medcon={"Pick one","Cholestrol","Diabetes","Pre-diabetes","None"};
    DatePickerDialog datepicker;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthinfo);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=findViewById(R.id.dob);
        e2=findViewById(R.id.height);
        e3=findViewById(R.id.cweight);
        e5=findViewById(R.id.diabetes);
        e6=findViewById(R.id.cholestrol);
        e7=findViewById(R.id.pressure);
        b1=findViewById(R.id.button1);
//        r=findViewById(R.id.radiogroup);
//        r1=findViewById(R.id.yes);
//        r2=findViewById(R.id.no);
//        s=findViewById(R.id.spinner);


//        ArrayAdapter<String> ad=new ArrayAdapter<>(Healthinfo.this,android.R.layout.simple_list_item_1,medcon);
//        s.setAdapter(ad);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };


        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Healthinfo.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dob = e1.getText().toString();
                height = e2.getText().toString();
                cweight = e3.getText().toString();
                diabetes = e5.getText().toString();
                cholestrol=e6.getText().toString();
                pressure=e7.getText().toString();
//                if(r1.isChecked())
//                {
//                    hd=r1.getText().toString();
//                }
//                else
//                {
//                    hd=r2.getText().toString();
//                }
//                medcond=s.getSelectedItem().toString();


                if (dob.equalsIgnoreCase("")) {
                    e1.setError("Enter Date");
                    e1.requestFocus();
                }
                else if (height.equalsIgnoreCase("")) {
                    e2.setError("Enter Height");
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
                    e3.setError("Enter current Weight");
                    e3.requestFocus();
                }
                else if (cweight.length() <= 1) {
                    e3.setError("Invalid Weight");
                    e3.requestFocus();
                }
                else if (cweight.length() >= 4) {
                    e3.setError("Invalid Height");
                    e3.requestFocus();
                }
                else if (diabetes.length() >= 4) {
                    e5.setError("Invalid");
                    e5.requestFocus();
                }
                else if (cholestrol.length() >= 4) {
                    e6.setError("Invalid");
                    e6.requestFocus();
                }
                else if (pressure.length() >= 4) {
                    e7.setError("Invalid");
                    e7.requestFocus();
                }
//                else if (s.getSelectedItem().toString().trim().equals("Pick one")) {
//                    Toast.makeText(Healthinfo.this, "Pick medical condition", Toast.LENGTH_SHORT).show();
//                }
//                else if (r.getCheckedRadioButtonId() == -1){
//                    Toast.makeText(Healthinfo.this, "Select heart disease", Toast.LENGTH_SHORT).show();
//                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(Healthinfo.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/hinfo";
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
                                    Toast.makeText(Healthinfo.this, "success", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Login.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(Healthinfo.this, res, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Healthinfo.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                            params.put("dob", dob);
                            params.put("height", height);
                            params.put("cweight", cweight);
                            params.put("diabetes", diabetes);
                            params.put("cholestrol", cholestrol);
                            params.put("pressure", pressure);
//                            params.put("hd", hd);

//                            params.put("medcond", medcond);
                            params.put("lid", sh.getString("lid", ""));
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }


    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        e1.setText(dateFormat.format(myCalendar.getTime()));
    }
}