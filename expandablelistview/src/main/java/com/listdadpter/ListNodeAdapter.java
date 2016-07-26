package com.listdadpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.datahelper.Node;
import com.datahelper.NodesHelper;
import com.seniorcontrol.administrator.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class ListNodeAdapter extends NodesItemAdapter{
    public <T> ListNodeAdapter(ListView listView, Context context, List<T> datas) throws IllegalAccessException {
        super(listView, context, datas);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getItemView(List<Node> mVisiableNodes, ViewGroup parent, int position, View convertView, LayoutInflater layoutInflater) {
        ViewHolder viewHolder;
        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else{
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.listview_item, parent, false);

            viewHolder.id = (TextView) convertView.findViewById(R.id.ViewId);
            viewHolder.tagString = (TextView)convertView.findViewById(R.id.ViewTagString);
            viewHolder.index = (TextView)convertView.findViewById(R.id.ViewIndex);
            convertView.setTag(viewHolder);
        }
        viewHolder.index.setText((position + 1) + "");
        viewHolder.id.setText(mVisiableNodes.get(position).getId() + "");
        viewHolder.tagString.setText(mVisiableNodes.get(position).getTagName() + "");
        convertView.setPadding(30 * (mVisiableNodes.get(position).getNodeLevel() + 1), 20, 0, 0);
        return convertView;
    }

    static class ViewHolder{
        public TextView id;
        public TextView tagString;
        public TextView index;
    }

}
