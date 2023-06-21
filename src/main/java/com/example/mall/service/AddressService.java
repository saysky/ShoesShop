package com.example.mall.service;

import com.example.mall.entity.Address;

import java.util.List;

/**
 * (TAddress)表服务接口
 *
 * @author makejava
 * @since 2021-03-09 00:38:45
 */
public interface AddressService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Address queryById(Long id);



    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Address> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tAddress 实例对象
     * @return 实例对象
     */
    Address insert(Address tAddress);

    /**
     * 修改数据
     *
     * @param tAddress 实例对象
     * @return 实例对象
     */
    Address update(Address tAddress);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 根据用户查询
     *
     * @param userId
     * @return
     */
    List<Address> findByUserId(Long userId);

    /**
     * 获得默认地址
     *
     * @param userId
     * @return
     */
    Address getDefaultAddress(Long userId);

}