package ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.seniorcontrol.administrator.imagewall.R;

import java.util.List;

import bean.FolderBean;
import utils.ImageLoader;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class ListImagePupupWindow extends PopupWindow{
    private int mWidth,mHeight;
    private View mConverView;
    private ListView mListView;
    private List<FolderBean> mDatas;
    private Context mContext;
    private OnDirSelectListener mOnDirSelectListener;


    public ListImagePupupWindow(Context context, List<FolderBean> Datas) {
        //super(contentView);
        this.mContext = context;
        this.mDatas = Datas;

        calcWidthAndHeight(context);
        mConverView = LayoutInflater.from(context).inflate(R.layout.pupup, null);
        setContentView(mConverView);
        setWidth(mWidth);
        setHeight(mHeight);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initViews();
        initEvent();

    }
    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDirSelectListener != null){
                    mOnDirSelectListener.OnSelect(mDatas.get(position));
                    dismiss();
                }
            }
        });
    }

    private void initViews() {
        mListView = (ListView) mConverView.findViewById(R.id.popup);
        mListView.setAdapter(new ListAdapter(mContext, 0, mDatas));
    }

    private void calcWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.5f);
    }

    private class ListAdapter extends ArrayAdapter<FolderBean>{
        private LayoutInflater mInflater;
        private List<FolderBean> mDatas;

        public ListAdapter(Context context, int resource, List<FolderBean> objects) {
            super(context, resource, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_list, parent, false);
                viewHolder.mImage = (ImageView) convertView.findViewById(R.id.item_choice);
                viewHolder.mDirCount = (TextView) convertView.findViewById(R.id.item_count);
                viewHolder.mDirName = (TextView) convertView.findViewById(R.id.item_dirName);
                convertView.setTag(viewHolder);

            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            FolderBean bean = getItem(position);
            viewHolder.mImage.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.getmInstance(ImageLoader.Type.FIFO).loadImage(bean.getFirstImgPath(), viewHolder.mImage);
            viewHolder.mDirName.setText(bean.getName());
            viewHolder.mDirCount.setText(bean.getCount() + "");
            return convertView;
        }

        private class ViewHolder{
            ImageView mImage;
            TextView mDirName;
            TextView mDirCount;
        }
    }

    public interface OnDirSelectListener{
        void OnSelect(FolderBean folderbean);
    }

    public void setOnDirSelectListener(OnDirSelectListener onDirSelectListener){
        mOnDirSelectListener = onDirSelectListener;
    }
}
