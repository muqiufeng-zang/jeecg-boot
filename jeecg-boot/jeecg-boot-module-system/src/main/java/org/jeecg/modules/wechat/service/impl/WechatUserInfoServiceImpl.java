package org.jeecg.modules.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.modules.customer.entity.CustomerContacts;
import org.jeecg.modules.customer.mapper.CustomerContactsMapper;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.wechat.entity.WechatUserInfo;
import org.jeecg.modules.wechat.mapper.WechatUserInfoMapper;
import org.jeecg.modules.wechat.service.IWechatUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Description: 微信用户表
 * @Author: jeecg-boot
 * @Date: 2021-03-22
 * @Version: V1.0
 */
@Service
public class WechatUserInfoServiceImpl extends ServiceImpl<WechatUserInfoMapper, WechatUserInfo> implements IWechatUserInfoService {

    @Autowired
    private CustomerContactsMapper customerContactsMapper;

    @Autowired
    private WechatUserInfoMapper wechatUserInfoMapper;

    @Override
    @Transactional
    public boolean bindUserMobile(WechatUserInfo wechatUserInfo) {

        LambdaQueryWrapper<WechatUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WechatUserInfo::getAppOpenId, wechatUserInfo.getAppOpenId());
        WechatUserInfo wechatUserInfo1 = wechatUserInfoMapper.selectOne(queryWrapper);
        //老手机号用户设置为未关注状态
        LambdaUpdateWrapper<CustomerContacts> customerContactsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        customerContactsLambdaUpdateWrapper.eq(CustomerContacts::getMobile, wechatUserInfo1.getMobile())
                .set(CustomerContacts::getIsSubscribe, 0);
        customerContactsMapper.update(null, customerContactsLambdaUpdateWrapper);
        //微信用户信息更新新手机号
        LambdaUpdateWrapper<WechatUserInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WechatUserInfo::getAppOpenId, wechatUserInfo.getAppOpenId())
                .set(WechatUserInfo::getMobile, wechatUserInfo.getMobile());
        int updateNum = wechatUserInfoMapper.update(null, updateWrapper);
        //新手机号用户设置为已关注状态
        LambdaUpdateWrapper<CustomerContacts> customerContactsLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
        customerContactsLambdaUpdateWrapper1.eq(CustomerContacts::getMobile, wechatUserInfo.getMobile())
                .set(CustomerContacts::getIsSubscribe, 1);
        customerContactsMapper.update(null, customerContactsLambdaUpdateWrapper1);
        return updateNum > 0;
    }
}
