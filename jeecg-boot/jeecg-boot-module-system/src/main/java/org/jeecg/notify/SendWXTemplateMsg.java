package org.jeecg.notify;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.jeecg.en.WxMpTemplateMessageEnum;
import org.jeecg.notify.entity.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/5/23
 */
@Component
public class SendWXTemplateMsg {

    @Autowired
    private WxMpService wxMpService;

    public void send(MessageEntity messageEntity) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        WxMpTemplateData waybillStateWxMpTemplateData = new WxMpTemplateData("first", messageEntity.getWaybillState());
        WxMpTemplateData forwarderCompanyWxMpTemplateData = new WxMpTemplateData("keyword1", messageEntity.getForwarderCompany());
        WxMpTemplateData waybillNoWxMpTemplateData = new WxMpTemplateData("keyword2", messageEntity.getWaybillNo());
        WxMpTemplateData notifyDateWxMpTemplateData = new WxMpTemplateData("keyword3", messageEntity.getNotifyDate());
        WxMpTemplateData notifyDetailWxMpTemplateData = new WxMpTemplateData("keyword4", messageEntity.getNotifyDetail());
        WxMpTemplateData remarkWxMpTemplateData = new WxMpTemplateData("remark", messageEntity.getRemark() == null ? "" : messageEntity.getRemark());
        List<WxMpTemplateData> data = new ArrayList<>();
        data.add(waybillStateWxMpTemplateData);
        data.add(forwarderCompanyWxMpTemplateData);
        data.add(waybillNoWxMpTemplateData);
        data.add(notifyDateWxMpTemplateData);
        data.add(notifyDetailWxMpTemplateData);
        data.add(remarkWxMpTemplateData);
        templateMessage.setData(data);
        templateMessage.setTemplateId(WxMpTemplateMessageEnum.TRACK.getId());
        templateMessage.setToUser(messageEntity.getOpenId());
        templateMessage.setUrl("http://h5.accdaa.com/#/waybill/detail?waybillNo=" + messageEntity.getWaybillNo());
        String msgid = "";
        try {
            msgid = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            System.out.println("发送模板消息id：" + msgid);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
