package org.jeecg;

import lombok.SneakyThrows;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/2/21
 */
public enum WaybillStateEn {

    DELIVERED_TO_CONSIGNEE(100, "Delivered to consignee");

    private Integer code;

    private String describe;

    WaybillStateEn(Integer code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }

    @SneakyThrows
    public static WaybillStateEn toEnum(String describe) {
        WaybillStateEn[] var = values();
        for (int i = 0; i < var.length; i++) {
            WaybillStateEn en = var[i];
            if (en.describe.equals(describe)) {
                return en;
            }
        }
        throw new Exception("状态类型【" + describe + "】不存在");
    }
}
