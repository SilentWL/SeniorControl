package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.seniorcontrol.administrator.imagewall.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.ImageLoader;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class ImageAdapter extends BaseAdapter{
    private static Set<String> mSelectImg = new HashSet<String>();
    private String mDirPath;
    private List<String> mImgNames;
    private LayoutInflater mLayoutInflater;
    public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
        //super();
        mDirPath = dirPath;
        mImgNames = mDatas;
        mLayoutInflater = LayoutInflater.from(context);
    }
    public void resetImageAdapter(Context context, List<String> mDatas, String dirPath) {
        mDirPath = dirPath;
        mImgNames = mDatas;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mImgNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item_gridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.select);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);



        ImageLoader.getmInstance(3, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + mImgNames.get(position), viewHolder.imageView);
        final String filePath = mDirPath + "/" + mImgNames.get(position);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectImg.contains(filePath)){
                    mSelectImg.remove(filePath);
                    viewHolder.imageView.setColorFilter(null);
                    viewHolder.imageButton.setImageResource(R.mipmap.ic_launcher);
                }else {
                    mSelectImg.add(filePath);
                    viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.imageButton.setImageResource(R.drawable.se);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
        ImageButton imageButton;
    }
}
