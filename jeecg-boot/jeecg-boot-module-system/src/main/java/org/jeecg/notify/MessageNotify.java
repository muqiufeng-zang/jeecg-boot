package org.jeecg.notify;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.en.WxMpTemplateMessageEnum;
import org.jeecg.modules.customer.entity.CustomerContacts;
import org.jeecg.modules.customer.service.ICustomerContactsService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.service.IWaybillInfoService;
import org.jeecg.modules.waybillInfo.service.IWaybillNoticeService;
import org.jeecg.modules.wechat.entity.WechatUserInfo;
import org.jeecg.modules.wechat.service.IWechatUserInfoService;
import org.jeecg.notify.entity.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/5/23
 */
@Slf4j
@Component
public class MessageNotify {

    @Autowired
    private ICustomerContactsService customerContactsService;
    @Autowired
    private IWaybillNoticeService waybillNoticeService;
    @Autowired
    private IWaybillInfoService waybillInfoService;
    @Autowired
    private IWechatUserInfoService wechatUserInfoService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private SendWXTemplateMsg sendWXTemplateMsg;
    @Autowired
    private SendSMS sendSMS;

    public Boolean toNotify(String waybillState, String waybillNo, String notifyDate, String notifyDetail, String remark) {
        AtomicBoolean notifyResult = new AtomicBoolean(true);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setWaybillState(waybillState);
        messageEntity.setWaybillNo(waybillNo);
        messageEntity.setNotifyDate(notifyDate);
        messageEntity.setNotifyDetail(notifyDetail);
        messageEntity.setRemark(remark);
        messageEntity.setTemplateId(WxMpTemplateMessageEnum.TRACK.getId());
        //查询运单所属货代公司
        LambdaQueryWrapper<WaybillInfo> waybillInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        waybillInfoLambdaQueryWrapper.eq(WaybillInfo::getWaybillNo, waybillNo);
        WaybillInfo waybillInfo = waybillInfoService.getOne(waybillInfoLambdaQueryWrapper);
        LambdaQueryWrapper<SysDepart> sysDepartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDepartLambdaQueryWrapper.eq(SysDepart::getOrgCode, waybillInfo.getSysOrgCode());
        SysDepart sysDepart = sysDepartService.getOne(sysDepartLambdaQueryWrapper);
        messageEntity.setForwarderCompany(sysDepart.getDepartName());
        //根据通知类型选择不同的推送渠道
        LambdaQueryWrapper<WaybillNotice> waybillNoticeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        waybillNoticeLambdaQueryWrapper.eq(WaybillNotice::getWaybillNo, messageEntity.getWaybillNo());
        List<WaybillNotice> waybillNoticeList = waybillNoticeService.list(waybillNoticeLambdaQueryWrapper);
        log.info("运单【{}】的通知人员为：{}", waybillNo, JSON.toJSONString(waybillNoticeList));
        waybillNoticeList.forEach(waybillNotice -> {
            CustomerContacts customerContacts = customerContactsService.getById(waybillNotice.getUserId());
            messageEntity.setMobile(customerContacts.getMobile());
//                sendSMS.send(messageEntity);
            log.info("运单【{}】的通知人员【{}】的通知类型为：{}", waybillNo, waybillNotice.getUserId(), waybillNotice.getNotifyType());
            if (waybillNotice.getNotifyType() > 1) {
                LambdaQueryWrapper<WechatUserInfo> wechatUserInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                wechatUserInfoLambdaQueryWrapper.eq(WechatUserInfo::getMobile, customerContacts.getMobile());
                List<WechatUserInfo> wechatUserInfoList = wechatUserInfoService.list(wechatUserInfoLambdaQueryWrapper);
                wechatUserInfoList.forEach(wechatUserInfo -> {
                    messageEntity.setOpenId(wechatUserInfo.getAppOpenId());
                    String msgId = sendWXTemplateMsg.send(messageEntity);
                    if (null == msgId) {
                        notifyResult.set(false);
                        log.info("运单【{}】的通知人员【{}】的微信通知失败！", waybillNo, waybillNotice.getUserId());
                    }
                });
            }
        });
        return notifyResult.get();
    }
}
