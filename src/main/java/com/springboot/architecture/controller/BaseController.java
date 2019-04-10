package com.springboot.architecture.controller;

import com.springboot.architecture.contants.Const;
import com.springboot.architecture.contants.Errors;
import com.springboot.architecture.dao.entity.Admin;
import com.springboot.architecture.service.UserSessionService;
import com.springboot.architecture.util.ExceptionUtil;
import com.springboot.architecture.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;


/**
 * 基础的控制器，如默认的首页、辅助操作等..
 * 
 * @author lyc
 * @since 0.1.0
 */
@RestController
@RequestMapping("/")
public class BaseController {
 
  protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

  @Autowired
  protected UserSessionService userSessionService;

  // /**
  // * HomePage
  // */
  // @RequestMapping(value = "", method = RequestMethod.GET)
  // public ResponseEntity<String> homePage(HttpServletRequest request) {
  // logger.info("进入首页");
  // return ResponseEntityUtil.success("进入首页!");
  // }

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

  /**
   * protected
   *
   * @param request
   * @param
   * @return
   */
  protected String generateAccessToken(HttpServletRequest request) {
    return request.getSession().getId() + StringUtil.uuidNotLine();
	 // return request.getSession().getId();
  }

  /**
   * 获得 AccessToken
   * 
   * @return
   */
  protected String getAccessToken(HttpServletRequest request) {
    return request.getHeader(Const.ACCESS_TOKEN_HEADER_NAME);
  }

  /**
   * 设置登录权限的token
   *
   * @param request
   * @param accessToken
   */
  protected void setAccessTokenAttribute(HttpServletRequest request, String accessToken) {
    request.setAttribute(Const.ACCESS_TOKEN_HEADER_NAME, accessToken);
    }

  /**
   * 缓存用户信息
   * 
   * @param request
   * @param user
   */
  protected void sessionUser(HttpServletRequest request, Admin user) {
    userSessionService.sessionUser(request, user);
  }

  protected Admin getSessionUser(HttpServletRequest request) {
    return userSessionService.getSessionUser(request);
  }

  protected Admin getSessionUser(String accessToken) {
    return userSessionService.getSessionUser(accessToken);
  }

  protected String getSessionUserId(HttpServletRequest request) {
    return userSessionService.getSessionUser(request).getId().toString();
  }

  /**
   * 返回登录用户，未登录返回null
   * 
   * @param request
   * @return
   */
  protected Admin getLoginUser(HttpServletRequest request) {
    return userSessionService.getLoginUser(request);
  }

  protected void deleteSessionUser(HttpServletRequest request) {
    userSessionService.deleteSessionUser(request);
  }

  protected void throwException(int code, String codeLabel) {
    ExceptionUtil.throwException(code, codeLabel);
  }

  protected void throwException(Errors error) {
    ExceptionUtil.throwException(error);
  }

  /**
   * 导出下载文件
   * 
   * @param fileName 文件名
   * @param workBook 文件内容
   * @param errMsg 如果发生异常，异常提示说明
   * @param response
   */
  protected void writeFile(String fileName, HSSFWorkbook workBook, String errMsg, HttpServletResponse response) {
    OutputStream outputStream = null;
    try {
      response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
      response.setContentType("application/vnd.ms-excel");
      response.setCharacterEncoding("UTF-8");
      outputStream = response.getOutputStream();
      workBook.write(outputStream);
    } catch (IOException e) {
      e.printStackTrace();
      throwException(Errors.SYSTEM_CUSTOM_ERROR.code, errMsg);
    } finally {
      if (outputStream != null) {
        try {
          outputStream.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
