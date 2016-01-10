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
    private float lvl=0;
    private String name;
    private boolean flag;
    HashMap<Integer,Float> hashMap=new HashMap<Integer,Float>();




    private final IBinder mbinder=new  binds();
    public MyService() {
    super("");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,START_STICKY,startId);
    }
    @Override
    protected  void onHandleIntent(Intent intent){
        Cursor resultset;
        SQLiteDatabase sb = openOrCreateDatabase("watermeter", MODE_PRIVATE, null);
        int olog=1,ilog;
        while(true) {
            resultset = sb.rawQuery("select num,ip,port,name from tank where enable=1", null);
            Log.e("Pass of Outer Loop",""+(olog++));
            ilog=1;
            while (resultset.moveToNext()) {
              //before executing, comment ip=,client,ObjectInputStream,lvl
                Log.e("Pass of Inner Loop",""+(ilog++));
                flag=false;
                t_num = Integer.parseInt(resultset.getString(0));
                try {
                    ip = InetAddress.getByName(resultset.getString(1));
                    port = Integer.parseInt(resultset.getString(2));
                    client = new Socket(ip, port);
                    name=resultset.getString(3);
                    ObjectInputStream objectinputstream = new ObjectInputStream(client.getInputStream());
                    lvl = Float.parseFloat((objectinputstream.readObject()).toString());
                    Log.e("Current Level",""+lvl);
                    hashMap.put(t_num, lvl);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(MyService.this)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setDefaults(Notification.DEFAULT_ALL);
                    if(lvl<15){
                        mBuilder.setContentTitle("Low Water Level");
                        mBuilder.setContentText("Water Level:"+lvl+" at "+ name);
                        flag=true;
                    }
                    else
                    if(lvl>95){
                        mBuilder.setContentTitle("Tank Almost Full");
                        mBuilder.setContentText("Water Level at "+name+":"+lvl);
                        flag=true;
                    }
                    else
                    if(lvl==100){
                        mBuilder.setContentTitle("Tank Full");
                        mBuilder.setContentText("Tank Full at "+name);
                        flag=true;
                    }
                    if(flag==true){
                        Intent resultIntent = new Intent(MyService.this, display_level.class);
                        resultIntent.putExtra("tank_no",t_num);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
                        stackBuilder.addParentStack(display_level.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(11, mBuilder.build());
                    }
                    client.close();
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
    {
        return hashMap;
    }





}
