package com.example.heroPetShop.sildeshow;
public class Image {
    private String id;
    private String url;

    public Image() {}

    public Image(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
