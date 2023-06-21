package com.example.mall.controller.vo;

import java.io.Serializable;

/**
 * @author 言曌
 * @date 2021/2/28 11:07 上午
 */

public class AddressVO implements Serializable {

    private String nickName;
    private String phone;
    private String address;
    private String remark;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
