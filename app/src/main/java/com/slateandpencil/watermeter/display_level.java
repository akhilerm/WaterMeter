package com.slateandpencil.watermeter;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.HashMap;


public class display_level extends AppCompatActivity {
    MyService mservice;
    HashMap<Integer,Float> m=new HashMap<Integer,Float>();
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;

    int n;//tank no of the  current tank whose level is to be known

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView1 = (ImageView)findViewById(R.id.water_lvl1);
        imageView2 = (ImageView)findViewById(R.id.water_lvl2);
        imageView3 = (ImageView)findViewById(R.id.water_lvl3);
        imageView4 = (ImageView)findViewById(R.id.water_lvl4);
        setTitle(getIntent().getStringExtra("tank_name"));
        n=getIntent().getIntExtra("tank_no",1);
          }
    @Override
    protected void onStart() {
        super.onStart();
        Intent  i=new Intent(this,MyService.class);
        bindService(i, mconnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop() {
        super.onStop();

            unbindService(mconnection);

        }

    public void draw(float lvl){
        int color = Color.parseColor("#40A4DF");
        float val=(float)(0.86*(1-(lvl/100)));
        imageView1.setColorFilter(color);
        imageView2.setColorFilter(color);
        imageView3.setColorFilter(color);
        imageView4.setColorFilter(color);
        //Animation for upward movement of water
        Animation animation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.86f, Animation.RELATIVE_TO_SELF, val);
        Animation animation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0.86f, Animation.RELATIVE_TO_SELF, val);
        Animation animation3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0.86f, Animation.RELATIVE_TO_SELF, val);
        Animation animation4 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0.86f, Animation.RELATIVE_TO_SELF, val);
        animation1.setDuration(5300);
        animation2.setDuration(5300);
        animation3.setDuration(5100);
        animation4.setDuration(5100);
        animation1.setFillAfter(true);
        animation2.setFillAfter(true);
        animation3.setFillAfter(true);
        animation4.setFillAfter(true);
        imageView1.startAnimation(animation1);
        imageView2.startAnimation(animation2);
        imageView3.startAnimation(animation3);
        imageView4.startAnimation(animation4);
        //Animation for wave movement of water
        /*switch((int)(lvl*10)){
            case 1:
                val+=0.085;
                break;
            case 2:
                val+=0.075;
                break;
            case 3:
                val+=0.065;
                break;
            case 4:
                val+=0.055;
                break;
            case 5:
                val+=0.047;
                break;
            case 6:
                val+=0.037;
                break;
            case 7:
                val+=0.027;
                break;
            case 8:
                val+=0.018;
                break;
            case 9:
                val+=0.008;
                break;
        }*/
       // if()
        final Animation animation5 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, val, Animation.RELATIVE_TO_SELF, val);
        final Animation animation6 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, val, Animation.RELATIVE_TO_SELF, val);
        final Animation animation7 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, val, Animation.RELATIVE_TO_SELF, val);
        final Animation animation8 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, val, Animation.RELATIVE_TO_SELF, val);
        animation5.setRepeatCount(Animation.INFINITE);
        animation6.setRepeatCount(Animation.INFINITE);
        animation7.setRepeatCount(Animation.INFINITE);
        animation8.setRepeatCount(Animation.INFINITE);
        animation5.setFillAfter(true);
        animation6.setFillAfter(true);
        animation7.setFillAfter(true);
        animation8.setFillAfter(true);
        animation5.setDuration(5300);
        animation6.setDuration(5300);
        animation7.setDuration(4100);
        animation8.setDuration(4100);
        //Animation Listener
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView1.startAnimation(animation5);
                imageView2.startAnimation(animation6);
                imageView3.startAnimation(animation7);
                imageView4.startAnimation(animation8);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Wave animation ends here
    }

    public void show()  {

            m = (HashMap) mservice.display();

            float lvl;
            lvl = (float) m.get(n);
            Log.e("varunnilla", "" + lvl);
            draw(lvl);

        }


private ServiceConnection mconnection=new ServiceConnection(){
    @Override
    public void onServiceConnected(ComponentName className,IBinder service)
    {
        MyService.binds   bind=(MyService.binds) service;
        mservice=bind.getService();

            show();



    }
    @Override
    public void onServiceDisconnected(ComponentName arg0) {

    }

};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.display_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Log.e("refresh clicked","OK");
                show();


        }
        return true;//return super.onOptionsSelected(MenuItem item);


    }
}
