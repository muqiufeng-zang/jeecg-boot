package org.jeecg.wx.mp.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.entity.SysThirdAccount;
import org.jeecg.modules.wechat.entity.WechatUserInfo;
import org.jeecg.modules.wechat.mapper.WechatUserInfoMapper;
import org.jeecg.wx.mp.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Resource
    private WechatUserInfoMapper wechatUserInfoMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = weixinService.getUserService()
                    .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                // TODO 可以添加关注用户到本地数据库
                LambdaQueryWrapper<WechatUserInfo> query = new LambdaQueryWrapper<>();
                query.eq(WechatUserInfo::getAppOpenId, userWxInfo.getOpenId());
                WechatUserInfo oldWechatUserInfo = wechatUserInfoMapper.selectOne(query);
                if (null != oldWechatUserInfo) {
                    LambdaUpdateWrapper<WechatUserInfo> updateWrapper = new UpdateWrapper<WechatUserInfo>()
                            .lambda()
                            .eq(WechatUserInfo::getAppOpenId, userWxInfo.getOpenId())
                            .set(WechatUserInfo::getSubscribeState, 1);
                    wechatUserInfoMapper.update(new WechatUserInfo(), updateWrapper);
                    this.logger.info("老用户重新关注 OPENID: " + wxMessage.getFromUser());
                }else {
                    WechatUserInfo wechatUserInfo = new WechatUserInfo();
                    wechatUserInfo.setNickName(userWxInfo.getNickname());
                    wechatUserInfo.setAvatar(userWxInfo.getHeadImgUrl());
                    wechatUserInfo.setUnionId(userWxInfo.getUnionId());
                    wechatUserInfo.setAppOpenId(userWxInfo.getOpenId());
                    wechatUserInfo.setSubscribeOpenId(wxMessage.getToUser());
                    wechatUserInfo.setSubscribeState(userWxInfo.getSubscribe() ? 1 : 0);
                    wechatUserInfo.setLanguage(userWxInfo.getLanguage());
                    wechatUserInfo.setCity(userWxInfo.getCity());
                    wechatUserInfo.setSex(userWxInfo.getSex());
                    wechatUserInfo.setSexdesc(userWxInfo.getSexDesc());
                    wechatUserInfo.setProvince(userWxInfo.getProvince());
                    wechatUserInfo.setCountry(userWxInfo.getCountry());
                    wechatUserInfo.setRemark(userWxInfo.getRemark());
                    wechatUserInfo.setSubscribetime(userWxInfo.getSubscribeTime().toString());
                    wechatUserInfo.setGroupid(userWxInfo.getGroupId());
                    wechatUserInfo.setTagids(Arrays.toString(userWxInfo.getTagIds()));
                    wechatUserInfo.setSubscribescene(userWxInfo.getSubscribeScene());
                    wechatUserInfo.setQrscene(userWxInfo.getQrScene());
                    wechatUserInfo.setQrscenestr(userWxInfo.getQrSceneStr());
                    wechatUserInfoMapper.insert(wechatUserInfo);
                    this.logger.info("保存新用户信息 OPENID: " + wxMessage.getFromUser());
                }
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        }


        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = this.handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("感谢关注", wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
            throws Exception {
        //TODO
        return null;
    }

}
