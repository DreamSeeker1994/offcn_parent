package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.util.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Api(tags="项目基本功能模块（文件上传、项目信息获取等）")
@Slf4j
@RequestMapping("/project")
@RestController
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("文件上传功能")
    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> upload(@RequestParam("file")MultipartFile[] files) throws IOException {
        Map<String,Object> map = new HashMap();
        List<String> list = new ArrayList<>();
        System.out.println(Arrays.toString(files));
        if (files != null && files.length > 0){
            for (MultipartFile file : files) {
                if (!file.isEmpty()){
                    String url = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                    list.add(url);
                }
            }

            map.put("urls",list);
            log.debug("ossTemplate信息：{},文件上传成功访问路径{}",ossTemplate,list);
        }
        return AppResponse.ok(map);
    }

    @ApiOperation("获取详细的回报信息")
    @GetMapping("/getDetailsReturn/{projectId}")
    public AppResponse getDetailsReturn(@PathVariable(value = "projectId") Integer projectId){
        List<TReturn> projectReturns = projectInfoService.getProjectReturns(projectId);
        return AppResponse.ok(projectReturns);
    }

    @ApiOperation("获取所有的项目")
    @GetMapping("/getAllProject")
    public AppResponse<List<ProjectVo>> getAllProject(){
        List<ProjectVo> projectVoList = new ArrayList<>();
        List<TProject> allProjects = projectInfoService.getAllProjects();

        for (TProject project : allProjects) {
            Integer projectId = project.getId();
            List<TProjectImages> images = projectInfoService.getProjectImages(projectId);
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(project,projectVo);
            for (TProjectImages image : images) {
                if (image.getImgtype() == 0){
                    projectVo.setHeaderImage(image.getImgurl());
                }
            }

            projectVoList.add(projectVo);
        }
        return AppResponse.ok(projectVoList);
    }

    @ApiOperation("获取项目详细信息")
    @GetMapping("/getDetailPro/{pid}")
    public AppResponse getDetailPro(@PathVariable("pid") Integer pid){
        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        TProject projectInfo = projectInfoService.getProjectInfo(pid);

        List<String> detailsImage = projectDetailVo.getDetailsImage();
        List<TProjectImages> projectImages = projectInfoService.getProjectImages(pid);
        if (detailsImage == null){
            detailsImage = new ArrayList<>();
        }
        for (TProjectImages image : projectImages) {
            if(image.getImgtype() == 0){
                projectDetailVo.setHeaderImage(image.getImgurl());
            }else {
                detailsImage.add(image.getImgurl());
            }
        }
        projectDetailVo.setDetailsImage(detailsImage);

        List<TReturn> projectReturns = projectInfoService.getProjectReturns(pid);

        projectDetailVo.setProjectReturns(projectReturns);
        BeanUtils.copyProperties(projectInfo,projectDetailVo);
        return AppResponse.ok(projectDetailVo);
    }

    @ApiOperation("获取所有的项目标签")
    @GetMapping("/getAllTag")
    public AppResponse<List<TTag>> getAllTag(){
        List<TTag> allProjectTags = projectInfoService.getAllProjectTags();
        return AppResponse.ok(allProjectTags);
    }
    @GetMapping("/getAllType")
    @ApiOperation("获取项目所有的分类")
    public AppResponse<List<TType>> getAllType(){
        List<TType> projectTypes = projectInfoService.getProjectTypes();
        return AppResponse.ok(projectTypes);
    }

    @ApiOperation("获取回报的详细信息")
    @GetMapping("/getDetailReturn/{returnId}")
    public AppResponse getDetailReturn(@PathVariable("returnId") Integer returnId){
        TReturn returnInfo = projectInfoService.getReturnInfo(returnId);
        return AppResponse.ok(returnInfo);
    }
}
