package com.pan.cookbookproject.bean;

import java.io.Serializable;

/**
 * Created by pan on 16/7/14.
 */
public class CookBookBean implements Serializable{
    private String name;
    private String seasoning;
    private String method;
    private String remark;
    private int id = -1;

    public CookBookBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeasoning() {
        return seasoning;
    }

    public void setSeasoning(String seasoning) {
        this.seasoning = seasoning;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
