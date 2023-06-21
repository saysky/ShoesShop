package com.example.mall.common;

/**

 */
public enum ServiceResultEnum {
    ERROR("error"),

    SUCCESS("success"),

    DATA_NOT_EXIST("未查询到记录！"),

    SAME_CATEGORY_EXIST("有同级同名的分类！"),

    SAME_LOGIN_NAME_EXIST("账号已存在！"),

    LOGIN_NAME_NULL("请输入账号！"),

    NICK_NAME_NULL("请输入姓名！"),

    LOGIN_PASSWORD_NULL("请输入密码！"),

    LOGIN_PASSWORD_IS_OVER_LONG("请输入20位以内的密码！"),

    LOGIN_VERIFY_CODE_NULL("请输入验证码！"),

    LOGIN_VERIFY_CODE_ERROR("验证码错误！"),

    GOODS_NOT_EXIST("商品不存在！"),

    SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("超出单个商品的最大购买数量！"),

    LOGIN_ERROR("登录失败！"),

    LOGIN_USER_LOCKED("用户账号未激活！"),

    ORDER_NOT_EXIST_ERROR("订单不存在！"),

    NULL_ADDRESS_ERROR("地址不能为空！"),

    ORDER_PRICE_ERROR("订单价格异常！"),

    ORDER_GENERATE_ERROR("生成订单异常！"),

    SHOPPING_ITEM_ERROR("购物车数据异常！"),

    SHOPPING_ITEM_COUNT_ERROR("库存不足！"),

    ORDER_STATUS_ERROR("订单状态异常！"),

    OPERATE_ERROR("操作失败！"),

    DB_ERROR("database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
