package com.springboot.architecture.service;

import java.util.List;

/**
 * @Description: 短信发送业务
 * @date 2017年1月23日
 * @version V1.0
 */
public interface SmsService {

  /**
   * 短信单发
   * @param mobile
   * @param content
   * @return true 发送成功，false 发送失败
   */
  boolean send(String mobile, String content);

  /**
   * 短信群发
   * @param mobiles
   * @param content
   */
  void send(List<String> mobiles, String content);
  
  boolean sendMass(List<String> mobiles, String content);
  
  public String sign(String content, String charset);

}
