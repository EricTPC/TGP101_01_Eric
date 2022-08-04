package idv.tgp10101.eric;

import java.io.Serializable;

public class Spot implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String web;
    private String imagePath;

    public Spot() {

    }
    public Spot(String id, String name, String phone, String address, String web) {
        this(id, name, phone, address, web, null);
    }

    public Spot(String id, String name, String phone, String address, String web, String imagePath) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.web = web;
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
