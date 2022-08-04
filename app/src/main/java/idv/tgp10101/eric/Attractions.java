package idv.tgp10101.eric;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

public class Attractions implements Serializable {
    private List<?> attList;
    private String imageee;         //圖檔路徑
    private Integer id;
    private int ImageResId;
    private String searchpc;        //專案名稱
    private String title;           //抬頭
    private String name;            //名稱
    private int age;                //年齡
    private String describe,des;    //描述
    private String web;             //網址
    private String phone;           //電話
    private String address;         //地址
    private byte[] image;           //圖片儲存


    public Attractions() {
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

    public Attractions(String title, byte[] image) {
        this.title = title;
        this.image = image;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
