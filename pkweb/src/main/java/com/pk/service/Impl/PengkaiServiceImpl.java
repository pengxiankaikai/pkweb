package com.pk.service.Impl;

import com.pk.dao.PengkaiMapper;
import com.pk.entity.PengKai;
import com.pk.service.PengkaiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by dev on 2016/7/4.
 */
@Service
public class PengkaiServiceImpl implements PengkaiService {

    @Resource
    private PengkaiMapper pengkaiMapper;

    public void insert(PengKai pk){
        pengkaiMapper.insert(pk);
    }

    public PengKai get(int id){
        return pengkaiMapper.get(id);
    }
}
