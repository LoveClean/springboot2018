package com.springboot.architecture.controller.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class AdminInsertSelective {
    @NotBlank(message = "手机号码不能为空")
    @ApiModelProperty(value = "手机号码，必填", required = true)
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Length(min = 3, max = 16, message = "请输入3-16位密码")
    @ApiModelProperty(value = "密码，必填", required = true)
    private String password;

    @ApiModelProperty(value = "姓名，选填")
    private String trueName;

    @ApiModelProperty(value = "身份等级，必填", required = true)
    private Integer level;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
