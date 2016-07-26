package com.seniorcontrol.administrator.httpregist;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private static final String jsonUrl = "http://192.168.199.18:8080/Web/GsonServlet";
    private static final String xmlUrl = "http://192.168.199.18:8080/Web/resources.xml";
    private static final String fileUrl = "http://192.168.199.18:8080/Web/abc.mp3";
    private static final String uploadFileUrl = "http://192.168.199.18:8080/upload/Upload";

    private EditText name,age;
    private Button submitGet, submitPost;
    private ListView mListView = null;
    private Handler mHandler = null;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_list);
        mListView = (ListView) findViewById(R.id.listView);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                if (msg.what == 100){
                    if (++count == 3){
                        Toast.makeText(MainActivity.this, "Download Finished!!!", Toast.LENGTH_LONG).show();
                    }
                }else if (msg.what == 200){
                    Toast.makeText(MainActivity.this, "Upload Finished!!!", Toast.LENGTH_LONG).show();
                }
            }
        };
        //new GsonParserThread(mListView, jsonUrl, mHandler, new GsonAdapter(MainActivity.this, mHandler)).start();
        //new XmlThread(mListView, xmlUrl, mHandler, new GsonAdapter(MainActivity.this, mHandler)).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new DownLoad(mHandler).downLoadFile(fileUrl);
//            }
//        }).start();
        File file = Environment.getExternalStorageDirectory();
        File fileName = new File(file, "abc.mp3");
        new UploadThread(fileName.getAbsolutePath(), uploadFileUrl, mHandler).start();


//        name = (EditText) findViewById(R.id.name);
//        age = (EditText) findViewById(R.id.age);
//
//        submitGet = (Button) findViewById(R.id.submitGet);
//        submitPost = (Button) findViewById(R.id.submitPost);
//        submitGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    new Thread(new SubmitThread(new Handler(), MainActivity.this, url, name.getText().toString(), age.getText().toString(), true)).start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        submitPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    new Thread(new SubmitThread(new Handler(), MainActivity.this, url, name.getText().toString(), age.getText().toString(), false)).start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }
}
