package com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.ListViewData.User1;
import com.listdadpter.ListNodeAdapter;
import com.seniorcontrol.administrator.activity.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<User1> user = null;
    private ListNodeAdapter mListNodeAdapter = null;
    ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        user = new ArrayList<User1>();

        user.add(new User1(1, -1, "1-1"));
        user.add(new User1(2, 1, "1-2"));
        user.add(new User1(3, 1, "1-3"));
        user.add(new User1(4, -1, "2-1"));
        user.add(new User1(5, -1, "3-1"));
        user.add(new User1(6, 2, "2-1"));
        user.add(new User1(7, 2, "2-2"));
        user.add(new User1(8, 6, "3-1"));
        user.add(new User1(9, 4, "2-1"));
        user.add(new User1(10, 4, "2-1"));
        user.add(new User1(11, 4, "2-1"));



        try {
            mListNodeAdapter = new ListNodeAdapter(listView, MainActivity.this, user);
            listView.setAdapter(mListNodeAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editText = new EditText(MainActivity.this);

                new AlertDialog.Builder(MainActivity.this).setTitle("Add Node").setView(editText).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListNodeAdapter.addNode(position, editText.getText().toString());
                    }
                }).setNegativeButton("Cancel", null).show();
                return true;
            }
        });


    }
}
