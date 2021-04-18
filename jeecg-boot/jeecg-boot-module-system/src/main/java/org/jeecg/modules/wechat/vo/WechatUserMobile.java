package org.jeecg.modules.wechat.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/4/19
 */
@Data
public class WechatUserMobile {

    //用户openId
    private String openId;

    //用户微信绑定手机号
    private String mobile;
}
