package com.example.smarthealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddReminder extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText e1,e2,e3,e4,e5;
    Button b1,b2,b3;
    ListView l;
    String medicine,sdate,edate,url,time,dose;
    SharedPreferences sh;
    ArrayList<String> dose1;
    DatePickerDialog datepicker;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=findViewById(R.id.editText1);
        e2=findViewById(R.id.editText2);
        e3=findViewById(R.id.editText3);
//        e4=findViewById(R.id.editText4);
        e5=findViewById(R.id.editText5);
        l=findViewById(R.id.listview);

        b1=findViewById(R.id.button1);
        b2=findViewById(R.id.button2);
        b3=findViewById(R.id.button3);

//        l.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
        e5.setVisibility(View.GONE);


        e2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(AddReminder.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String _year = String.valueOf(year);
                    String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                    String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                    String _pickedDate = year + "-" + _month + "-" + _date;
                    Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                    e2.setText(_pickedDate);
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        }
    });

        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = year + "-" + _month + "-" + _date;
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                        e3.setText(_pickedDate);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });


        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String am_pm;
                        if (selectedHour > 12)
                        {
                            selectedHour = selectedHour - 12;
                            am_pm = "PM";
                        } else {
                            am_pm = "AM";
                        }
                        e5.setText( selectedHour + ":" + selectedMinute + " " +am_pm);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b2.setVisibility(view.VISIBLE);
                b3.setVisibility(view.VISIBLE);
                e5.setVisibility(view.VISIBLE);
                medicine = e1.getText().toString();
                sdate = e2.getText().toString();
                edate = e3.getText().toString();
//                num = e4.getText().toString();


                try {
                    SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
                    if(dfDate.parse(sdate).before(dfDate.parse(edate)))
                    {
                        Toast.makeText(AddReminder.this, "correct date", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (medicine.equalsIgnoreCase("")) {
                    e1.setError("Enter Medicine");
                    e1.requestFocus();
                }
                else if (sdate.equalsIgnoreCase("")) {
                    e2.setError("Enter Start date");
                    e2.requestFocus();
                }
                else if (edate.equalsIgnoreCase("")) {
                    e3.setError("Enter End date");
                    e3.requestFocus();
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(AddReminder.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/addrem";
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
                                String mid = json.getString("mid");

                                SharedPreferences.Editor edp = sh.edit();
                                edp.putString("medid", mid);
                                edp.commit();



//                                if (res.equalsIgnoreCase("success")) {
//                                    Toast.makeText(AddReminder.this, "Added successfully", Toast.LENGTH_SHORT).show();
//                                    Intent ik = new Intent(getApplicationContext(), Reminder.class);
//                                    startActivity(ik);
//
//                                } else {
//
                                    Toast.makeText(AddReminder.this, res, Toast.LENGTH_SHORT).show();
//
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(AddReminder.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                            params.put("medicine", medicine);
                            params.put("startdate", sdate);
                            params.put("enddate", edate);
//                            params.put("num", num);
                            params.put("lid", sh.getString("lid", ""));
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                    b1.setVisibility(View.GONE);
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                time = e5.getText().toString();
                Toast.makeText(AddReminder.this, ""+time, Toast.LENGTH_SHORT).show();

                if (time.equalsIgnoreCase("")) {
                    e5.setError("Enter Time");
                    e5.requestFocus();
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(AddReminder.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/adddose";
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
                                    Toast.makeText(AddReminder.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                    view_dose();

//                                    Intent ik = new Intent(getApplicationContext(), Reminder.class);
//                                    startActivity(ik);
//
                                }
//                                else {
//
//                                    Toast.makeText(AddReminder.this, res, Toast.LENGTH_SHORT).show();
//
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(AddReminder.this, "or " + e, Toast.LENGTH_SHORT).show();
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
                            params.put("time", time);
                            params.put("mid", sh.getString("medid", ""));
                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(AddReminder.this, "Added successfully", Toast.LENGTH_SHORT).show();
//                Intent ik = new Intent(getApplicationContext(), Reminder.class);
//                startActivity(ik);

                RequestQueue queue = Volley.newRequestQueue(AddReminder.this);
                url = "http://" + sh.getString("ip", "") + ":5000/ndose";
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
                                Toast.makeText(AddReminder.this, "Added successfully", Toast.LENGTH_SHORT).show();

                                Intent ik = new Intent(getApplicationContext(), Reminder.class);
                                startActivity(ik);

                            }
                            else {
                                    Toast.makeText(AddReminder.this, res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddReminder.this, "or " + e, Toast.LENGTH_SHORT).show();
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
                        params.put("mid", sh.getString("medid", ""));
                        return params;
                    }
                };
                queue.add(stringRequest);

            }
        });
    }


    private void view_dose() {
        url = "http://" + sh.getString("ip", "") + ":5000/viewdose";
        RequestQueue queue = Volley.newRequestQueue(AddReminder.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);
                    dose1 = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        dose1.add(jo.getString("dose"));

                    }

                    ArrayAdapter<String> ad=new ArrayAdapter<>(AddReminder.this,android.R.layout.simple_list_item_1,dose1);
                    l.setAdapter(ad);
                    l.setOnItemClickListener(AddReminder.this);


//                    l1.setAdapter(new custom(Reminder.this, medicine));
//                    l1.setOnItemClickListener(view_menu.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AddReminder.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mid", sh.getString("medid", ""));
                return params;
            }
        };
        queue.add(stringRequest);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


//
//    private void updateLabel() {
//        String myFormat="yyyy-MM-dd";
//        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
//        e2.setText(dateFormat.format(myCalendar.getTime()));
//    }
//
//    private void updateLabel2() {
//        String myFormat="yyyy-MM-dd";
//        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
//        e3.setText(dateFormat.format(myCalendar.getTime()));
//    }
//



}