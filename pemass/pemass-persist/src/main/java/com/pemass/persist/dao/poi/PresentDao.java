/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PresentItemTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;

/**
 * @Description: PresentDao
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 17:22
 */
public interface PresentDao extends BaseDao {
    /**
     * 统计用户未拆分的红包数量
     *
     * @param uid
     * @return
     */
    Long selectUserPackCount(Long uid);

    /**
     * 根据商户所属机构的id统计未发放的红包数量
     *
     * @param organizationId 商户所属机构的id
     * @return
     */
    Long getPresentCount(Long organizationId);

    /**
     * 根据不同状态查询红包
     *
     * @param uid
     * @param presentStatus
     * @param pageSize
     * @param pageIndex
     * @return
     */
    DomainPage selectPack(Long uid, PresentStatusEnum presentStatus, long pageSize, long pageIndex);

    /**
     * 查询出用户某一个红包详情
     *
     * @param uid       用户id
     * @param presentId 红包id
     * @return
     */
    Present selectPackDetail(Long uid, Long presentId);

    /**
     * 查询总条数
     *
     * @param uid
     * @return
     */
    long selectCount(Long uid);

    /**
     * 根据organizationId查询领取红包的用户
     *
     * @param organizationId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectReceivePresentById(Long organizationId, long pageIndex, long pageSize);

    /**
     * 更新红包的审核状态
     *
     * @param auditStatus
     * @param presentPackId
     * @return
     */
    boolean updateAuditStatus(AuditStatusEnum auditStatus, Long presentPackId);


    /**
     * 统计某个批次红包消耗某个批次积分总数
     *
     * @param presentPackID   红包批次ID
     * @param presentItemType 红包内容类型
     * @return 返回的总数
     */
    long getTotalPointByPresentPackID(Long presentPackID, PresentItemTypeEnum presentItemType);
}
