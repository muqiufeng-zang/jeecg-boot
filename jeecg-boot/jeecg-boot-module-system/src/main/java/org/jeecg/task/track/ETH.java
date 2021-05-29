package org.jeecg.task.track;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.jeecg.HttpClientUtil;
import org.jeecg.WaybillNotifyStateEn;
import org.jeecg.WaybillStateEn;
import org.jeecg.en.WxMpTemplateMessageEnum;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNoticeHistory;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeHistoryMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/3/18
 */

@Slf4j
@Component
public class ETH extends TrackAbstract {


    //物流跟踪
    @Override
    public void waybillTrack(WaybillInfo waybillInfo){
//        String url = "https://cargo.ethiopianairlines.com/e-cargo/cargotrack/Index/";
//        String html = HttpClientUtil.doPost(url, "utf-8", waybillInfo.getWaybillNo());
//        Document document = Jsoup.parse(html);
    }
}
