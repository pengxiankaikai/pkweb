package com.pk.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dev on 2016/6/20.
 */
public class PengKai implements Serializable{

    private Integer id;
    private String name;
    private Date create_date;

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

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
