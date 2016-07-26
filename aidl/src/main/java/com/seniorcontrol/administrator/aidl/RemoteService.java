package com.seniorcontrol.administrator.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class RemoteService extends Service{
    private static final String TAG = "RemoteService";

    @Nullable
    @Override
        public IBinder onBind(Intent intent) {
            return iBinder;
        }

        private IBinder iBinder = new FirstAidl.Stub(){
            @Override
            public int add(int num1, int num2) throws RemoteException {
                Log.d(TAG, "Receive remote request: num1=" + num1 + ", num2=" + num2);
                return num1 + num2;
        }
    };
}
