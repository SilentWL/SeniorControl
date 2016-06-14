package com.seniorcontrol.administrator.seniorcontrol;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CustomTittleView customTittleView = (CustomTittleView)findViewById(R.id.first);
        customTittleView.setOnClickListener(customTittleView);


    }


}
