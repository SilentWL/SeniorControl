package bean;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class FolderBean {

    private String dir;
    private String firstImgPath;
    private String name;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;



    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String dir){
        this.name = dir.substring(dir.lastIndexOf("/") + 1);
    }
}
