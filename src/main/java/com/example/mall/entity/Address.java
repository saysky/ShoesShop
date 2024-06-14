package com.example.mall.entity;

import java.io.Serializable;

/**
 * (TAddress)实体类
 *
 * @author makejava
 * @since 2021-03-09 00:38:45
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 127575428998180170L;
    
    private Long id;
    
    private String address;
    
    private String name;
    
    private String phone;
    
    private Integer isDefault;
    
    private Long userId;
    
    private Object createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

