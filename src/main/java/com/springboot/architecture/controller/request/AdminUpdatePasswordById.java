package com.springboot.architecture.controller.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class AdminUpdatePasswordById {
    @ApiModelProperty(value = "id")
    private Integer id;

    @NotBlank(message = "原密码不能为空")
    @Length(min = 3, max = 16, message = "请输入3-16位密码")
    @ApiModelProperty(value = "原密码，必填", required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Length(min = 3, max = 16, message = "请输入3-16位密码")
    @ApiModelProperty(value = "新密码，必填", required = true)
    private String newPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
