package com.example.mall.service.impl;

import com.example.mall.common.*;
import com.example.mall.controller.vo.*;
import com.example.mall.dao.GoodsMapper;
import com.example.mall.dao.OrderMapper;
import com.example.mall.entity.Goods;
import com.example.mall.entity.Order;
import com.example.mall.entity.OrderItem;
import com.example.mall.entity.StockNumDTO;
import com.example.mall.util.BeanUtil;
import com.example.mall.util.NumberUtil;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;
import com.example.mall.dao.OrderItemMapper;
import com.example.mall.dao.ShoppingCartItemMapper;
import com.example.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper mallOrderMapper;
    @Autowired
    private OrderItemMapper mallOrderItemMapper;
    @Autowired
    private ShoppingCartItemMapper mallShoppingCartItemMapper;
    @Autowired
    private GoodsMapper mallGoodsMapper;

    @Override
    public PageResult getMallOrdersPage(PageQueryUtil pageUtil) {
        List<Order> mallOrders = mallOrderMapper.findmallOrderList(pageUtil);
        int total = mallOrderMapper.getTotalmallOrders(pageUtil);
        PageResult pageResult = new PageResult(mallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(Order mallOrder) {
        Order temp = mallOrderMapper.selectByPrimaryKey(mallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(mallOrder.getTotalPrice());
            temp.setUserAddress(mallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<Order> orders = mallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (Order mallOrder : orders) {
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                if (mallOrder.getOrderStatus() != 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (mallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<Order> orders = mallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (Order mallOrder : orders) {
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                if (mallOrder.getOrderStatus() != 1 && mallOrder.getOrderStatus() != 2) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (mallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<Order> orders = mallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (Order mallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (mallOrder.getOrderStatus() == 4 || mallOrder.getOrderStatus() < 0) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (mallOrderMapper.closeOrder(Arrays.asList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(UserVO user, AddressVO addressVO, List<ShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(ShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(ShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<Goods> mallGoods = mallGoodsMapper.selectByPrimaryKeys(goodsIds);
        Map<Long, Goods> mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!mallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > mallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(mallGoods)) {
            if (mallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = mallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                Order mallOrder = new Order();
                mallOrder.setOrderNo(orderNo);
                mallOrder.setUserId(user.getUserId());
                mallOrder.setUserAddress(addressVO != null ? addressVO.getAddress() : user.getAddress());
                mallOrder.setUserName(addressVO != null ? addressVO.getNickName() : user.getNickName());
                mallOrder.setUserPhone(addressVO != null ? addressVO.getPhone() : user.getPhone());
                mallOrder.setRemark(addressVO != null && !StringUtils.isEmpty(addressVO.getRemark()) ? addressVO.getRemark() : "");
                //总价
                for (ShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                mallOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                mallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (mallOrderMapper.insertSelective(mallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<OrderItem> mallOrderItems = new ArrayList<>();
                    for (ShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
                        OrderItem mallOrderItem = new OrderItem();
                        //使用BeanUtil工具类将mallShoppingCartItemVO中的属性复制到mallOrderItem对象中
                        BeanUtil.copyProperties(mallShoppingCartItemVO, mallOrderItem);
                        //mallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        mallOrderItem.setOrderId(mallOrder.getOrderId());
                        mallOrderItem.setIsComment(0);
                        mallOrderItems.add(mallOrderItem);
                    }
                    //保存至数据库
                    if (mallOrderItemMapper.insertBatch(mallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        Order mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null && Objects.equals(mallOrder.getUserId(), userId)) {
            List<OrderItem> orderItems = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<OrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, OrderItemVO.class);
                OrderDetailVO mallOrderDetailVO = new OrderDetailVO();
                BeanUtil.copyProperties(mallOrder, mallOrderDetailVO);
                mallOrderDetailVO.setOrderStatusString(OrderStatusEnum.getMallOrderStatusEnumByStatus(mallOrderDetailVO.getOrderStatus()).getName());
                mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrderDetailVO.getPayType()).getName());
                mallOrderDetailVO.setMallOrderItemVOS(mallOrderItemVOS);
                Integer goodsCount = mallOrderItemVOS.stream().mapToInt(OrderItemVO::getGoodsCount).sum();
                mallOrderDetailVO.setGoodsCount(goodsCount);
                return mallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public Order getMallOrderByOrderNo(String orderNo) {
        return mallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = mallOrderMapper.getTotalmallOrders(pageUtil);
        List<Order> mallOrders = mallOrderMapper.findmallOrderList(pageUtil);
        List<OrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(mallOrders, OrderListVO.class);
            //设置订单状态中文显示值
            for (OrderListVO mallOrderListVO : orderListVOS) {
                mallOrderListVO.setOrderStatusString(OrderStatusEnum.getMallOrderStatusEnumByStatus(mallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = mallOrders.stream().map(Order::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<OrderItem> orderItems = mallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<OrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(OrderItem::getOrderId));
                for (OrderListVO mallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(mallOrderListVO.getOrderId())) {
                        List<OrderItem> orderItemListTemp = itemByOrderIdMap.get(mallOrderListVO.getOrderId());
                        //将mallOrderItem对象列表转换成mallOrderItemVO对象列表
                        List<OrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, OrderItemVO.class);
                        mallOrderListVO.setMallOrderItemVOS(mallOrderItemVOS);
                        Integer goodsCount = mallOrderItemVOS.stream().mapToInt(OrderItemVO::getGoodsCount).sum();
                        mallOrderListVO.setGoodsCount(goodsCount);
                    }

                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        Order mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null && Objects.equals(mallOrder.getUserId(), userId)) {
            if (mallOrderMapper.closeOrder(Collections.singletonList(mallOrder.getOrderId()), OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        Order mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null && Objects.equals(mallOrder.getUserId(), userId)) {
            mallOrder.setOrderStatus( OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String deleteOrder(String orderNo, Long userId) {
        Order mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null && Objects.equals(mallOrder.getUserId(), userId)) {
            if (mallOrderMapper.deleteByPrimaryKey(mallOrder.getOrderId()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        Order mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            mallOrder.setOrderStatus( OrderStatusEnum.OREDER_PAID.getOrderStatus());
            mallOrder.setPayType( payType);
            mallOrder.setPayStatus( PayStatusEnum.PAY_SUCCESS.getPayStatus());
            mallOrder.setPayTime(new Date());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }


    @Override
    public List<OrderItemVO> getOrderItems(Long id) {
        Order mallOrder = mallOrderMapper.selectByPrimaryKey(id);
        if (mallOrder != null) {
            List<OrderItem> orderItems = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<OrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, OrderItemVO.class);
                return mallOrderItemVOS;
            }
        }
        return null;
    }

    @Override
    public OrderItem findOrderItemById(Long id) {
        return mallOrderItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        mallOrderItemMapper.updateByPrimaryKeySelective(orderItem);
    }

    @Override
    public Map<String, Object> getStatusCount(Long userId) {
        PageQueryUtil pageUtil = new PageQueryUtil();
        pageUtil.put("userId", userId);
        pageUtil.put("isDeleted", 0);

        // 总数
        int total = mallOrderMapper.getTotalmallOrders(pageUtil);

        // 待付款数
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.ORDER_PRE_PAY.getOrderStatus());
        pageUtil.put("orderStatusList", statusList);
        int toPay = mallOrderMapper.getTotalmallOrders(pageUtil);

        // 待发货数
        List<Integer> statusList2 = new ArrayList<>();
        statusList2.add(OrderStatusEnum.OREDER_PAID.getOrderStatus());
        statusList2.add(OrderStatusEnum.OREDER_PACKAGED.getOrderStatus());
        pageUtil.put("orderStatusList", statusList2);
        int toDelivery = mallOrderMapper.getTotalmallOrders(pageUtil);

        // 待收货
        List<Integer> statusList3 = new ArrayList<>();
        statusList3.add(OrderStatusEnum.OREDER_EXPRESS.getOrderStatus());
        pageUtil.put("orderStatusList", statusList3);
        int toReceive = mallOrderMapper.getTotalmallOrders(pageUtil);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("toPay", toPay);
        resultMap.put("toDelivery", toDelivery);
        resultMap.put("toReceive", toReceive);
        return resultMap;
    }
