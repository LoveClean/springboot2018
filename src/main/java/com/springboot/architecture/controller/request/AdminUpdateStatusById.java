package com.springboot.architecture.controller.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class AdminUpdateStatusById {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
