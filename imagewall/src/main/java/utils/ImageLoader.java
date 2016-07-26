package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    private LruCache<String, Bitmap> mLruCache;
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;

    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    private Handler mUIHandler;
    private LinkedList<Runnable> mTaskQueue;

    private static int mThreadCount = DEFAULT_THREAD_COUNT;

    public enum Type{
        FIFO, LIFO;
    }
    private static Type mType = Type.LIFO;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;

    private ImageLoader(int ThreadCount, Type type){
        initImageLoaderThread(ThreadCount, type);

    }

    private void initImageLoaderThread(int threadCount, Type type) {
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //super.handleMessage(msg);
                        mThreadPool.execute(getTask());


                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        mLruCache = new LruCache<String, Bitmap>((int)Runtime.getRuntime().maxMemory() / 8){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        mThreadCount = threadCount;
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    private Runnable getTask() {
        if (mType == Type.FIFO){
            return mTaskQueue.removeFirst();
        }else if (mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    public static ImageLoader getmInstance(Type type){
        mType = type;
        if (mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader(mThreadCount, mType);
                }
            }
        }
        return mInstance;
    }
    public static ImageLoader getmInstance(int threadCount, Type type){
        mType = type;
        if (mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);

        if (mUIHandler == null){
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    ImageBeanHolder imageInfo = (ImageBeanHolder) msg.obj;

                    if (imageInfo != null && imageInfo.imageView != null && imageInfo.image != null) {
                        if (imageInfo.imageView.getTag().equals(imageInfo.path)) {
                            imageInfo.imageView.setImageBitmap(imageInfo.image);
                        }
                    }


                }
            };
        }

        Bitmap bitmap = getBitmapFromLruCache(path);

        if (bitmap != null){
            SetImageToUI(path, imageView, bitmap);
        }else {
            addTask(new Runnable(){
                @Override
                public void run() {
                    ImageSize imageSize = getImageViewSize(imageView);
                    Bitmap bitmap = decodeBitmapFromPath(path, imageSize);

                    addBitmapToLruCache(path, bitmap);
                    SetImageToUI(path, imageView, bitmap);
                    mSemaphoreThreadPool.release();
                }
            });
        }
    }

    private void SetImageToUI(String path, ImageView imageView, Bitmap bitmap) {
        Message msg = Message.obtain();

        msg.obj = new ImageBeanHolder(bitmap, imageView, path);
        mUIHandler.sendMessage(msg);
    }

    private void addBitmapToLruCache(String path, Bitmap bitmap) {
        if (getBitmapFromLruCache(path) == null){
            if (bitmap != null){
                mLruCache.put(path,bitmap);
            }
        }
    }

    private Bitmap decodeBitmapFromPath(String path, ImageSize imageSize){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, imageSize.width, imageSize.height);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);

    }

    private int caculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int tempWidth = options.outWidth;
        int tempHeight = options.outHeight;

        int inSampleSize = 1;
        if (tempWidth > width || tempHeight > height){
            int widthRadio = Math.round(tempWidth * 1.0f / width);
            int heightRadio = Math.round(tempHeight * 1.0f / height);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;

    }

    private ImageSize getImageViewSize(ImageView imageView){

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        int width = imageView.getWidth();

        if (width <= 0){
            width = lp.width;
        }

        if (width <= 0){
            width = imageView.getMaxWidth();
        }

        if (width <= 0){
            width = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();

        if (height <= 0){
            height = lp.width;
        }

        if (height <= 0){
            height = imageView.getMaxHeight();
        }

        if (height <= 0){
            height = displayMetrics.heightPixels;
        }

        return new ImageSize(width, height);
    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);

        try {
            if (mPoolThreadHandler == null) {
                mSemaphorePoolThreadHandler.acquire();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(100);
    }

    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    private class ImageBeanHolder{
        public Bitmap image;
        public String path;
        public ImageView imageView;

        public ImageBeanHolder(Bitmap image, ImageView imageView, String path) {
            this.image = image;
            this.imageView = imageView;
            this.path = path;
        }
    }

    private class ImageSize {
        int width;
        int height;

        public ImageSize(int width,int  height) {
            this.height = height;
            this.width = width;
        }
    }
}
