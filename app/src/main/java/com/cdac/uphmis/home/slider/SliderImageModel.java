package com.cdac.uphmis.home.slider;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class SliderImageModel {

    private Drawable mUrl ;
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SliderImageModel(Drawable mUrl,String id) {
        this.mUrl = mUrl;
        this.id=id;
    }



    public Drawable getmUrl() {
        return mUrl;
    }

    public void setmUrl(Drawable mUrl) {
        this.mUrl = mUrl;
    }

}
