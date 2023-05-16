package com.example.smarthealth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserReminder extends Service {

	private LocationManager locationManager;
	private Boolean locationChanged;
	private Handler handler = new Handler();
	public static boolean isService = true;
	private File root;
	private ArrayList<String> fileList = new ArrayList<String>();
	String ip="";
	String[] zone;
	String pc="",url="";
	SQLiteDatabase db;
	String datemsg = "";
	String imei="";
	String encodedImage = null;
	String table,req;
	ArrayList<String> name,date,Time;
	TelephonyManager telemanager;
	SharedPreferences sh;
	int NOTIFICATION_ID = 234;
	NotificationManager notificationManager;


	@Override
	public void onCreate() {
		   super.onCreate();
		notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Toast.makeText(this, "Started"+"======", Toast.LENGTH_SHORT).show();
	        isService =  true;
	    }
	    final String TAG="LocationService";
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	    	return super.onStartCommand(intent, flags, startId);
	   }
	   @Override

	   public void onLowMemory() {
	       super.onLowMemory();
	   }
	@Override
	public void onStart(Intent intent, int startId) {
		//  Toast.makeText(this, "Start services", Toast.LENGTH_SHORT).show();
		  telemanager  = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

		  sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		  String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		Toast.makeText(this, "+++++++++++", Toast.LENGTH_SHORT).show();
//		  if(!provider.contains("gps"))
//	        { //if gps is disabled
//	        	final Intent poke = new Intent();
//	        	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//	        	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//	        	poke.setData(Uri.parse("3"));
//	        	sendBroadcast(poke);
//	        }

//		  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//	      URL=preferences.getString("url", "");

	      handler.postDelayed(GpsFinder,10000);
	}

	@Override
	public void onDestroy() {
		 String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		   if(provider.contains("gps"))
		   { //if gps is enabled
		   final Intent poke = new Intent();
		   poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		   poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		   poke.setData(Uri.parse("3"));
		   sendBroadcast(poke);
		   }

		   handler.removeCallbacks(GpsFinder);
	       handler = null;
	       Toast.makeText(this, "Service Stopped..!!", Toast.LENGTH_SHORT).show();
	       isService = false;
	   }


	  public Runnable GpsFinder = new Runnable(){


	    public void run(){

	    	String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

//	  	  if(!provider.contains("gps"))
//	          { //if gps is disabled
//	          	final Intent poke = new Intent();
//	          	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//	          	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//	          	poke.setData(Uri.parse("3"));
//	          	sendBroadcast(poke);
//	          }

	    		//Toast.makeText(getApplicationContext(), phoneid, Toast.LENGTH_LONG).show();
	           // Log.d("MyService", String.valueOf("latitude"+curLocation.getLatitude()));

	            getnotify();

//	            db=new completedboperation(getApplicationContext());
//	            db.location(lati, logi);

	           // Toast.makeText(getApplicationContext(),URL+" received", Toast.LENGTH_SHORT).show();


      	handler.postDelayed(GpsFinder,1000);// register again to start after 20 seconds...
	    }

	  };

	private  void getnotify() {
		RequestQueue queue3 = Volley.newRequestQueue(UserReminder.this);
		String url3 = "http://" + sh.getString("ip", "") + ":5000/reminder1";
		// Request a string response from the provided URL.
		StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Display the response string.
				Log.d("+++++++++++++++++", response);
				try {
					JSONObject json = new JSONObject(response);
					String rslt = json.getString("task");
					if (rslt.equalsIgnoreCase("invalid")) {
//                                     Toast.makeText(getApplicationContext(), rslt, Toast.LENGTH_LONG).show();
					}
					else
					{
						String[] temp=rslt.split("\\@");
						if(temp.length>0){
							for (int i = 0; i < temp.length; i++) {
								String[] res=temp[i].split("\\#");
								String cur_time= new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
//								Toast.makeText(getApplicationContext(), cur_time+"--------+++"+res[1], Toast.LENGTH_LONG).show();

								if(cur_time.equals(res[1])) {
//									Toast.makeText(getApplicationContext(), cur_time+"--------+++"+res[1], Toast.LENGTH_LONG).show();

									notification_popup(res[0], res[1]);
								}
							}
//                            ed.putString("enddate", datemsg);
//                            ed.commit();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

//                        pDialog.hide();
				Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();


				return params;
			}
		};
		// Add the request to the RequestQueue.
		queue3.add(stringRequest3);

	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void notificationCheck() {
		// TODO Auto-generated method stub
		try {


//				String msg="Have "+medname+" \n on   "+time+" ";
			String msg="Carefull some distruptions are there ";


//				notification_popup(msg);
			}


		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}


	private void notificationChecknew() {
		// TODO Auto-generated method stub
		try {


				String msg="there is a information from "+table+" \n for   "+req+" ";
//			String msg="Be carefull You are in danger";


//			notification_popup(msg);
		}


		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

	}
	public void notification_popup(String names,String time) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String CHANNEL_ID = "my_channel_01";
			CharSequence name = "my_channel";
			String Description = "This is my channel";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			mChannel.setDescription(Description);
			mChannel.enableLights(true);
			mChannel.setLightColor(Color.RED);
			mChannel.enableVibration(true);
			mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//			mChannel.setVibrationPattern(new long[]{0, 800, 200, 1200, 300, 2000, 400, 4000});
			mChannel.setShowBadge(false);
			notificationManager.createNotificationChannel(mChannel);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_01")
				.setSmallIcon(R.drawable.icon)
				.setContentTitle("Pill Reminder : Notification For Medicine")
				.setContentText("Have "+names+" "+"on...."+time);


//		Intent resultIntent = new Intent(getApplicationContext(), Details.class);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//		stackBuilder.addParentStack(MainActivity.class);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		builder.setContentIntent(resultPendingIntent);

		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}

}
