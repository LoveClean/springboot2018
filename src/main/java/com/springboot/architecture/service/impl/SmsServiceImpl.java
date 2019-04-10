package com.springboot.architecture.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.springboot.architecture.config.SmsConfig;
import com.springboot.architecture.contants.Errors;
import com.springboot.architecture.contants.SmsConstants;
import com.springboot.architecture.service.SmsService;
import com.springboot.architecture.util.ExceptionUtil;
import com.springboot.architecture.util.HttpUtil;
import com.springboot.architecture.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * @Title: SmsServiceImpl.java
 * @Package cc.uworks.library.service.impl
 * @author liyuchang
 * @date 2017年1月23日
 * @version V1.0
 */
@Service("smsService")
public class SmsServiceImpl implements SmsService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private SmsConfig smsConfig;

  /**
   * 短信单发
   * 
   * @param mobile
   * @param content
   * @return true 发送成功，false 发送失败
   */
  @Override
  public boolean send(String mobile, String content) {
    try {
      logger.info("发送短信, mobile = {}, content = {}", mobile, content);
      if (StringUtil.isBlank(mobile)) {
        ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "手机号不能为空");
      }
      if (StringUtil.isBlank(content)) {
        ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "短信内容不能为空");
      }
      JSONObject params = new JSONObject();
      params.put("platformId", smsConfig.getPlatformId());
      params.put("mobiles", mobile);
      params.put("content", content);
      params.put("sign", this.sign(content, "utf-8"));
      String json = params.toJSONString();
      
      System.out.println(json);

      HttpUtil.doPostJson(smsConfig.getUrl()+"sendMessage", json);
      return true;
    } catch (Exception e) {
      logger.error("发送短信失败， mobile = {}, content = {}, error={}", mobile, content, e.getMessage(), e);
      return false;
    }
  }
  
  public boolean sendMass(List<String> mobiles, String content) {
	  StringBuilder mobilesStr = new StringBuilder();
	    mobiles.forEach(mobile -> {
	      if (StringUtils.isNotBlank(mobile)) {
	        mobilesStr.append(mobile).append(",");
	      }
	      });
	    mobilesStr.deleteCharAt(mobilesStr.length()-1);
	    try {
	      logger.info("发送短信, mobiles = {}, content = {}", mobilesStr, content);
	      
	      if (StringUtil.isBlank(mobilesStr)) {
	        ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "手机号不能为空");
	      }
	      if (StringUtil.isBlank(content)) {
	        ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "短信内容不能为空");
	      }
	      JSONObject params = new JSONObject();
	      params.put("platformId", smsConfig.getPlatformId());
	      params.put("mobiles", mobilesStr);
	      params.put("content", content);
	      params.put("sign", this.sign(content, "utf-8"));
	      String json = params.toJSONString();
	      
	      System.out.println(json);

	      HttpUtil.doPostJson(smsConfig.getUrl()+"sendMass", json);
	      return true;
	    } catch (Exception e) {
	      logger.error("发送短信失败， mobile = {}, content = {}, error={}", mobilesStr, content, e.getMessage(), e);
	      return false;
	    }
	  }

  /**
   * 短信群发
   * 
   * @param mobiles
   * @param content
   */
  @Override
  public void send(List<String> mobiles, String content) {
    StringBuilder mobilesStr = new StringBuilder();
    mobiles.forEach(mobile -> {
      if (StringUtils.isNotBlank(mobile)) {
        mobilesStr.append(mobile).append(",");
      }
      if (mobiles.toString().split(",").length >= SmsConstants.MAX_COUNT) {
        this.send(mobiles.toString().replaceAll(",$", ""), content);
        mobilesStr.setLength(0);
      }
    });
    if (mobilesStr.length() > 0) {
      this.send(mobiles.toString().replaceAll(",$", ""), content);
    }
  }

  //私钥签名
  public String sign(String content, String charset) {
	  String privateKey=smsConfig.getPrivateKey();
	  byte[] keyBytes= Base64.decodeBase64(privateKey);
	  PKCS8EncodedKeySpec pkcs8KeySpec= new PKCS8EncodedKeySpec(keyBytes);
	  
	  try {
		  KeyFactory keyFactory=KeyFactory.getInstance("RSA");
		  PrivateKey privateK=keyFactory.generatePrivate(pkcs8KeySpec);
		  Signature signature=Signature.getInstance("SHA1WithRSA");
		  signature.initSign(privateK);
		  
		  signature.update(content.getBytes(charset));
		  
		  String signStr= Base64.encodeBase64String(signature.sign());
		  if(verify(content, signStr, smsConfig.getPublicKey())) {
			  return signStr;
		  }else {
			  return "";
		  }
		  
	  }catch (Exception e) {
		e.printStackTrace();
		return "发生异常";
	}
  }
  
  //签名与公钥验证
  public boolean verify(String content, String sign, String publicKeyString) throws Exception {
	  KeyFactory keyFactory= KeyFactory.getInstance("RSA");
	  PublicKey publicKey= keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString)));
	  Signature signature=Signature.getInstance("SHA1WithRSA");
	  signature.initVerify(publicKey);
	  signature.update(content.getBytes("utf-8"));
	  return signature.verify(Base64.decodeBase64(sign));
	  
  }
  
}
