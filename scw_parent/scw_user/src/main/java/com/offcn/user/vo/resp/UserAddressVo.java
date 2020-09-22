package com.offcn.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class UserAddressVo {

    @ApiModelProperty("地址id")
    private Integer id ;

    @ApiModelProperty("会员地址")
    private String address ;

    public UserAddressVo(Integer id, String address) {
        this.id = id;
        this.address = address;
    }

    public UserAddressVo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
