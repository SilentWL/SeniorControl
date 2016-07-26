package com.seniorcontrol.administrator.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seniorcontrol.administrator.aidl.FirstAidl;

public class MainActivity extends AppCompatActivity {
    private static final String REMOTE_SERVICE_ACTION = "com.seniorcontrol.administrator.aidl.RemoteService";

    private EditText num1, num2;
    private TextView result;
    private Button calc;
    private FirstAidl firstAidl;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            firstAidl = FirstAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            firstAidl = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1 = (EditText) findViewById(R.id.num1);
        num2 = (EditText) findViewById(R.id.num2);
        result = (TextView) findViewById(R.id.Result);

        calc = (Button) findViewById(R.id.Calc);
        bindService();
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstAidl != null){
                    try {
                        int calcResult = firstAidl.add(Integer.parseInt(num1.getText().toString()), Integer.parseInt(num2.getText().toString()));
                        result.setText(calcResult + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        findViewById(R.id.nextActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setAction(NextActivity.ACTION_NEXT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firstAidl != null) {
            unbindService(conn);
        }
    }

    private void bindService() {
        if (firstAidl == null) {
            Intent intent = new Intent();
            //intent.setComponent(new ComponentName("com.seniorcontrol.administrator.aidl", "com.seniorcontrol.administrator.aidl.RemoteService"));
            intent.setAction(REMOTE_SERVICE_ACTION);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }
    }
}
