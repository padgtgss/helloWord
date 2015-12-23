package com.pemass.service.pemass.poi;


import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.PointPoolOrganization;

import java.util.List;

/**
 * @Description: 积分池与商户的关系
 * @Author: cassiel.liu
 * @CreateTime: 2015-07-16 22:12
 */
public interface PointPoolOrganizationService {

    /**
     * 新增一条记录
     *
     * @param pointPoolOrganization
     * @return
     */
    PointPoolOrganization insert(PointPoolOrganization pointPoolOrganization);

    /**
     * 更新积分池和组织机构关系表
     *
     * @param source
     * @return
     */
    PointPoolOrganization update(PointPoolOrganization source);

    /**
     * 根据ID获取PointPoolOrganization
     *
     * @param pointPoolOrganizationId
     * @return
     */
    PointPoolOrganization selectById(long pointPoolOrganizationId);

    /**
     * 根据ID删除PointPoolOrganization
     *
     * @param pointPoolOrganizationId
     * @return
     */
    PointPoolOrganization deleteById(long pointPoolOrganizationId);

    /**
     * 根据上商户id查询所有该商户可以认购的积分池id
     *
     * @param organizationId
     * @return
     */
    List<PointPoolOrganization> selectByOrganizationId(Long organizationId);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<PointPoolOrganization> selectAllPointPoolOrganization();

    /**
     * 分页查询所有数据
     *
     * @return
     */
    DomainPage selectAllPointPoolOrganization(long pageIndex, long pageSize);

}
