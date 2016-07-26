package com.seniorcontrol.administrator.httpregist;

import android.os.Handler;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class XmlThread extends Thread{
    private URL url;
    private Handler handler;
    private ListView listView;
    private GsonAdapter adapter;

    public XmlThread(ListView listView, String url, Handler handler, GsonAdapter adapter) {
        //super();
        try {
            this.url = new URL(url);
            this.handler = handler;
            this.listView = listView;
            this.adapter = adapter;
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

            InputStream inputStream = httpURLConnection.getInputStream();

            final List<Person> persons = parserXML(inputStream);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.setData(persons);
                    listView.setAdapter(adapter);
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Person> parserXML(InputStream inputStream) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        XmlPullParser parser = factory.newPullParser();

        parser.setInput(inputStream, "UTF-8");

        int eventType = parser.getEventType();
        Person person = null;
        List<Person> persons = new ArrayList<Person>();
        try {
        while (eventType != XmlPullParser.END_DOCUMENT){
            String tagName = parser.getName();

            switch (eventType){
                case XmlPullParser.START_TAG:
                    if (tagName.equals("resource")){
                        person = new Person();
                    }else if (tagName.equals("name")){
                        if (person != null) {
                            person.setName(parser.nextText());
                        }
                    }else if (tagName.equals("age")){
                        if (person != null){
                            person.setAge(Integer.parseInt(parser.nextText()));
                        }
                    }else if (tagName.equals("url")){
                        if (person != null){
                            person.setUrl(parser.nextText());
                        }
                    }else if (tagName.equals("school")){
                        if (person != null){
                            person.setSchoolInfo(new SchoolInfo(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (tagName.equals("resource")){
                        persons.add(person);
                    }
                    break;
            }


            eventType = parser.next();

        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persons.size() != 0 ? persons : null;
    }
}
