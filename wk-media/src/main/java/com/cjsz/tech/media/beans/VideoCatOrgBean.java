package com.cjsz.tech.media.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 被分配的机构
 * Created by Administrator on 2016/12/23 0023.
 */
public class VideoCatOrgBean {

    private Long video_cat_id;    //分类Id

    private Long project_id;    //项目Id

    private String project_code;    //项目编号

    private String project_name;    //项目名称

    private Long org_id;    //机构Id

    private String  org_code;   //机构编码

    private String  org_name;   //机构名

    private String  short_name;   //机构名简称

    @JSONField(format = "yyyy-MM-dd")
    private Date create_time;

    private Integer is_delete;  //是否删除（1：删除；2：不删除）

    public Long getVideo_cat_id() {
        return video_cat_id;
    }

    public void setVideo_cat_id(Long video_cat_id) {
        this.video_cat_id = video_cat_id;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public Long getOrg_id() {
        return org_id;
    }

    public void setOrg_id(Long org_id) {
        this.org_id = org_id;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Integer is_delete) {
        this.is_delete = is_delete;
    }
}
