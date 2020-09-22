package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.resp.UserAddressVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("获取用户收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken" ,value = "用户令牌" ,required = true)
    })
    @RequestMapping("/getAllAddress")
    public AppResponse getAllAddress(String accessToken){
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)){
            return AppResponse.fail(null);
        }

        List<UserAddressVo> addressVoList = new ArrayList<>();

        List<TMemberAddress> tMemberAddresses = userService.addressList(Integer.parseInt(memberId));

        for (TMemberAddress address : tMemberAddresses) {
            UserAddressVo userAddressVo = new UserAddressVo();
            userAddressVo.setId(address.getId());
            userAddressVo.setAddress(address.getAddress());
            addressVoList.add(userAddressVo);
        }

        return AppResponse.ok(addressVoList);
    }
}
