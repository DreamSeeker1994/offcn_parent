package com.offcn.project.service;

import com.offcn.dycommon.enums.ProjectStatusEnum;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {
    /**新建项目初始化
     * @param memberId
     * @return
     */
    public String initCreateProject(Integer memberId);

    /**保存项目到mysql数据库中
     * @param auth 项目状态
     * @param project 保存在redis里面的项目信息
     */
    public void saveProjectInfo(ProjectStatusEnum auth, ProjectRedisStorageVo project);
}
