package idv.tgp10101.eric;

import java.io.Serializable;

public class Friend implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String web;
    private String imagePath;


    public Friend() {

    }

    public Friend(String id, String name, String phone, String email, String web) {
        this(id, name, phone, email, web, null);
    }

    public Friend(String id, String name, String phone, String email, String web, String imagePath) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.web = web;
        this.imagePath = imagePath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
