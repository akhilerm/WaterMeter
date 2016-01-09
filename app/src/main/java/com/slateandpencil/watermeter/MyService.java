package com.slateandpencil.watermeter;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class MyService extends IntentService {
    private Socket client;
    private InetAddress ip;
    private int port;
    private int t_num;
    private int lvl=0;
    HashMap<String,Integer> map=new HashMap<String,Integer>();




    private final IBinder mbinder=new  binds();
    public MyService() {
    super("");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    protected  void onHandleIntent(Intent intent){
        Cursor resultset;
        SQLiteDatabase sb = openOrCreateDatabase("watermeter", MODE_PRIVATE, null);

        while(true) {
            resultset = sb.rawQuery("select num,ip,port from tank where enable=1", null);

            while (resultset.moveToNext())
            {
              //before executing, comment ip=,client,ObjectInputStream,lvl
                t_num = Integer.parseInt(resultset.getString(0));
                try {
                    //ip = InetAddress.getByName(resultset.getString(1));
                    port = Integer.parseInt(resultset.getString(2));
                   /* client = new Socket(ip, port);
                    ObjectInputStream objectinputstream = new ObjectInputStream(client.getInputStream());
                    lvl = Integer.parseInt((objectinputstream.readObject()).toString());
*/
                    //map.put("ip"+t_num,ip.toString());
                    map.put("port"+t_num,port);
                    map.put("lvl"+t_num,lvl);
                    Log.e(""+port,""+lvl);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            resultset.close();



                }
        }


    @Override
    public IBinder onBind(Intent intent) {

        return mbinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }

    public class binds extends Binder {


        public MyService getService()
        {


            return MyService.this;
        }




    }

    public HashMap display()
    {int f=0;
        while(f<=1500000){
            f++;
        }
        return map;


    }





}
