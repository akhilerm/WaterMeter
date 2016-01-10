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
import android.widget.ImageView;
import android.widget.Toast;
import java.util.HashMap;


public class display_level extends AppCompatActivity {
    MyService mservice;
    HashMap<Integer,Float> m=new HashMap<Integer,Float>();


    int n;//tank no of the  current tank whose level is to be known

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("tank_name"));
        //ImageView imageView=(ImageView)findViewById(R.id.water_lvl);
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

    public void show()  {

            m = (HashMap) mservice.display();

            float lvl;
            lvl = (float) m.get(n);
            Log.e("varunnilla",""+lvl);
            Toast.makeText(getApplicationContext(), ""+lvl, Toast.LENGTH_LONG).show();



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
