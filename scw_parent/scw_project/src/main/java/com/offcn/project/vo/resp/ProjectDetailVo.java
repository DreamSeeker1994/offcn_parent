package com.offcn.project.vo.resp;

import com.offcn.project.po.TReturn;

import java.util.List;

public class ProjectDetailVo {
    private Integer memberid;// 会员id
    private Integer id;

    private String name;//项目名称

    private String remark;//项目简介

    private Long money;//筹资金额

    private Integer day;//筹资天数

    private String status;//筹资状态  0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败

    private String deploydate;//发布日期
    private Long supportmoney = 0L;//支持金额
    private Integer supporter = 0;//支持者数量
    private Integer completion = 0;//完成度
    private Integer follower = 100;//关注者数量
    // 项目头部图片
    private String headerImage;
    // 项目详情图片
    private List<String> detailsImage;
    // 发起人信息：自我介绍，详细自我介绍，联系电话，客服电话
    // 项目回报
    private List<TReturn> projectReturns;

    public ProjectDetailVo(Integer memberid, Integer id, String name, String remark, Long money, Integer day, String status, String deploydate, Long supportmoney, Integer supporter, Integer completion, Integer follower, String headerImage, List<String> detailsImage, List<TReturn> projectReturns) {
        this.memberid = memberid;
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.money = money;
        this.day = day;
        this.status = status;
        this.deploydate = deploydate;
        this.supportmoney = supportmoney;
        this.supporter = supporter;
        this.completion = completion;
        this.follower = follower;
        this.headerImage = headerImage;
        this.detailsImage = detailsImage;
        this.projectReturns = projectReturns;
    }

    public ProjectDetailVo() {
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeploydate() {
        return deploydate;
    }

    public void setDeploydate(String deploydate) {
        this.deploydate = deploydate;
    }

    public Long getSupportmoney() {
        return supportmoney;
    }

    public void setSupportmoney(Long supportmoney) {
        this.supportmoney = supportmoney;
    }

    public Integer getSupporter() {
        return supporter;
    }

    public void setSupporter(Integer supporter) {
        this.supporter = supporter;
    }

    public Integer getCompletion() {
        return completion;
    }

    public void setCompletion(Integer completion) {
        this.completion = completion;
    }

    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public List<String> getDetailsImage() {
        return detailsImage;
    }

    public void setDetailsImage(List<String> detailsImage) {
        this.detailsImage = detailsImage;
    }

    public List<TReturn> getProjectReturns() {
        return projectReturns;
    }

    public void setProjectReturns(List<TReturn> projectReturns) {
        this.projectReturns = projectReturns;
    }
}
