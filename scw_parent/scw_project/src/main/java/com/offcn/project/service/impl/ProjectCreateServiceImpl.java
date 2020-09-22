package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnum;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TProjectMapper tProjectMapper;

    @Autowired
    private TProjectImagesMapper tProjectImagesMapper;

    @Autowired
    private TProjectTagMapper tProjectTagMapper;

    @Autowired
    private TProjectTypeMapper tProjectTypeMapper;

    @Autowired
    private TReturnMapper tReturnMapper;

    /**初始化项目
     * @param memberId
     * @return
     */
    @Override
    public String initCreateProject(Integer memberId) {
        String projectToken = UUID.randomUUID().toString().replace("-","");
        ProjectRedisStorageVo storageVo = new ProjectRedisStorageVo();
        storageVo.setMemberid(memberId);
        String jsonString = JSON.toJSONString(storageVo);
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken,jsonString);

        return projectToken;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnum auth, ProjectRedisStorageVo project) {
        TProject projectBase = new TProject();
        BeanUtils.copyProperties(project,projectBase);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        projectBase.setCreatedate(sdf.format(new Date()));
        //存入状态
        projectBase.setStatus(auth.getCode()+"");
        //1保存项目基本信息,并获取刚插入信息的id
        tProjectMapper.insertSelective(projectBase);

        Integer id = projectBase.getId();
        //2保存项目的头图片
        String headerImage = project.getHeaderImage();
        TProjectImages projectImages = new TProjectImages(null,id,headerImage, ProjectImageTypeEnume.HEADER.getCode());
        tProjectImagesMapper.insertSelective(projectImages);

        //3保存项目的详细图片

        List<String> detailsImage = project.getDetailsImage();

        for (String image : detailsImage) {
            TProjectImages images = new TProjectImages(null,id,image,ProjectImageTypeEnume.DETAILS.getCode());
            tProjectImagesMapper.insertSelective(images);
        }

        //4保存项目的标签

        List<Integer> tagids = project.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tag = new TProjectTag(null,id,tagid);
            tProjectTagMapper.insertSelective(tag);
        }

        //5保存项目的分类信息
        List<Integer> typeids = project.getTypeids();
        for (Integer typeid : typeids) {
            TProjectType type = new TProjectType(null,id,typeid);
            tProjectTypeMapper.insertSelective(type);
        }

        //保存项目的回报信息

        List<TReturn> projectReturns = project.getProjectReturns();
        for (TReturn tReturn : projectReturns) {
            tReturn.setProjectid(id);
            tReturnMapper.insertSelective(tReturn);
        }
        //7删除缓存里面的项目信息
        stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX + project.getProjectToken());
    }
}
