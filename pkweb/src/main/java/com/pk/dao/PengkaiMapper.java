package com.pk.dao;


import com.pk.entity.PengKai;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by dev on 2016/6/20.
 */
public interface PengkaiMapper {

    public void insert(PengKai pk);

    public PengKai get(int id);
}
