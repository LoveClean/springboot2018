package com.springboot.architecture.controller;

import com.springboot.architecture.controller.request.*;
import com.springboot.architecture.controller.response.PageResponseBean;
import com.springboot.architecture.dao.entity.Admin;
import com.springboot.architecture.service.AdminService;
import com.springboot.architecture.service.MemcachedService;
import com.springboot.architecture.service.SmsService;
import com.springboot.architecture.util.ResponseEntity;
import com.springboot.architecture.util.ResponseEntityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(description = "管理员操作接口", produces = "application/json")
@RestController
@RequestMapping("/admin/")
public class AdminController extends BaseController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private MemcachedService memcachedService;

    @ApiOperation(value = "删除管理员", notes = "根据主键删除")
    @DeleteMapping(value = "deleteByPrimaryKey")
    public ResponseEntity<Integer> deleteByPrimaryKey(@RequestParam Integer id, HttpServletRequest request) {
        return adminService.deleteByPrimaryKey(id, super.getSessionUser(request).getTruename());
    }

    @ApiOperation(value = "添加管理员", notes = "添加管理员")
    @PostMapping(value = "insertSelective")
    public ResponseEntity<Integer> insertSelective(@Valid @RequestBody AdminInsertSelective bean,
                                                   HttpServletRequest request) {
        Admin admin = new Admin(bean.getTrueName(), bean.getPassword(),
                bean.getPhone(), bean.getLevel(), super.getSessionUser(request).getTruename());

        ResponseEntity response = adminService.insertSelective(admin);
        if (response.isSuccess()) {
            //注册成功，发送短信，告知初始密码
            smsService.send(bean.getPhone(), "您已成为系统的" + bean.getLevel() + "管理员,初始密码为：" + bean.getPassword());
        }
        return response;
    }

    @ApiOperation(value = "查看其他管理员", notes = "根据主键查看管理员")
    @GetMapping(value = "selectByPrimaryKey")
    public ResponseEntity<Admin> selectByPrimaryKey(@RequestParam Integer id) {
        return adminService.selectByPrimaryKey(id);
    }

    @ApiOperation(value = "查看个人信息", notes = "查看个人信息")
    @GetMapping(value = "selectBySession")
    public ResponseEntity<Admin> selectBySession(HttpServletRequest request) {
        Admin admin = super.getSessionUser(request);
        if (admin != null) {
            return ResponseEntityUtil.success(admin);
        }
        return ResponseEntityUtil.fail("用户未登录，无法获取当前用户信息");
    }

    @ApiOperation(value = "修改管理员", notes = "修改管理员")
    @PutMapping(value = "updateByPrimaryKeySelective")
    public ResponseEntity<Integer> updateByPrimaryKeySelective(@Valid @RequestBody AdminUpdateByPrimaryKeySelective bean, HttpServletRequest request) {
        Admin admin = new Admin();

        admin.setId(bean.getId());
        admin.setTruename(bean.getTrueName());
        admin.setPhone(bean.getPhone());
        admin.setPhone(bean.getPassword());
        admin.setLevel(bean.getLevel());
        admin.setUpdateBy(super.getSessionUser(request).getTruename());

        return adminService.updateByPrimaryKeySelective(admin);
    }

    @ApiOperation(value = "修改管理员密码", notes = "修改管理员密码")
    @PutMapping(value = "updatePasswordById")
    public ResponseEntity<Integer> updatePasswordById(@Valid @RequestBody AdminUpdatePasswordById bean, HttpServletRequest request) {
        return adminService.updatePasswordById(bean.getId(), bean.getOldPassword(), bean.getNewPassword(), super.getSessionUser(request).getTruename());
    }

    @ApiOperation(value = "修改管理员状态", notes = "修改管理员状态")
    @PutMapping(value = "updateStatusById")
    public ResponseEntity<Integer> updateStatusById(@Valid @RequestBody AdminUpdateStatusById bean, HttpServletRequest request) {
        return adminService.updateStatusById(bean.getId(), bean.getStatus(), super.getSessionUser(request).getTruename());
    }

    @ApiOperation(value = "查看管理员列表", notes = "查看管理员列表")
    @GetMapping(value = "selectList")
    public PageResponseBean selectList(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return adminService.selectList(pageNum, pageSize);
    }

    @ApiOperation(value = "登陆", notes = "管理员登陆")
    @PostMapping(value = "login")
    public ResponseEntity<Admin> login(@Valid @RequestBody AdminLogin bean, HttpServletRequest request) {

//        Boolean flag=false;
//        if (memcachedService.get(Const.VERIFY_CODE) != null) {
//            String randomCode = memcachedService.get(Const.VERIFY_CODE).toString();
//
//            if (StringUtil.isNotBlank(randomCode) && bean.getVerifyCode().toUpperCase().equals(randomCode.toUpperCase())) {
//                flag = true;
//                memcachedService.delete(Const.VERIFY_CODE);
//            }
//        }
//
//        if(!flag) {
//            return ResponseEntityUtil.fail("验证码错误");
//        }

        ResponseEntity<Admin> response = adminService.login(bean.getPhone(), bean.getLoginPwd());
        if (response.isSuccess()) {
            Admin admin = response.getData();
            //session.setAttribute(Const.CURRENT_USER, response.getData());
            // 创建访问token
            String accessToken = super.generateAccessToken(request);
            admin.setAccessToken(accessToken);

            super.setAccessTokenAttribute(request, accessToken);
            super.sessionUser(request, admin);

            return ResponseEntityUtil.success(admin);
        }
        return response;
    }
}
