package idv.tgp10101.eric;

import java.io.Serializable;

public class MemberUser implements Serializable {
    private String uid;             //會員UID
    private String userloginclass;  //會員登入類別        1 = Firebase註冊 , 2 = google , 3 = Facebook , 4 = Github , 5 = Apple
    private String userimage;       //會員大頭照
    private String username;        //會員名字
    private String email;           //會員信箱
    private String password;        //會員密碼
    private String phone;           //會員手機號碼
    private String address;         //會員地址
    private String online;          //會員是否在線上       1 = 線上 , 2 = 離線
    private String level;           //會員等級          一般 = ordinary , 金 = gold , 白金 = platinum , 鑽石 = diamond ,
    private String vippay;          //會員是否在線上       1 = 付費會員 , 2 = 未付費會員
    private String viplevel;        //VIP會員等級       超級VIP = svip , VIP = vip
    private String usertoken;       //會員Token          ( 預設 null )
    private String adminclass;      //管理員代號          ( 預設 "0"一般會員 "1"版主 "2"管理員 )
    public MemberUser() {
    }

    @Override
    public String toString() {
        return "MemberUser{" +
                "uid='" + uid + '\'' +
                ", userloginclass='" + userloginclass + '\'' +
                ", userimage='" + userimage + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", online='" + online + '\'' +
                ", level='" + level + '\'' +
                ", vippay='" + vippay + '\'' +
                ", viplevel='" + viplevel + '\'' +
                ", usertoken='" + usertoken + '\'' +
                ", adminclass='" + adminclass + '\'' +
                '}';
    }

    public MemberUser(String uid, String userloginclass, String userimage, String username, String email, String password, String phone, String address, String online, String level, String vippay, String viplevel, String usertoken) {
        this.uid = uid;
        this.userloginclass = userloginclass;
        this.userimage = userimage;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.online = online;
        this.level = level;
        this.vippay = vippay;
        this.viplevel = viplevel;
        this.usertoken = usertoken;
    }

    public String getAdminclass() {
        return adminclass;
    }

    public void setAdminclass(String adminclass) {
        this.adminclass = adminclass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getVippay() {
        return vippay;
    }

    public void setVippay(String vippay) {
        this.vippay = vippay;
    }

    public String getViplevel() {
        return viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    public String getUserloginclass() {
        return userloginclass;
    }

    public void setUserloginclass(String userloginclass) {
        this.userloginclass = userloginclass;
    }

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }
}
