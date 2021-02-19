package org.jeecg.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.internal.platform.OpenJSSEPlatform;
import org.jeecg.HttpClientUtil;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.waybillInfo.entity.WaybillInfo;
import org.jeecg.modules.waybillInfo.entity.WaybillNotice;
import org.jeecg.modules.waybillInfo.mapper.WaybillInfoMapper;
import org.jeecg.modules.waybillInfo.mapper.WaybillNoticeMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: qiufeng
 * @Date: Created in 2021/2/19
 */

@Slf4j
@Component
public class WaybillNoticeTask {

    @Resource
    private WaybillNoticeMapper waybillNoticeMapper;

    @Resource
    private WaybillInfoMapper waybillInfoMapper;

    /**
     * 每隔5秒一次
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void notice() {

        QueryWrapper<WaybillInfo> queryWrapper = new QueryWrapper<WaybillInfo>()
                .between("waybill_sate", 5, 100);

        List<WaybillInfo> waybillInfos = waybillInfoMapper.selectList(queryWrapper);

        waybillInfos.forEach(waybillInfo -> {
            String url = "https://cargoserv.champ.aero/trace/trace.asp";
            String html = HttpClientUtil.doGet(url, "utf-8", "072-70872163");
            Document document = Jsoup.parse(html);
            System.out.println(document.getElementsByTag("title").first());
            Elements detail = document.getElementsByClass("detail");
            for (Element element : detail) {
                Elements trs = element.select("tr");
                for (Element tr : trs) {
                    Elements tds = tr.select("td");
                    System.out.println("内容："+tds.get(0).text()+"时间："+tds.get(1).text());
                }
            }
        });
        log.info("运输中的运单：" + JSON.toJSONString(waybillInfos));
    }

}
