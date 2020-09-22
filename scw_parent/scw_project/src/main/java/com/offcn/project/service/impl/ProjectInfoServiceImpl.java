package com.offcn.project.service.impl;

import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private TReturnMapper tReturnMapper;

    @Autowired
    private TProjectMapper tProjectMapper;

    @Autowired
    private TProjectImagesMapper imagesMapper;

    @Autowired
    private TTagMapper tTagMapper;

    @Autowired
    private TTypeMapper tTypeMapper;

    @Autowired
    private TReturnMapper returnMapper;

    @Override
    public List<TReturn> getProjectReturns(Integer projectId) {
        TReturnExample example = new TReturnExample();
        TReturnExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        return  tReturnMapper.selectByExample(example);
    }

    @Override
    public List<TProject> getAllProjects() {
        List<TProject> tProjects = tProjectMapper.selectByExample(null);
        return tProjects;
    }

    @Override
    public List<TProjectImages> getProjectImages(Integer pid) {

        TProjectImagesExample example = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(pid);

        return imagesMapper.selectByExample(example);
    }

    @Override
    public TProject getProjectInfo(Integer projectId) {
        return tProjectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public List<TTag> getAllProjectTags() {
        return tTagMapper.selectByExample(null);
    }

    @Override
    public List<TType> getProjectTypes() {
        return tTypeMapper.selectByExample(null);
    }

    @Override
    public TReturn getReturnInfo(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }
}
