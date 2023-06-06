package com.cdac.uphmis.home;

import android.graphics.drawable.Drawable;

public class PatientHomeFragmentModel {
    private String id,name,color;
    private  Drawable drawable;

    public PatientHomeFragmentModel(String id, String name, Drawable drawable, String color) {
        this.id = id;
        this.name = name;
        this.drawable = drawable;
        this.color=color;
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

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return id;
    }
}
