package com.pk.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果封装
 * Created by pengkai
 * @date 2016/10/13 0009.
 * @email pengxiankaikai@163.com
 */
public class RetBiz<T extends Serializable> implements Serializable{

    //返回值 SUCCESS,FAIL
    private String retCode;
    //返回信息
    private String msg;
    //返回结果集
    private List<T> results = new ArrayList<T>();
    //返回对象
    private T result;

    public RetBiz(){}

    public RetBiz(String retCode, String msg) {
        this.retCode = retCode;
        this.msg = msg;
    }

    public RetBiz(String retCode){
        if (RetResult.SUCCESS.equals(retCode)){
            this.retCode = RetResult.SUCCESS;
            this.msg = RetResult.MSG_SUCCESS;
        }else {
            this.retCode = RetResult.FAIL;
            this.msg = RetResult.MSG_FAIL;
        }
    }

    public RetBiz(T t){
        this.retCode = RetResult.SUCCESS;
        this.msg = RetResult.MSG_SUCCESS;
        this.result = t;
    }

    public RetBiz(List<T> results){
        this.retCode = RetResult.SUCCESS;
        this.msg = RetResult.MSG_SUCCESS;
        this.results = results;
    }

    public boolean isSuccess(){
        if (RetResult.SUCCESS.equals(retCode)){
            return true;
        }else {
            return false;
        }
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
