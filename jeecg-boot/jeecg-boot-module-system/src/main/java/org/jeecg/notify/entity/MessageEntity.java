package org.jeecg.notify.entity;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/5/23
 */

import lombok.Data;

/**
 * @Description: 消息内容
 * @Author: jeecg-boot
 * @Date:   2021-03-26
 * @Version: V1.0
 */
@Data
public class MessageEntity {

    /**
     * 运单状态
     */
    private String waybillState;

    /**
     * 货代公司
     */
    private String forwarderCompany;

    /**
     * 运单号
     */
    private String waybillNo;

    /**
     * 通知时间
     */
    private String notifyDate;

    /**
     * 通知详情
     */
    private String notifyDetail;

    /**
     * 备注  非必填
     */
    private String remark;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 通知用户openId
     */
    private String openId;

    /**
     * 通知用户mobile
     */
    private String mobile;
}
