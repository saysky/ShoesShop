package com.example.mall.dao;

import com.example.mall.entity.Address;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (TAddress)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-09 00:38:45
 */
public interface AddressMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Address queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Address> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tAddress 实例对象
     * @return 对象列表
     */
    List<Address> queryAll(Address tAddress);

    /**
     * 新增数据
     *
     * @param tAddress 实例对象
     * @return 影响行数
     */
    int insert(Address tAddress);

    /**
     * 修改数据
     *
     * @param tAddress 实例对象
     * @return 影响行数
     */
    int update(Address tAddress);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 获得默认地址
     *
     * @param userId
     * @return
     */
    Address getDefaultAddress(Long userId);

