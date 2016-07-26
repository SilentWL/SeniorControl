package ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seniorcontrol.administrator.imagewall.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.ImageAdapter;
import bean.FolderBean;

public class MainActivity extends Activity implements ListImagePupupWindow.OnDirSelectListener{
    private GridView mGridView;
    private RelativeLayout mBottom;
    private List<String> mImgUrl;
    private ImageAdapter mImageAdapter;

    private TextView mDirName;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();
    private ProgressDialog mProgressBar;

    private ListImagePupupWindow mListImagePupupWindow;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            if (msg.what == 100){
                mProgressBar.dismiss();

                data2View();
            }
        }
    };

    private void data2View() {
        if (mCurrentDir == null){
            Toast.makeText(MainActivity.this, "No Images!", Toast.LENGTH_SHORT).show();
            return;
        }
        mImgUrl = Arrays.asList(mCurrentDir.list());

        if (mImageAdapter == null) {
            mImageAdapter = new ImageAdapter(MainActivity.this, mImgUrl, mCurrentDir.getAbsolutePath());
            mGridView.setAdapter(mImageAdapter);
        }else{
            mImageAdapter.resetImageAdapter(MainActivity.this, mImgUrl, mCurrentDir.getAbsolutePath());
            mImageAdapter.notifyDataSetChanged();
        }

        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridView);
        mBottom = (RelativeLayout) findViewById(R.id.bottom);

        mDirName = (TextView) findViewById(R.id.dirName);
        mDirCount = (TextView) findViewById(R.id.dirCount);

        initDatas();
        initDirPopupWindow();

        mBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImagePupupWindow.showAsDropDown(mBottom, 0, 0);
                mListImagePupupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);

                WindowManager.LayoutParams lp = MainActivity.this.getWindow().getAttributes();
                lp.alpha = 0.3f;
                MainActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    private void initDirPopupWindow() {
        mListImagePupupWindow = new ListImagePupupWindow(MainActivity.this, mFolderBeans);
        mListImagePupupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = MainActivity.this.getWindow().getAttributes();
                lp.alpha = 1.0f;
                MainActivity.this.getWindow().setAttributes(lp);
            }
        });
        mListImagePupupWindow.setOnDirSelectListener(this);

    }

    private void initDatas() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(MainActivity.this, "Unused SD Card!", Toast.LENGTH_SHORT).show();
        }else{
            mProgressBar = ProgressDialog.show(this, "null", "Running...");

            new Thread(){
                @Override
                public void run() {
                    //super.run();
                    Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                    ContentResolver cr = MainActivity.this.getContentResolver();

                    Cursor cursor = cr.query(mImgUri, null, MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                    Set<String> parentPathSet = new HashSet<String>();

                    while (cursor.moveToNext()){
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                        File parentFile = new File(path).getParentFile();

                        if (parentFile == null){
                            continue;
                        }
                        String parentPath = parentFile.getAbsolutePath();

                        if (!parentPathSet.contains(parentPath)){
                            parentPathSet.add(parentPath);
                        }else{
                            continue;
                        }
                        FolderBean folderBean = new FolderBean();
                        folderBean.setDir(parentPath);
                        folderBean.setName(parentPath);
                        folderBean.setFirstImgPath(path);

                        if (parentFile.list() == null){
                            continue;
                        }
                        folderBean.setCount(parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")){
                                    return true;
                                }
                                return false;
                            }
                        }).length);

                        if (folderBean.getCount() > mMaxCount){
                            mMaxCount = folderBean.getCount();
                            mCurrentDir = parentFile;
                        }
                        mFolderBeans.add(folderBean);
                    }
                    cursor.close();
                    mHandler.sendEmptyMessage(100);
                }
            }.start();
        }
    }

    @Override
    public void OnSelect(FolderBean folderbean) {
        mCurrentDir = new File(folderbean.getDir());

        mMaxCount = folderbean.getCount();
        data2View();

    }
}
