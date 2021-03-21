package org.jeecg.en;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/3/21
 */
public enum WxMpTemplateMessageEnum {

    TRACK("hqpaOVz64YYwA7oJq2Xz6RZWmeirjLa-V8ypd2LtiJ0");

    private String id;

    WxMpTemplateMessageEnum(String id) {
        this.id = id;
    }

    public static WxMpTemplateMessageEnum toNum(String id) throws Exception {
        WxMpTemplateMessageEnum[] var = values();
        for (int i = 0; i < var.length; i++) {
            WxMpTemplateMessageEnum en = var[i];
            if (en.id.equals(id)) {
                return en;
            }
        }
        throw new Exception("方法类型不存在");
    }

    public String getId() {
        return id;
    }

}
