package org.jeecg.modules.wechat.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 微信用户表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Data
@TableName("wechat_user_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wechat_user_info对象", description="微信用户表")
public class WechatUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private String mobile;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private String email;
	/**微信昵称*/
	@Excel(name = "微信昵称", width = 15)
    @ApiModelProperty(value = "微信昵称")
    private String nickName;
	/**固定号码*/
	@Excel(name = "固定号码", width = 15)
    @ApiModelProperty(value = "固定号码")
    private String telephone;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String avatar;
	/**用户状态*/
	@Excel(name = "用户状态", width = 15)
    @ApiModelProperty(value = "用户状态")
    private Integer userStatus;
	/**微信端id*/
	@Excel(name = "微信端id", width = 15)
    @ApiModelProperty(value = "微信端id")
    private String wxAppId;
	/**0 微信；1 小程序*/
	@Excel(name = "0 微信；1 小程序", width = 15)
    @ApiModelProperty(value = "0 微信；1 小程序")
    private Integer authType;
	/**微信union_id*/
	@Excel(name = "微信union_id", width = 15)
    @ApiModelProperty(value = "微信union_id")
    private String unionId;
	/**微信open_id*/
	@Excel(name = "微信open_id", width = 15)
    @ApiModelProperty(value = "微信open_id")
    private String appOpenId;
	/**公众号的open ID*/
	@Excel(name = "公众号的open ID", width = 15)
    @ApiModelProperty(value = "公众号的open ID")
    private String subscribeOpenId;
	/**用户是否关注公众号,1-关注,0,取消关注*/
	@Excel(name = "用户是否关注公众号,1-关注,0,取消关注", width = 15)
    @ApiModelProperty(value = "用户是否关注公众号,1-关注,0,取消关注")
    private Integer subscribeState;
	/**语言*/
	@Excel(name = "语言", width = 15)
    @ApiModelProperty(value = "语言")
    private String language;
	/**城市*/
	@Excel(name = "城市", width = 15)
    @ApiModelProperty(value = "城市")
    private String city;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private Integer sex;
	/**性别描述信息*/
	@Excel(name = "性别描述信息", width = 15)
    @ApiModelProperty(value = "性别描述信息")
    private String sexdesc;
	/**省份*/
	@Excel(name = "省份", width = 15)
    @ApiModelProperty(value = "省份")
    private String province;
	/**国家*/
	@Excel(name = "国家", width = 15)
    @ApiModelProperty(value = "国家")
    private String country;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**关注时间*/
	@Excel(name = "关注时间", width = 15)
    @ApiModelProperty(value = "关注时间")
    private String subscribetime;
	/**组id*/
	@Excel(name = "组id", width = 15)
    @ApiModelProperty(value = "组id")
    private Integer groupid;
	/**标签*/
	@Excel(name = "标签", width = 15)
    @ApiModelProperty(value = "标签")
    private String tagids;
	/**关注的渠道来源*/
	@Excel(name = "关注的渠道来源", width = 15)
    @ApiModelProperty(value = "关注的渠道来源")
    private String subscribescene;
	/**二维码扫码场景*/
	@Excel(name = "二维码扫码场景", width = 15)
    @ApiModelProperty(value = "二维码扫码场景")
    private String qrscene;
	/**二维码扫码场景描述*/
	@Excel(name = "二维码扫码场景描述", width = 15)
    @ApiModelProperty(value = "二维码扫码场景描述")
    private String qrscenestr;
}
