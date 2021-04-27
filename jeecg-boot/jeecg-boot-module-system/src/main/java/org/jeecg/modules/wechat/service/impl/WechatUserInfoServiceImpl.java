package org.jeecg.modules.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.wechat.entity.WechatUserInfo;
import org.jeecg.modules.wechat.mapper.WechatUserInfoMapper;
import org.jeecg.modules.wechat.service.IWechatUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    private WechatUserInfoMapper wechatUserInfoMapper;

    @Override
    public boolean bindUserMobile(WechatUserInfo wechatUserInfo) {
        LambdaUpdateWrapper<WechatUserInfo> updateWrapper = new UpdateWrapper<WechatUserInfo>().lambda();
        updateWrapper.set(WechatUserInfo::getAppOpenId, wechatUserInfo.getAppOpenId());
        int updateNum = wechatUserInfoMapper.update(wechatUserInfo, updateWrapper);
        return updateNum > 0;
    }
}
