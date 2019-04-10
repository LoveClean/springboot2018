package com.springboot.architecture.service.impl;

import com.alibaba.fastjson.JSON;
import com.springboot.architecture.contants.Const;
import com.springboot.architecture.contants.Errors;
import com.springboot.architecture.dao.entity.Admin;
import com.springboot.architecture.service.MemcachedService;
import com.springboot.architecture.service.UserSessionService;
import com.springboot.architecture.util.ExceptionUtil;
import com.springboot.architecture.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("userSessionService")
//@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserSessionServiceImpl implements UserSessionService {

	@Autowired  
    private MemcachedService memcachedService;


  /**
   * 缓存用户信息，JSON格式
   * 
   * @param request
   * @param
   */
  @Override
  public void sessionUser(HttpServletRequest request, Admin admin) {
    String key = getUserSessionKey(request);
    // JSON格式
    String userJson = JSON.toJSONString(admin);
    
   
    memcachedService.set(key, Const.SERVER_USER_EXP_KEY, userJson);
    // 注销其他登陆
    String accesskey = Const.SERVER_USER_KEY + admin.getId();
    String _key = (String) memcachedService.get(accesskey);
    if (StringUtil.isNotBlank(_key) && !key.equals(_key)) {
    	memcachedService.delete(_key);
    }
    // 存入新accessKey
    memcachedService.set(accesskey, Const.SERVER_USER_EXP_KEY, key);
  }

  /**
   * 获取登录用户
   */
  @Override
  public Admin getSessionUser(HttpServletRequest request) {
    String key = getUserSessionKey(request);
    String jsonStr = (String) memcachedService.get(key);
    
    if (StringUtil.isBlank(jsonStr)) {
      ExceptionUtil.throwException(Errors.SYSTEM_NOT_LOGIN);
    }
    Admin user = JSON.parseObject(jsonStr, Admin.class);
    if (user != null) {
      memcachedService.set(key, Const.SERVER_USER_EXP_KEY, jsonStr);
      String accesskey = Const.SERVER_USER_KEY + user.getId();
      memcachedService.set(accesskey, Const.SERVER_USER_EXP_KEY, key);
    }
    return user;
  }

  /**
   * 获取登录用户
   */
  @Override
  public Admin getSessionUser(String accessToken) {
    String key = Const.SERVER_USER_KEY + accessToken;

    String jsonStr = (String) memcachedService.get(key);
    if (StringUtil.isBlank(jsonStr)) {
      return null;
    }
    return JSON.parseObject(jsonStr, Admin.class);
  }

  /**
   * 清除登录用户
   */
  @Override
  public void deleteSessionUser(HttpServletRequest request) {
    String key = getUserSessionKey(request);
    String jsonStr = (String) memcachedService.get(key);
    if (StringUtil.isNotBlank(jsonStr)) {
      String accesskey = Const.SERVER_USER_KEY + (JSON.parseObject(jsonStr, Admin.class)).getId();
      memcachedService.delete(accesskey);
      memcachedService.delete(key);
    }
  }

  /**
   * 获取用户缓存key
   * 
   * @param request
   * @return
   */
  private String getUserSessionKey(HttpServletRequest request) {
    String key = Const.SERVER_USER_KEY + getSessionKey(request);
    return key;
  }

  /**
   * <pre>
   * 获取缓存key
   * 同时使用，使用token保存登录信息，优先使用token，如果获取失败则取session
   * </pre>
   *
   * @param request
   */
  private String getSessionKey(HttpServletRequest request) {
    String sessionId = "";
    Object sessionIdAttribute = request.getAttribute(Const.ACCESS_TOKEN_HEADER_NAME);
    
    if (StringUtil.isNotBlank(sessionIdAttribute)) {
      sessionId = sessionIdAttribute.toString();
    }
    if (StringUtil.isBlank(sessionId)) {
      sessionId = request.getHeader(Const.ACCESS_TOKEN_HEADER_NAME);
    }
    if (StringUtil.isBlank(sessionId)) {
      sessionId = request.getParameter("token");
    }
    if (StringUtil.isBlank(sessionId)) {
      sessionId = request.getSession().getId();
    }
    return sessionId;
  }

  /**
   * 生成token sessionid+uuid
   */
  @Override
  public String generateAccessToken(HttpServletRequest request) {
    return request.getSession().getId() + StringUtil.uuidNotLine();
  }

  /**
   * 返回登录用户，未登录返回null
   * 
   * @param request
   * @return
   */
  @Override
  public Admin getLoginUser(HttpServletRequest request) {
    String key = getUserSessionKey(request);
    String jsonStr = (String) memcachedService.get(key);
    if (StringUtil.isBlank(jsonStr)) {
      return null;
    }
    Admin user = JSON.parseObject(jsonStr, Admin.class);
    if (user != null) {
      memcachedService.set(key, Const.SERVER_USER_EXP_KEY, jsonStr);
      String accesskey = Const.SERVER_USER_KEY + user.getId();
      memcachedService.set(accesskey, Const.SERVER_USER_EXP_KEY, key);
    }
    return user;
  }

  /**
   * 获取真实ip
   * 
   * @param request
   * @return
   */
  public String getRemoteIP(HttpServletRequest request) {
    if (request.getHeader("x-forwarded-for") == null) {
      return request.getRemoteAddr();
    }
    return request.getHeader("x-forwarded-for");
  }

}
