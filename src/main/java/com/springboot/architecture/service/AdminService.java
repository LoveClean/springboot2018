package com.springboot.architecture.service;

import com.springboot.architecture.controller.response.PageResponseBean;
import com.springboot.architecture.dao.entity.Admin;
import com.springboot.architecture.util.ResponseEntity;

public interface AdminService {
    ResponseEntity<Integer> deleteByPrimaryKey(Integer id, String updateBy);

    ResponseEntity<Integer> insertSelective(Admin record);

    ResponseEntity<Admin> selectByPrimaryKey(Integer id);

    ResponseEntity<Integer> updateByPrimaryKeySelective(Admin record);

    ResponseEntity<Integer> updatePasswordById(Integer id, String oldPassword, String newPassword, String updateBy);

    ResponseEntity<Integer> updateStatusById(Integer id, Integer status, String updateBy);

    PageResponseBean selectList(Integer pageNum, Integer pageSize);

    ResponseEntity<Admin> login(String phone, String password);
}
