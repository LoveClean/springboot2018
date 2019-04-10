package com.springboot.architecture.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.architecture.controller.response.PageResponseBean;
import com.springboot.architecture.dao.entity.Admin;
import com.springboot.architecture.dao.mapper.AdminMapper;
import com.springboot.architecture.service.AdminService;
import com.springboot.architecture.util.ResponseEntity;
import com.springboot.architecture.util.ResponseEntityUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    AdminMapper adminMapper;

    @Override
    public ResponseEntity<Integer> deleteByPrimaryKey(Integer id, String updateBy) {
        return ResponseEntityUtil.success(adminMapper.deleteByPrimaryKey(id, updateBy));
    }

    @Override
    public ResponseEntity<Integer> insertSelective(Admin record) {
        return ResponseEntityUtil.success(adminMapper.insertSelective(record));
    }

    @Override
    public ResponseEntity<Admin> selectByPrimaryKey(Integer id) {
        return ResponseEntityUtil.success(adminMapper.selectByPrimaryKey(id));
    }

    @Override
    public ResponseEntity<Integer> updateByPrimaryKeySelective(Admin record) {
        return ResponseEntityUtil.success(adminMapper.updateByPrimaryKeySelective(record));
    }

    @Override
    public ResponseEntity<Integer> updatePasswordById(Integer id, String oldPassword, String newPassword, String updateBy) {
        return ResponseEntityUtil.success(adminMapper.updatePasswordById(id, oldPassword, newPassword, updateBy));
    }

    @Override
    public ResponseEntity<Integer> updateStatusById(Integer id, Integer status, String updateBy) {
        return ResponseEntityUtil.success(adminMapper.updateStatusById(id, status, updateBy));
    }

    @Override
    public PageResponseBean selectList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> admins= adminMapper.selectList();
        PageInfo pageInfo =new PageInfo(admins);
        pageInfo.setList(admins);

        PageResponseBean page = new PageResponseBean<Admin>(pageInfo);
        page.setCode(0);
        page.setHttpStatus(200);
        return page;
    }

    @Override
    public ResponseEntity<Admin> login(String phone, String password) {
        return ResponseEntityUtil.success(adminMapper.login(phone, password));
    }
}
