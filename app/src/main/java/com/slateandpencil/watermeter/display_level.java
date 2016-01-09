package com.slateandpencil.watermeter;

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
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

public class display_level extends AppCompatActivity {
    MyService mservice;
    HashMap<String,Integer> m=new HashMap<String,Integer>();
    Intent intent1;


    int n;//tank no of the  current tank whose level is to be known

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent1=getIntent();
        n=intent1.getIntExtra("tank_no",0);
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
            //String ip;
            int port, lvl;
            //ip=(String)m.get("ip"+n);

            port = (int) m.get("port"+n);
            lvl = (int) m.get("lvl" + n);
            Toast.makeText(getApplicationContext(), "  port :" + port + " lvl:" + lvl, Toast.LENGTH_LONG).show();



        }


private ServiceConnection mconnection=new ServiceConnection(){
    @Override
    public void onServiceConnected(ComponentName className,IBinder service)
    {
        MyService.binds   bind=(MyService.binds) service;
        mservice=(MyService)bind.getService();

            show();



    }
    @Override
    public void onServiceDisconnected(ComponentName arg0) {

    }

};


}
