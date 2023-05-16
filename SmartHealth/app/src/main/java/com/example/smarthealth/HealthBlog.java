package com.example.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthBlog extends AppCompatActivity {

    String image,caption,date,blog;
    TextView t1,t2,t3;
    ImageView i;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_blog);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t1=findViewById(R.id.textView1);
        t2=findViewById(R.id.textView2);
        t3=findViewById(R.id.textView3);
        i=findViewById(R.id.imageView);

        caption=getIntent().getStringExtra("caption");
        date=getIntent().getStringExtra("date");
        image=getIntent().getStringExtra("image");
        blog=getIntent().getStringExtra("blog");

        t1.setText(caption);
        t2.setText(date);
        t3.setText(blog);
        if(android.os.Build.VERSION.SDK_INT>9)
        {
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        java.net.URL thumb_u;
        try {

            //thumb_u = new java.net.URL("http://192.168.43.57:5000/static/photo/flyer.jpg");

            thumb_u = new java.net.URL("http://"+sh.getString("ip","")+":5000/media/"+image);
            Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
            i.setImageDrawable(thumb_d);

        }
        catch (Exception e)
        {
            Log.d("errsssssssssssss",""+e);
        }


    }
}