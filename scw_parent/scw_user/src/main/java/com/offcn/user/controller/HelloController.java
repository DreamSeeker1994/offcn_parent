package com.offcn.user.controller;

import com.offcn.user.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "测试Swagger")
public class HelloController {

    @ApiOperation("测试方法hello")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "姓名",required = true),
            @ApiImplicitParam(name = "age",value = "年龄")
    })
    @GetMapping("/hello")
    public String hello(String name,int age){
        return "ok!"+"name:"+name+"age:"+age;
    }

    @ApiOperation("保存用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "姓名",required = true),
            @ApiImplicitParam(name = "email",value = "邮箱")
    })
    @GetMapping("/save")
    public User save(String name,String email){
        User user = new User();
        user.setName(name);
        user.setEmail(email);

        return user;
    }

}
