package com.slateandpencil.watermeter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

public class add_tank extends AppCompatActivity {

    EditText tank_name, ip_addr, port_no;
    SwitchCompat notification;
    boolean option=true;
    int tank_no;
    Cursor resultSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_tank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        option=getIntent().getBooleanExtra("option",true);
        tank_name = (EditText) findViewById(R.id.input_name);
        ip_addr = (EditText) findViewById(R.id.input_ip);
        port_no = (EditText) findViewById(R.id.input_port);
        notification = (SwitchCompat) findViewById(R.id.Switch);
        if(option==false){
            setTitle(R.string.action_settings);
            tank_no=getIntent().getIntExtra("tank_no", 1);
            SQLiteDatabase sb;
            sb=openOrCreateDatabase("watermeter",MODE_PRIVATE,null);
            resultSet=sb.rawQuery("select name,ip,port,enable from tank where num = "+tank_no+";",null);
            resultSet.moveToNext();
            tank_name.setText(resultSet.getString(0));
            ip_addr.setText(resultSet.getString(1));
            port_no.setText(resultSet.getString(2));
            if(resultSet.getString(3).equals("1"))
                notification.setChecked(true);
            else
                notification.setChecked(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        if(option==true) {
            MenuItem item = menu.findItem(R.id.delete);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SQLiteDatabase sb=openOrCreateDatabase("watermeter", MODE_PRIVATE, null);
        String name = tank_name.getText().toString();
        String ip = ip_addr.getText().toString();
        String port = port_no.getText().toString();
        int enable;
        int count = 0;
        if (notification.isChecked())
            enable = 1;
        else
            enable = 0;
        switch (item.getItemId()) {
            case R.id.save:
                if(option==true){
                    resultSet = sb.rawQuery("Select num from  tank;", null);
                    while (resultSet.moveToNext()){
                    count = Integer.parseInt(resultSet.getString(0));}
                    sb.execSQL("INSERT INTO 'tank' ('num','name','ip','port',enable) VALUES\n" +
                            "('" + (count + 1) + "','" + name + "','" + ip + "','" + port + "'," + enable + ");");

                    Toast.makeText(add_tank.this, "Added New Tank", Toast.LENGTH_SHORT).show();
                }
                else{
                    resultSet=sb.rawQuery("select name,ip,port,enable from tank where num = "+tank_no+";",null);
                    resultSet.moveToNext();
                    if(resultSet.getString(0).equals(name)&&resultSet.getString(1).equals(ip)&&resultSet.getString(2).equals(port)&&resultSet.getString(3).equals(""+enable)){
                        Toast.makeText(add_tank.this,"No changes made", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        sb.execSQL("update 'tank' set 'name'='"+name+"','ip'='"+ip+"','port'='"+port+"','enable'='"+enable+"' where num = "+tank_no+";");
                        Toast.makeText(add_tank.this,"Changes Saved", Toast.LENGTH_SHORT).show();
                    }

                }

               // Cursor resultSet;

                break;
            case R.id.delete:
                sb.execSQL("delete from tank where num="+tank_no+";");
                Toast.makeText(add_tank.this,"Tank removed", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(add_tank.this, "Changes Discarded", Toast.LENGTH_SHORT).show();

        }
       /* intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
        return true;//return super.onOptionsSelected(MenuItem item);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(add_tank.this, "Changes Discarded", Toast.LENGTH_SHORT).show();
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_slide_out_bottom);
    }


}
