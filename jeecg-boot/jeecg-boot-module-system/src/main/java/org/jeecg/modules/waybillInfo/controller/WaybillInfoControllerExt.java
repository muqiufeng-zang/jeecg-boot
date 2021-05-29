package org.jeecg.modules.waybillInfo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.jeecg.modules.customer.entity.CustomerContacts;
import org.jeecg.modules.customer.service.ICustomerContactsService;
import org.jeecg.modules.waybillInfo.entity.*;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jeecg.modules.waybillInfo.service.*;
import org.jeecg.modules.wechat.entity.WechatUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.jeecg.modules.wechat.service.IWechatUserInfoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @Description: 运单信息表
 * @Author: jeecg-boot
 * @Date: 2021-03-18
 * @Version: V1.0
 */
@AllArgsConstructor
@Api(tags = "运单信息表")
@RestController
@RequestMapping("/waybillInfo/waybillInfo/wx")
@Slf4j
public class WaybillInfoControllerExt {
    @Autowired
    private IWaybillInfoService waybillInfoService;
    @Autowired
    private IWaybillNoticeService waybillNoticeService;

    private final WxMpService wxService;

    @Autowired
    private IWechatUserInfoService wechatUserInfoService;

    @Autowired
    private ICustomerContactsService customerContactsService;

    @Autowired
    private WaybillNoticeHistoryMapper waybillNoticeHistoryMapper;

    /**
     * 分页列表查询当前微信用户的运单列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "运单信息表-分页列表查询")
    @ApiOperation(value = "运单信息表-分页列表查询", notes = "运单信息表-分页列表查询")
    @GetMapping(value = "/myList/{code}")
    public Result<?> queryPageList(@PathVariable String code,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        WxOAuth2AccessToken accessToken;
        try {
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return Result.error("获取用户信息失败");
        }
        QueryWrapper<WechatUserInfo> queryWrapper =
                new QueryWrapper<WechatUserInfo>().eq("app_open_id", accessToken.getOpenId());
//                new QueryWrapper<WechatUserInfo>().eq("app_open_id", "dsffdgdgsdfggf");
        WechatUserInfo wechatUserInfo = wechatUserInfoService.getOne(queryWrapper);
        if (null == wechatUserInfo){
            return Result.error(2001,"用户尚未绑定手机号，请前往个人中心绑定手机号！");
        }
        QueryWrapper<CustomerContacts> customerContactsQueryWrapper = new QueryWrapper<>();
        customerContactsQueryWrapper.lambda().eq(CustomerContacts::getMobile, wechatUserInfo.getMobile());
        CustomerContacts customerContacts = customerContactsService.getOne(customerContactsQueryWrapper);
        IPage<WaybillInfo> waybillInfoIPage = new Page<>();
        if (customerContacts == null) {
            return Result.OK(waybillInfoIPage);
        }
        QueryWrapper<WaybillNotice> waybillNoticeQueryWrapper = new QueryWrapper<>();
        waybillNoticeQueryWrapper.lambda().eq(WaybillNotice::getUserId, customerContacts.getId());
        Page<WaybillNotice> page = new Page<>(pageNo, pageSize);
        IPage<WaybillNotice> pageList = waybillNoticeService.page(page, waybillNoticeQueryWrapper);
        log.info("运单通知列表{}", JSON.toJSONString(pageList));
        List<WaybillInfo> records = new ArrayList<>();
        pageList.getRecords().forEach(waybillNotice -> {
            QueryWrapper<WaybillInfo> waybillInfoQueryWrapper = new QueryWrapper<>();
            waybillInfoQueryWrapper.lambda().eq(WaybillInfo::getWaybillNo, waybillNotice.getWaybillNo());
            WaybillInfo waybillInfo = waybillInfoService.getOne(waybillInfoQueryWrapper);
            if (null == waybillInfo) {
                return;
            }
            records.add(waybillInfo);
        });
        log.info("我的运单列表{}", JSON.toJSONString(waybillInfoIPage));
        waybillInfoIPage.setRecords(records);
        waybillInfoIPage.setTotal(pageList.getTotal());
        waybillInfoIPage.setSize(pageList.getSize());
        waybillInfoIPage.setCurrent(pageList.getCurrent());
        waybillInfoIPage.setPages(pageList.getPages());
        return Result.OK(waybillInfoIPage);
    }


    /**
     * 通过waybillNo查询
     *
     * @param waybillNo
     * @return
     */
    @AutoLog(value = "运单通知历史通过运单号查询")
    @ApiOperation(value = "运单通知运单号查询", notes = "运单通知历史-运单号查询")
    @GetMapping(value = "/queryWaybillNoticeHistoryByWaybillNo")
    public Result<?> queryWaybillNoticeHistoryListByMainId(@RequestParam(name = "waybillNo", required = true) String waybillNo) {
        LambdaQueryWrapper<WaybillNoticeHistory> queryWrapper = new QueryWrapper<WaybillNoticeHistory>().lambda()
                .eq(WaybillNoticeHistory::getWaybillNo, waybillNo);
        List<WaybillNoticeHistory> waybillNoticeHistoryList = waybillNoticeHistoryMapper.selectList(queryWrapper);
        Collections.reverse(waybillNoticeHistoryList);
        return Result.OK(waybillNoticeHistoryList);
    }

    /**
     * 通过waybillNo查询
     *
     * @param waybillNo
     * @return
     */
    @AutoLog(value = "运单信息表-通过运单号查询")
    @ApiOperation(value = "运单信息表-通过运单号查询", notes = "运单信息表-通过运单号查询")
    @GetMapping(value = "/queryWaybillInfoByWaybillNo")
    public Result<?> queryWaybillInfoByWaybillNo(@RequestParam(name = "waybillNo", required = true) String waybillNo) {
        LambdaQueryWrapper<WaybillInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WaybillInfo::getWaybillNo, waybillNo);
        WaybillInfo waybillInfo = waybillInfoService.getOne(lambdaQueryWrapper);
        if (waybillInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(waybillInfo);

    }
}
