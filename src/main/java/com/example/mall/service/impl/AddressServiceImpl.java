package com.example.mall.service.impl;

import com.example.mall.dao.AddressMapper;
import com.example.mall.entity.Address;
import com.example.mall.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TAddress)表服务实现类
 *
 * @author makejava
 * @since 2021-03-09 00:38:45
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressMapper addressMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Address queryById(Long id) {
        return this.addressMapper.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Address> queryAllByLimit(int offset, int limit) {
        return this.addressMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tAddress 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address insert(Address tAddress) {
        removeIsDefault(tAddress);
        this.addressMapper.insert(tAddress);
        return tAddress;
    }

    /**
     * 修改数据
     *
     * @param tAddress 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address update(Address tAddress) {
        removeIsDefault(tAddress);
        this.addressMapper.update(tAddress);
        return this.queryById(tAddress.getId());
    }

    private void removeIsDefault(Address address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1 && address.getUserId() != null) {
            // 去掉其他的为不是默认
            Address condition = new Address();
            condition.setUserId(address.getUserId());
            condition.setIsDefault(address.getIsDefault());
            List<Address> addressList = addressMapper.queryAll(condition);
            addressList.stream().forEach(p -> {
                p.setIsDefault(0);
                addressMapper.update(p);
            });
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.addressMapper.deleteById(id) > 0;
    }

    @Override
    public List<Address> findByUserId(Long userId) {
        Address address = new Address();
        address.setUserId(userId);
        return addressMapper.queryAll(address);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        Address address = addressMapper.getDefaultAddress(userId);
        if (address == null) {
            List<Address> addressList = this.findByUserId(userId);
            if (addressList != null && addressList.size() > 0) {
                address = addressList.get(0);
            }
        }
        return address;
    }
}