package com.seniorcontrol.administrator.httpregist;

import android.os.Handler;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class GsonParserThread extends Thread{
    private Handler handler;
    private URL url;
    private ListView listView;
    private GsonAdapter adapter;

    public GsonParserThread(ListView listView, String url, Handler handler, GsonAdapter adapter){
        this.handler = handler;
        this.listView = listView;
        this.adapter = adapter;
        try {
            this.url = new URL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //super.run();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);

            StringBuffer stringBuffer = new StringBuffer();

            String response;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            while ((response = bufferedReader.readLine()) != null){
                stringBuffer.append(response);
            }

            if (stringBuffer.length() != 0){
                //final List<Person> persons = parserJson(stringBuffer.toString());
                final List<Person> persons = parserGson(stringBuffer.toString());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setData(persons);
                        listView.setAdapter(adapter);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private List<Person> parserGson(String json){
        Result result;

        Gson gson = new Gson();
        result = gson.fromJson(json, Result.class);


        return result.getPersonData();
    }
    private List<Person> parserJson(String json){
        List<Person> persons = new ArrayList<Person>();

        try {
            JSONObject object = new JSONObject(json);

            int result = object.getInt("result");

            if (result == 1){
                JSONArray jsonArray = object.getJSONArray("personData");

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String name = jsonObject.getString("name");
                    int age = jsonObject.getInt("age");
                    String url = jsonObject.getString("url");

                    JSONObject schoolInfoObject = new JSONObject(jsonObject.getString("schoolInfo"));
                    SchoolInfo schoolInfo = new SchoolInfo(schoolInfoObject.getString("schoolName"));
                    persons.add(new Person(age, name, schoolInfo, url));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (persons.size() == 0 ? null : persons);
    }


}

