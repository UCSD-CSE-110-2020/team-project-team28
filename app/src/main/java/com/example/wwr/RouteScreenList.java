package com.example.wwr;

public class RouteScreenList {
    private int image;
    private String routeName;
    private String text2;
    private String text3;
    private String text4;

    public RouteScreenList(int imageResource, String routeName, String text2,
                           String text3, String text4) {
        this.image = imageResource;
        this.routeName = routeName;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
    }

    public int getImage() {
        return this.image;
    }

    public String getRouteName() {
        return this.routeName;
    }

    public String getText2() {
        return this.text2;
    }

    public String getText3() {
        return this.text3;
    }

    public String getText4() {
        return this.text4;
    }
}
