package org.jeecg;

import lombok.SneakyThrows;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/2/21
 */
public enum WaybillNotifyStateEn {

    SUCCESS(1),
    FAIL(0),
    NO_NOTIFY(-1);

    private Integer code;


    WaybillNotifyStateEn(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
