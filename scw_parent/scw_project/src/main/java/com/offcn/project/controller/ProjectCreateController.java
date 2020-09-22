package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnum;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.po.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import com.offcn.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "项目基本功能模块 增删改查")
@RequestMapping("/project")
public class ProjectCreateController {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;


    @ApiOperation("项目发起第1步-阅读同意协议")
    @PostMapping("/init")
    public AppResponse init(BaseVo baseVo){
        String accessToken = baseVo.getAccessToken();

        String memberId = stringRedisTemplate.opsForValue().get(accessToken);

        if (StringUtils.isEmpty(memberId)){
            return AppResponse.fail("没有权限，请先登录");
        }
        int id = Integer.parseInt(memberId);

        String projectToken  = projectCreateService.initCreateProject(id);

        return AppResponse.ok(projectToken);
    }

    @ApiOperation("项目发起第2步-保存项目的基本信息")
    @PostMapping("/savebaseInfo")
    public AppResponse savebaseInfo(ProjectBaseInfoVo vo){
        String project  = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken());
        ProjectRedisStorageVo storageVo = JSON.parseObject(project, ProjectRedisStorageVo.class);
        BeanUtils.copyProperties(vo,storageVo);
        String jsonString = JSON.toJSONString(storageVo);
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken(),jsonString);

        return AppResponse.ok("项目信息保存成功");
    }

    @PostMapping("/saveReturnInfo")
    @ApiOperation("项目发起第3步-项目保存项目回报信息")
    public AppResponse saveReturnInfo(@RequestBody List<ProjectReturnVo> returnVos){
        ProjectReturnVo returnVo = returnVos.get(0);

        String projectToken = returnVo.getProjectToken();

        String project = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken);

        ProjectRedisStorageVo storageVo = JSON.parseObject(project, ProjectRedisStorageVo.class);

        List<TReturn> list = new ArrayList<>();

        for (ProjectReturnVo vo : returnVos) {

            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(vo,tReturn);

            list.add(tReturn);
        }

        storageVo.setProjectReturns(list);

        String jsonString = JSON.toJSONString(storageVo);

        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken,jsonString);

        return AppResponse.ok("保存回报成功");
    }

    @ApiOperation("项目发起第4步-将项目全部信息永久保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken",value = "用户令牌",required = true),
            @ApiImplicitParam(name = "projectToken",value = "项目令牌",required = true),
            @ApiImplicitParam(name="ops",value="用户操作类型 0-保存草稿 1-提交审核",required = true)
    })
    @GetMapping("/submit")
    public AppResponse submit(String accessToken,String projectToken,String ops){
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)){
            return AppResponse.fail("没有权限，请先登录");
        }

        String project = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken);

        ProjectRedisStorageVo storageVo = JSON.parseObject(project, ProjectRedisStorageVo.class);

        if (!StringUtils.isEmpty(ops)){
            if (ops.equals("1")){
                ProjectStatusEnum submitAuth = ProjectStatusEnum.SUBMIT_AUTH;
                projectCreateService.saveProjectInfo(submitAuth,storageVo);
            }else if (ops.equals("0")){
                ProjectStatusEnum draft = ProjectStatusEnum.DRAFT;
                projectCreateService.saveProjectInfo(draft,storageVo);
            }else {
                return AppResponse.fail("不支持此操作");
            }
        }
        return AppResponse.fail(null);
    }
}
