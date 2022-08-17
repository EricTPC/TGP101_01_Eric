package idv.tgp10101.eric;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

public class Attractions implements Serializable {
    private Integer id;
    private int ImageResId;
    private String takePic_Uid;                     //UID
    private String takePic_Title;                   //抬頭
    private String takePic_WriterName;              //作者名稱
    private String takePic_Describe,takePic_Des;    //描述
    private String takePic_Web;                     //網址
    private String takePic_Phone;                   //電話
    private String takePic_Address;                 //地址
    private List<String> takePic_PicList;           //圖檔路徑List
    private String takePic_Image;                   //圖檔路徑List的第一張照片
//    private byte[] takePic_Image;                   //圖片儲存


    public Attractions() {
    }


    public Attractions(String takePic_Title, String takePic_Des, List<String> takePic_PicList, String takePic_Image) {
        this.takePic_Title = takePic_Title;
        this.takePic_Des = takePic_Des;
        this.takePic_PicList = takePic_PicList;
        this.takePic_Image = takePic_Image;
    }

    public Attractions(String takePic_Title, String takePic_WriterName, String takePic_Des, List<String> takePic_PicList, String takePic_Image) {
        this.takePic_Title = takePic_Title;
        this.takePic_WriterName = takePic_WriterName;
        this.takePic_Des = takePic_Des;
        this.takePic_PicList = takePic_PicList;
        this.takePic_Image = takePic_Image;
    }

    public List<String> getTakePic_PicList() {
        return takePic_PicList;
    }

    public void setTakePic_PicList(List<String> takePic_PicList) {
        this.takePic_PicList = takePic_PicList;
    }


    public String getTakePic_Uid() {
        return takePic_Uid;
    }

    public void setTakePic_Uid(String takePic_Uid) {
        this.takePic_Uid = takePic_Uid;
    }

    public String getTakePic_Image() {
        return takePic_Image;
    }

    public void setTakePic_Image(String string_Image) {
        this.takePic_Image = string_Image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getImageResId() {
        return ImageResId;
    }

    public void setImageResId(int imageResId) {
        ImageResId = imageResId;
    }

    public String getTakePic_Title() {
        return takePic_Title;
    }

    public void setTakePic_Title(String takePic_Title) {
        this.takePic_Title = takePic_Title;
    }

    public String getTakePic_Name() {
        return takePic_WriterName;
    }

    public void setTakePic_Name(String takePic_Name) {
        this.takePic_WriterName = takePic_Name;
    }

    public String getTakePic_Describe() {
        return takePic_Describe;
    }

    public void setTakePic_Describe(String takePic_Describe) {
        this.takePic_Describe = takePic_Describe;
    }

    public String getTakePic_Des() {
        return takePic_Des;
    }

    public void setTakePic_Des(String takePic_Des) {
        this.takePic_Des = takePic_Des;
    }

    public String getTakePic_Web() {
        return takePic_Web;
    }

    public void setTakePic_Web(String takePic_Web) {
        this.takePic_Web = takePic_Web;
    }

    public String getTakePic_Phone() {
        return takePic_Phone;
    }

    public void setTakePic_Phone(String takePic_Phone) {
        this.takePic_Phone = takePic_Phone;
    }

    public String getTakePic_Address() {
        return takePic_Address;
    }

    public void setTakePic_Address(String takePic_Address) {
        this.takePic_Address = takePic_Address;
    }

}
