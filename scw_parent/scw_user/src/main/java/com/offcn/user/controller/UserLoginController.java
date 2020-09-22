package com.offcn.user.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.po.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登录/注册模块")
@Slf4j
public class UserLoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("获取注册的验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true)
    })
    @PostMapping("/sendCode")
    public AppResponse sendCode(String phone){
        String code = UUID.randomUUID().toString().substring(0,4);

        stringRedisTemplate.opsForValue().set(phone,code,10, TimeUnit.MINUTES);
        System.out.println(code);
        Map map = new HashMap();
        map.put("mobile",phone);
        map.put("param","code:"+code);
        map.put("tpl_id", "TP1711063");

        String sendCode = smsTemplate.sendCode(map);

        if (sendCode.equals("") || sendCode.equals("fail")){
            return AppResponse.fail("短信发送失败");
        }

        return AppResponse.ok(sendCode);
    }

    @ApiOperation("用户注册")
    @PostMapping("/regist")
    public AppResponse regist(UserRegistVo registVo) {
        String code = stringRedisTemplate.opsForValue().get(registVo.getLoginacct());

        if (!StringUtils.isEmpty(code)) {
            boolean b = code.equalsIgnoreCase(registVo.getCode());
            if (b) {
                TMember member = new TMember();
                BeanUtils.copyProperties(registVo,member);

                try {
                    userService.registerUser(member);
                    log.debug("用户注册成功：{}",member.getLoginacct());
                    stringRedisTemplate.delete(member.getLoginacct());
                    return AppResponse.ok("注册成功");
                } catch (Exception e) {
                    log.error("用户注册失败");
                    return AppResponse.fail(e.getMessage());
                }
            }else {
                return AppResponse.fail("验证码错误");
            }
        }else {
            return AppResponse.fail("验证码已过期");
        }
    }

    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true)
    })
    @GetMapping("/login")
    public AppResponse login(String username,String password){
        TMember member = userService.login(username, password);
        if (member == null){
            return AppResponse.fail("用户名或密码错误");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member,vo);
        vo.setAccessToken(token);
        stringRedisTemplate.opsForValue().set(token,member.getId()+"",5,TimeUnit.HOURS);

        return AppResponse.ok(vo);
    }

    @ApiOperation("根据用户id查询用户")
    @GetMapping("/findUser/{id}")
    public AppResponse findTmemberById(@RequestParam(value = "id") Integer id){
        TMember tmember = userService.findTmemberById(id);
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(tmember,vo);

        return AppResponse.ok(vo);
    }
}
