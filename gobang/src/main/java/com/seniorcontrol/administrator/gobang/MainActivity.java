package com.seniorcontrol.administrator.gobang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    GobangView mGobangView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mGobangView = (GobangView)findViewById(R.id.GobangView);

        ((Button)findViewById(R.id.Restart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGobangView.restartGame();
            }
        });

        ((Button)findViewById(R.id.Back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGobangView.backPieces();
            }
        });
    }
}
