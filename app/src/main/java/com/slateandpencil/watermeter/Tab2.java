package com.slateandpencil.watermeter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Akhil on 21-11-2015.
 */
public class Tab2 extends Fragment {
    ListView lv;
    SQLiteDatabase sb;
    Cursor resultSet = null;
    TextView textView;

    public Tab2() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = (TextView) getView().findViewById(R.id.empty_list);
        sb = getActivity().openOrCreateDatabase("watermeter", android.content.Context.MODE_PRIVATE, null);
        resultSet = sb.rawQuery("select name from tank;", null);
        if (resultSet.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        lv = (ListView) getView().findViewById(R.id.listView);
        ArrayList<String> x = new ArrayList<String>();
        while (resultSet.moveToNext()) {
            x.add(resultSet.getString(0));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), simple_list_item_1, x);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long ld) {

                String s=(String)adapter.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), add_tank.class);
                resultSet=sb.rawQuery("select num from tank where name ='"+s+"';",null);
                resultSet.moveToNext();
                int num=Integer.parseInt(resultSet.getString(0));
                intent.putExtra("option", false);
                Toast.makeText(getActivity(),num+"", Toast.LENGTH_SHORT).show();
                intent.putExtra("tank_no",num);
                Tab2.this.startActivity(intent);

            }
        });
    }
}

