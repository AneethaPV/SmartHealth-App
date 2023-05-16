package com.example.smarthealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
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
import java.util.Map;

public class EditReminder extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText e1, e2, e3;
    Button b;
    ListView l;
    String medicine, sdate, edate, d, url, url1,password;
    SharedPreferences sh;
    DatePickerDialog datepicker;
    final Calendar myCalendar = Calendar.getInstance();
    ArrayList<String> dose1,did;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1 = findViewById(R.id.editText1);
        e2 = findViewById(R.id.editText2);
        e3 = findViewById(R.id.editText3);
        b = findViewById(R.id.button1);
        l = findViewById(R.id.listview);
        dose();
        url1 = "http://" + sh.getString("ip", "") + ":5000/viewrem";

        RequestQueue queue = Volley.newRequestQueue(EditReminder.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);

                    {
                        JSONObject jo = ar.getJSONObject(0);
                        e1.setText(jo.getString("medicine"));
                        e2.setText(jo.getString("startdate"));
                        e3.setText(jo.getString("enddate"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("lid", sh.getString("lid", ""));
                params.put("mid", sh.getString("mid", ""));

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(EditReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
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
                DatePickerDialog dialog = new DatePickerDialog(EditReminder.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
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


//        e4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(EditReminder.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//                        String am_pm;
//                        if (selectedHour > 12)
//                        {
//                            selectedHour = selectedHour - 12;
//                            am_pm = "PM";
//                        } else {
//                            am_pm = "AM";
//                        }
//                        e4.setText( selectedHour + ":" + selectedMinute + " " +am_pm);
//                    }
//                }, hour, minute, false);
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                medicine = e1.getText().toString();
                sdate = e2.getText().toString();
                edate = e3.getText().toString();
                try {
                    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
                    if (dfDate.parse(edate).before(dfDate.parse(sdate))) {
                        Toast.makeText(EditReminder.this, "***************", Toast.LENGTH_SHORT).show();

                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (medicine.equalsIgnoreCase("")) {
                    e1.setError("Enter Medicine");
                    e1.requestFocus();
                } else if (sdate.equalsIgnoreCase("")) {
                    e2.setError("Enter Start date");
                    e2.requestFocus();
                } else if (edate.equalsIgnoreCase("")) {
                    e3.setError("Enter End date");
                    e3.requestFocus();
                } else {
                    RequestQueue queue = Volley.newRequestQueue(EditReminder.this);
                    url = "http://" + sh.getString("ip", "") + ":5000/updaterem";
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
                                    Toast.makeText(EditReminder.this, "Edited successfully", Toast.LENGTH_SHORT).show();
                                    Intent ik = new Intent(getApplicationContext(), Reminder.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(EditReminder.this, res, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditReminder.this, "or " + e, Toast.LENGTH_SHORT).show();

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
                            params.put("mid", sh.getString("mid", ""));
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }

    private void dose() {
        url = "http://" + sh.getString("ip", "") + ":5000/viewdose";
        RequestQueue queue = Volley.newRequestQueue(EditReminder.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                try {

                    JSONArray ar = new JSONArray(response);
                    dose1 = new ArrayList<>();
                    did = new ArrayList<>();


                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        dose1.add(jo.getString("dose"));
                        did.add(jo.getString("did"));

                    }

                    ArrayAdapter<String> ad = new ArrayAdapter<>(EditReminder.this, android.R.layout.simple_list_item_1, dose1);
                    l.setAdapter(ad);
                    l.setOnItemClickListener(EditReminder.this);

//                    l1.setAdapter(new custom(Reminder.this, medicine));
//                    l1.setOnItemClickListener(view_menu.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(EditReminder.this, "err" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mid",sh.getString("mid",""));
                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditReminder.this);
        alertDialog.setTitle("MEDICINE REMINDER");
        alertDialog.setMessage("Enter Date");

        final EditText input = new EditText(EditReminder.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditReminder.this, new TimePickerDialog.OnTimeSetListener() {
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
                        input.setText( selectedHour + ":" + selectedMinute + " " +am_pm);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        alertDialog.setPositiveButton("UPDATE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        password = input.getText().toString();

                        RequestQueue queue = Volley.newRequestQueue(EditReminder.this);
                        url = "http://" + sh.getString("ip", "") + ":5000/editdose";

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
                                        Toast.makeText(EditReminder.this, "Updated..", Toast.LENGTH_SHORT).show();

                                        Intent ik = new Intent(getApplicationContext(), EditReminder.class);
                                        startActivity(ik);

                                    }
                                    else {
                                        Toast.makeText(EditReminder.this, res, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(EditReminder.this, "or " + e, Toast.LENGTH_SHORT).show();
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
                                params.put("did",did.get(i));
                                params.put("dose",password);
                                return params;
                            }
                        };
                        queue.add(stringRequest);



                    }
                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }




}