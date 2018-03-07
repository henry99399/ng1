package com.cjsz.tech.cms.mapper;

import com.cjsz.tech.cms.beans.ArticleCatBean;
import com.cjsz.tech.cms.beans.ArticleCatOrgBean;
import com.cjsz.tech.cms.beans.FindCatOrgBean;
import com.cjsz.tech.cms.domain.ArticleCat;
import com.cjsz.tech.cms.provider.ArticleCatProvider;
import com.cjsz.tech.core.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public interface ArticleCatMapper extends BaseMapper<ArticleCat> {

    //获取机构的全部资讯分类(分类列表)
    @Select("select r.rel_id,r.org_id,r.article_cat_id,r.order_weight,r.is_show,r.source_id,r.source_type, " +
            "r.create_time,r.update_time,r.is_delete,c.pid,c.article_cat_name,c.article_cat_path, " +
            "c.enabled,c.remark,c.cat_pic,c.org_count  from article_cat_org_rel r " +
            "left join article_cat c on r.article_cat_id = c.article_cat_id " +
            "where c.is_delete = 2 and r.org_id = #{org_id} " +
            "order by length(c.article_cat_path)-length(replace(c.article_cat_path,'|','')) asc, r.order_weight desc")
    public List<ArticleCatBean> getCats(@Param("org_id") Long org_id);

    //获取机构的全部资讯分类(详情列表)
    @Select("select r.rel_id,r.org_id,r.article_cat_id,r.order_weight,r.is_show,r.source_id,r.source_type, " +
            "r.create_time,r.update_time,r.is_delete,c.pid,c.article_cat_name,c.article_cat_path, " +
            "c.enabled,c.remark,c.cat_pic,c.org_count  from article_cat_org_rel r " +
            "left join article_cat c on r.article_cat_id = c.article_cat_id " +
            "where c.is_delete = 2 and  c.enabled =1 and  r.org_id = #{org_id}  " +
            "order by length(c.article_cat_path)-length(replace(c.article_cat_path,'|','')) asc, r.order_weight desc")
    public List<ArticleCatBean> getOrgCats(@Param("org_id") Long org_id);

    //分类名称重复(分类名查找)
    @Select("select * from article_cat where org_id = #{0} and article_cat_name = #{1} and is_delete = 2")
    public List<ArticleCat> selectByCatName(Long org_id, String article_cat_name);

    //更新层次路径
    @Update("update article_cat set article_cat_path = #{1}, update_time = now() where article_cat_id = #{0}")
    public void updateCatPathByCatId(Long article_cat_id, String cat_path);

    //分类Id查询
    @Select("select * from article_cat where article_cat_id = #{0}")
    public ArticleCat selectByCatId(Long cat_id);

    //分类名称重复(分类名查找不包括本身)
    @Select("select * from article_cat where org_id = #{0} and article_cat_name = #{1} and article_cat_id != #{2} and is_delete = 2")
    public List<ArticleCat> selectOtherByCatName(Long org_id, String article_cat_name, Long article_cat_id);

    //将层次路径中包含old_full_path的，更新为new_full_path
    @Update("update article_cat set article_cat_path = REPLACE(article_cat_path,#{0},#{1}), update_time = now() where article_cat_path like concat(#{0},'%') and org_id = #{2}")
    public void updateFullPath(String old_full_path, String new_full_path, Long org_id);

    //启用
    @Update("update article_cat set enabled = 1,update_time = now() where enabled = 2 and org_id = #{org_id} and article_cat_id in(${cat_ids}) ")
    public void updateEnabledByCatIds(@Param("org_id") Long org_id, @Param("cat_ids") String cat_ids);

    //不启用
    @Update("update article_cat set enabled = 2,update_time = now() where enabled = 1 and org_id = #{org_id} and article_cat_path like concat('${article_cat_path}','%')")
    public void updateEnabledByCatPath(@Param("org_id") Long org_id, @Param("article_cat_path") String article_cat_path);

    //不显示
//	@Update("update article_cat set is_show = 2,update_time = now() where is_show = 1 and org_id = #{root_org_id} and article_cat_path like concat('${article_cat_path}','%')")
    @Update("update article_cat_org_rel r set r.is_show = 2,r.update_time = now() " +
            "where r.is_show = 1 and r.org_id = #{root_org_id} and r.article_cat_id in(select article_cat_id from article_cat where article_cat_path like concat('${article_cat_path}','%'))")
    public void updateShowByCatPath(@Param("root_org_id") Long root_org_id, @Param("article_cat_path") String article_cat_path);

    //显示
//	@Update("update article_cat set is_show = 1,update_time = now() where is_show = 2 and org_id = #{root_org_id} and article_cat_id in(${cat_ids}) ")
    @Update("update article_cat_org_rel r set r.is_show = 1,r.update_time = now() " +
            "where r.is_show = 2 and r.org_id = #{root_org_id} and r.article_cat_id in(${cat_ids})")
    public void updateShowByCatIds(@Param("root_org_id") Long root_org_id, @Param("cat_ids") String cat_ids);

    //查询机构的资讯分类
    @Select("select * from article_cat where org_id = #{0}")
    public List<Long> selectOrgCatIds(Long org_id);

    //查找分类
    @Select("select * from article_cat where org_id = #{0} and article_cat_id = #{1}")
    public ArticleCat selectOrgCatByCatId(Long org_id, Long pid);

    //通过cat_ids找到full_paths
    @Select("select article_cat_path from article_cat where article_cat_id in(${cat_ids})")
    public List<String> getFullPathsByCatIds(@Param("cat_ids") String cat_ids);

    //通过full_paths找到article_cat_ids
    @Select("select article_cat_id from article_cat where article_cat_path like '${full_path}%'")
    public List<Long> getCatIdsByFullPath(@Param("full_path") String full_path);

    //通过cat_ids删除资讯
//	@Delete("delete from article_cat where article_cat_id in (${cat_ids}) ")
    @Update("update article_cat set is_delete = 1, update_time = now() where article_cat_id in (${cat_ids}) ")
    public void deleteArticleCatsByIds(@Param("cat_ids") String cat_ids);

    @Select("select c.* from article_cat_org_rel r " +
            "left join article_cat c on c.article_cat_id = r.article_cat_id " +
            "where r.org_id = #{0} and c.pid = 0 and c.enabled = 1 and r.is_show = 1 and c.is_delete = 2 and r.is_delete = 2")
    public List<ArticleCat> selectSiteCatsByOrgId(Long org_id);

    @Select("select soe.extend_code org_code,soe.short_name,so.org_name from sys_organization so " +
            " left join sys_org_extend soe on(soe.org_id = so.org_id) " +
            " where so.enabled = 1 and so.is_delete = 2 and soe.is_delete = 2")
    public List<ArticleCatOrgBean> getAddOrgs();

    @Select("select soe.extend_code org_code,soe.short_name,so.org_name,r.create_time,r.article_cat_id,r.org_id from article_cat_org_rel r " +
            "left join sys_organization so on(so.org_id = r.org_id) " +
            "left join sys_org_extend soe on(soe.org_id = so.org_id) " +
            "where so.enabled = 1 and so.is_delete = 2 and soe.is_delete = 2 and r.article_cat_id = #{0} and r.is_delete = 2 ")
    public List<ArticleCatOrgBean> getRemoveOrgs(Long article_cat_id);

    @Update("update article_cat set org_count = (org_count + #{1}), update_time = now() where article_cat_id = #{0}")
    public void updateOrgCount(Long article_cat_id, Integer num);

    @Select("select c.* from article_cat c " +
            "left join article_cat_org_rel r on c.article_cat_id = r.article_cat_id " +
            "where r.org_id = #{0} and c.article_cat_path like concat(#{1},'%') and c.is_delete = 2 and c.enabled = 1 and r.is_delete = 2 and r.is_show = 1")
    public List<ArticleCatBean> getOwnerCats(Long org_id, String article_cat_path);

    @Select("select c.article_cat_id,c.org_id,c.pid, c.article_cat_name, c.article_cat_path, c.enabled, c.remark, c.cat_pic, c.is_delete org_is_delete,c.create_time,c.update_time,c.org_count, r.is_show, r.order_weight, r.is_delete from article_cat_org_rel r" +
            " left join article_cat c on c.article_cat_id = r.article_cat_id " +
            " where r.org_id = #{0} and (" +
            " c.update_time >#{1} or" +
            " r.update_time >#{1} " +
            ") limit #{2}, #{3}")
    public List<ArticleCatBean> getOffLineNumList(Long orgid, String oldtimestamp, Integer pageNum, Integer pageSize);

    @Select("select count(*) as num from " +
            "(select r.* from article_cat_org_rel r left join article_cat c on c.article_cat_id = r.article_cat_id " +
            "where r.org_id = #{0} and (c.update_time >#{1} or r.update_time >#{1})) a")
    public Integer checkOffLineNum(Long orgid, String timev);

    @SelectProvider(type = ArticleCatProvider.class, method = "getOrgQuery")
    public List<ArticleCatOrgBean> getOrgQuery(@Param("bean") FindCatOrgBean bean);

    @SelectProvider(type = ArticleCatProvider.class, method = "getOrgCat")
    public List<ArticleCatBean> getEnabledCats(@Param("org_id") Long org_id);

    @Select("select * from article_cat where article_cat_id = #{0} and org_id = #{1}")
    ArticleCat selectSourceById(Long cat_id,Long org_id);

    @Select("select r.rel_id,r.org_id,r.article_cat_id,r.order_weight,r.is_show,r.source_id,r.source_type, " +
            "r.create_time,r.update_time,r.is_delete,c.pid,c.article_cat_name,c.article_cat_path, " +
            "c.enabled,c.remark,c.cat_pic,c.org_count  from article_cat_org_rel r " +
            "left join article_cat c on r.article_cat_id = c.article_cat_id " +
            "where c.is_delete = 2 and  c.enabled =1 and  r.org_id = #{0} and r.is_show = 1 and r.is_delete = 2 " +
            "order by length(c.article_cat_path)-length(replace(c.article_cat_path,'|','')) asc, r.order_weight desc")
    List<ArticleCatBean> getAllCats(Long org_id);
}
