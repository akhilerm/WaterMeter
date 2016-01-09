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

public class MyService extends IntentService {
    private Socket client;
    private InetAddress ip;
    private int port;
    private int t_num;
    private int lvl;
    public MyService() {
    super("");
    }
    @Override
    protected void onHandleIntent(Intent intent){
        Cursor resultset;

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.watermeter");
        int count=1;
        SQLiteDatabase sb = openOrCreateDatabase("watermeter", MODE_PRIVATE, null);
            while(true)
            {count++;

                Log.e("sent",count+"");
           /* resultset = sb.rawQuery("select num,ip,port from tank where enable=1", null);
            while (resultset.moveToNext())
                 {
                //t_num = Integer.parseInt(resultset.getString(0));
                try {
                    ip = InetAddress.getByName(resultset.getString(1));
                    port = Integer.parseInt(resultset.getString(2));
                    client = new Socket(ip, port);
                    ObjectInputStream objectinputstream = new ObjectInputStream(client.getInputStream());
                    lvl = Integer.parseInt((objectinputstream.readObject()).toString());





                    } catch (IOException | ClassNotFoundException e) {
                       e.printStackTrace();
                }


                 }


        broadcastIntent.putExtra("hi1", count);
           */// sendBroadcast(broadcastIntent);
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
