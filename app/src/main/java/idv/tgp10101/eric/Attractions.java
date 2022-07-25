package idv.tgp10101.eric;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

public class Attractions implements Serializable {

    private String name , title , describe,searchpc,des ,imageee;
    private Integer id;
    private Bitmap imagebitmap;
    private int age,ImageResId;
    private List<ImageView> imagelist;
    private List<?> attList;



    public Attractions() {
    }

    public Attractions(String searchpc, String des, Bitmap imagebitmap) {
        this.searchpc = searchpc;
        this.des = des;
        this.imagebitmap = imagebitmap;
    }

    public Attractions(String searchpc, String des, String imageee) {
        this.searchpc = searchpc;
        this.des = des;
        this.imageee = imageee;
    }

    public Attractions(String searchpc, String des, List<Attractions> attList) {
        this.searchpc = searchpc;
        this.des = des;
        this.attList = attList;
    }

    public String getSearchpc() {
        return searchpc;
    }

    public void setSearchpc(String searchpc) {
        this.searchpc = searchpc;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<?> getAttList() {
        return attList;
    }

    public void setAttList(List<Attractions> attList) {
        this.attList = attList;
    }

    public Attractions(String searchpc, String des) {
        this.searchpc = searchpc;
        this.des = des;
    }

    public Attractions(int imageResId) {
        ImageResId = imageResId;
    }

    public int getImageResId() {
        return ImageResId;
    }

    public void setImageResId(int imageResId) {
        ImageResId = imageResId;
    }

    public String getImageee() {
        return imageee;
    }

    public void setImageee(String imageee) {
        this.imageee = imageee;
    }


}
