package com.seniorcontrol.administrator.httpregist;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class GsonAdapter extends BaseAdapter{
    private List<Person> mPersonList;
    private Context mContext;
    private LayoutInflater mInflater;
    private Handler mHandler;

    public GsonAdapter(Context context, List<Person> list, Handler handler) {
        super();
        this.mContext = context;
        this.mPersonList = list;
        mInflater = LayoutInflater.from(mContext);
        mHandler = handler;
    }
    public GsonAdapter(Context context,Handler handler) {
        super();
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mHandler = handler;
    }
    @Override
    public int getCount() {
        return mPersonList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ImageView imageView;
        TextView name;
        TextView age;
        TextView school;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item, parent, false);

            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);


            imageView = (ImageView)convertView.findViewById(R.id.image);
            name = (TextView) convertView.findViewById(R.id.name);
            age = (TextView) convertView.findViewById(R.id.age);
            school = (TextView) convertView.findViewById(R.id.school);
            viewHolder.setImageView(imageView);
            viewHolder.setName(name);
            viewHolder.setSchool(school);
            viewHolder.setAge(age);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            imageView = viewHolder.getImageView();
            name = viewHolder.getName();
            age = viewHolder.getAge();
            school = viewHolder.getSchool();

        }

        Person person = mPersonList.get(position);
        name.setText(person.getName());
        age.setText(person.getAge() + "");
        school.setText(person.getSchoolInfo().getSchoolName());

        new HttpImage(person.getUrl(), mHandler, imageView).start();

        return convertView;
    }
    public void setData(List<Person> persons){
        this.mPersonList = persons;
    }
    public class ViewHolder{
        private ImageView imageView;
        private TextView name;
        private TextView age;
        private TextView school;

        public TextView getAge() {
            return age;
        }

        public void setAge(TextView age) {
            this.age = age;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getSchool() {
            return school;
        }

        public void setSchool(TextView school) {
            this.school = school;
        }
    }
}
