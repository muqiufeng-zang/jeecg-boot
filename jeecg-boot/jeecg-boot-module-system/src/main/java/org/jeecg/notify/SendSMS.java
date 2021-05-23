package org.jeecg.notify;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.notify.entity.MessageEntity;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/5/23
 */
@Component
public class SendSMS {

    public void send(MessageEntity messageEntity){

        JSONObject templateParamJson = new JSONObject();
        templateParamJson.put("waybillNo", messageEntity.getWaybillNo());
        templateParamJson.put("notifyData", messageEntity.getNotifyDate());
        templateParamJson.put("notifyDetail", messageEntity.getNotifyDetail());
        try {
            DySmsHelper.sendSms(messageEntity.getMobile(), templateParamJson, DySmsEnum.REGISTER_TEMPLATE_CODE);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
