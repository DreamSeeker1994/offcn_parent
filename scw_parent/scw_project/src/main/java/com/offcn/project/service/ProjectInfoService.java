package com.offcn.project.service;

import com.offcn.project.po.*;

import java.util.List;

public interface ProjectInfoService {

    /**根据项目id查询回报
     * @param projectId
     * @return
     */
    public List<TReturn> getProjectReturns(Integer projectId);

    /**获取所有项目
     * @return
     */
    public List<TProject> getAllProjects();

    /**
     * 获取项目图片
     * @param pid
     * @return
     */
    List<TProjectImages> getProjectImages(Integer pid);

    /**获取项目详细信息
     * @param projectId
     * @return
     */
    TProject getProjectInfo(Integer projectId);

    /**
     * 获得项目标签
     * @return
     */
    List<TTag> getAllProjectTags();

    /**
     * 获取项目分类
     * @return
     */
    List<TType> getProjectTypes();

    /**
     * 获取回报信息
     * @param returnId
     * @return
     */
    TReturn getReturnInfo(Integer returnId);

}
