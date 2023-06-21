package com.example.mall.service;

import com.example.mall.controller.vo.*;
import com.example.mall.entity.Order;
import com.example.mall.entity.OrderItem;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;

import java.util.List;
import java.util.Map;

public interface OrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param mallOrder
     * @return
     */
    String updateOrderInfo(Order mallOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(UserVO user, AddressVO addressVO, List<ShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    Order getMallOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    /**
     * 删除订单
     * @param orderNo
     * @param userId
     */
    String deleteOrder(String orderNo, Long userId);

    /**
     * 支付成功
     *
     * @param orderNo
     * @param payType
     * @return
     */
    String paySuccess(String orderNo, int payType);

    /**
     * 获得订单列表
     *
     * @param id
     * @return
     */
    List<OrderItemVO> getOrderItems(Long id);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    OrderItem findOrderItemById(Long id);

    /**
     * 更新
     *
     * @param id
     */
    void updateOrderItem(OrderItem orderItem);


    /**
     * 获得订单状态统计
     *
     * @param userId
     * @return
     */
    Map<String, Object> getStatusCount(Long userId);
}
