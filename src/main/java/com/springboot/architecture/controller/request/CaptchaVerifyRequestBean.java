package com.springboot.architecture.controller.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class CaptchaVerifyRequestBean {
	@ApiModelProperty(value = "手机号", required = true)
	  @NotBlank(message = "手机号不能为空")
	private String mobile;
	@ApiModelProperty(value = "验证码", required = true)
	  @NotBlank(message = "验证码不能为空")
	private String captcha;
	  @ApiModelProperty(value = "类型：1注册,2修改密码,3重置密码，4注册+登陆", required = true)
	  @NotNull(message = "验证码类型不能为空")
    private byte type;  //type 类型:1注册,2修改密码,3找回密码
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}


	  
	  
}
