package com.listdadpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.datahelper.Node;
import com.datahelper.NodesHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public abstract class NodesItemAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private Context mContext = null;
    private NodesHelper mNodesHelper = null;
    private List<Node> mNodes = null;
    private List<Node> mVisiableNodes = null;
    private LayoutInflater mLayoutInflater = null;
    private ListView mListView = null;


    public <T>NodesItemAdapter(ListView listView, Context context, List<T> datas) throws IllegalAccessException {
        super();
        mContext = context;
        mNodesHelper = NodesHelper.newInstance();
        mNodes = mNodesHelper.convertDataToNodes(datas);
        mVisiableNodes = mNodesHelper.listVisiableNodesFromRootNodes(mNodesHelper.listRootNodes(mNodes));
        mLayoutInflater = LayoutInflater.from(context);
        mListView = listView;
        mListView.setOnItemClickListener(this);
    }

    @Override
    public int getCount() {
        return mVisiableNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisiableNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mVisiableNodes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(mVisiableNodes, parent, position, convertView, mLayoutInflater);
    }

    public abstract View getItemView(List<Node> mVisiableNodes, ViewGroup parent, int position, View convertView, LayoutInflater layoutInflater);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (!mVisiableNodes.get(position).isLeaf()) {
            mVisiableNodes.get(position).setExpandable(!mVisiableNodes.get(position).isExpandable());
            mVisiableNodes = mNodesHelper.listVisiableNodesFromRootNodes(mNodesHelper.listRootNodes(mNodes));
            notifyDataSetChanged();
        }
    }

    public void addNode(int position, String text){
        Node newNode = new Node(mNodesHelper.obtainAvailableId(mNodesHelper.listRootNodes(mNodes)), mVisiableNodes.get(position).getId(), text);
        mNodes.add(newNode);
        mNodesHelper.buildNodes(mNodes);
        newNode.setExpandable(true);
        mVisiableNodes = mNodesHelper.listVisiableNodesFromRootNodes(mNodesHelper.listRootNodes(mNodes));
        notifyDataSetChanged();
    }
}
