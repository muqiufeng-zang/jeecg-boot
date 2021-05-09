package org.jeecg.modules.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.customer.service.ICustomerContactsService;
import org.jeecg.modules.wechat.entity.WechatUserInfo;
import org.jeecg.modules.wechat.service.IWechatUserInfoService;
import org.jeecg.modules.wechat.vo.WechatUserMobile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 微信用户表
 * @Author: jeecg-boot
 * @Date: 2021-03-22
 * @Version: V1.0
 */
@AllArgsConstructor
@Api(tags = "微信用户表")
@RestController
@RequestMapping("/wechat/wechatUserInfo")
@Slf4j
public class WechatUserInfoControllerExt extends JeecgController<WechatUserInfo, IWechatUserInfoService> {
    @Autowired
    private IWechatUserInfoService wechatUserInfoService;

    private final WxMpService wxService;


    /**
     * 绑定用户手机号
     *
     * @param wechatUserInfo 用户信息
     * @return
     */
    @RequestMapping(value = "/bindMobile", method = RequestMethod.POST)
    public Result<?> bindMobile(@RequestBody WechatUserInfo wechatUserInfo) {
        boolean result = wechatUserInfoService.bindUserMobile(wechatUserInfo);
        if (result){
            return Result.OK("绑定成功!");
        }
        return Result.error("绑定失败!");
    }

    /**
     * 分页列表查询
     *
     * @param code 前端身份code
     * @return
     */
    @AutoLog(value = "获取用户的微信绑定手机号信息")
    @ApiOperation(value = "获取用户的微信绑定手机号信息", notes = "获取用户的微信绑定手机号信息")
    @GetMapping(value = "/userMobile/{code}")
    public Result<?> queryUserMobile(@PathVariable String code) {
        WxOAuth2AccessToken accessToken;
        try {
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return Result.error("获取用户信息失败");
        }
        QueryWrapper<WechatUserInfo> queryWrapper =
                new QueryWrapper<WechatUserInfo>().eq("app_open_id", accessToken.getOpenId());
        WechatUserInfo wechatUserInfo = wechatUserInfoService.getOne(queryWrapper);
        WechatUserMobile wechatUserMobile = new WechatUserMobile();
        wechatUserMobile.setMobile(wechatUserInfo.getMobile());
        wechatUserMobile.setAppOpenId(wechatUserInfo.getAppOpenId());
        return Result.OK(wechatUserMobile);
    }
}
