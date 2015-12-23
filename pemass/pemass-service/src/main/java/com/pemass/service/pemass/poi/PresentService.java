/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.domain.jpa.poi.PresentItem;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.PresentItemTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;

import java.util.List;

/**
 * @Description: PresentService
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 17:19
 */
public interface PresentService {

    /**
     * 保存红包
     *
     * @param present
     * @return
     */
    void savePresent(Present present);

    /**
     * 根据红包ID获取红包
     *
     * @param presentId
     * @return
     */
    Present getById(Long presentId);

    /**
     * 统计商户未发放的红包数量
     *
     * @param organizationId 商户所属机构id
     * @return
     */
    Long getNoneIssuePresentCountByOrganizationId(Long organizationId);

    /**
     * 根据红包批次id查询红包
     *
     * @param presentPackId 红包批次id
     * @return
     */
    List<Present> selectPresentByPackId(Long presentPackId);

    /**
     * 根据用户id,查询所用可使用红包
     *
     * @param uid
     * @return
     */
    DomainPage selectPack(Long uid,PresentStatusEnum presentStatus, long pageSize, long pageIndex);


    /**
     * 根据不同状态查询用户红包
     *
     * @param uid
     * @param presentStatus
     * @param pageSize
     * @param pageIndex
     * @return
     */
    DomainPage selectHasReceivePresentByOrganizationId(Long organizationId, long pageIndex, long pageSize);

    /**
     * 获取 presentItems
     *
     * @param presentId
     * @return
     */
    List<PresentItem> getPresentItemsByPresentId(Long presentId);


    /**
     * C端用户包红包
     *
     * @param uid
     * @param presentName
     * @param pointType
     * @param amount
     * @param isGeneral
     * @param orderTicketId
     * @param instruction
     * @return
     */
    Present pack(Long uid, String presentName, PointTypeEnum pointType, Integer amount, Integer isGeneral, Long[] orderTicketId, String instruction);


    /**
     * 赠送红包
     *
     * @param username  用户名（赠送手机号）
     * @param presentId 红包id
     * @return
     */
    Boolean grant(Long presentId, String username);


    /**
     * C端用户包红包和赠送红包
     *
     * @param uid           用户ID
     * @param presentName   红包名称
     * @param pointType     积分类型
     * @param amount        数量
     * @param isGeneral     积分是否通用
     * @param orderTicketId 电子票列表
     * @param username      接受用户名
     * @param instruction   须知
     * @return
     */
    Boolean packAndGrant(Long uid, String presentName, PointTypeEnum pointType, Integer amount, Integer isGeneral, Long[] orderTicketId,
                         String username, String instruction);

    /**
     * 拆红包
     *
     * @param presentId 红包ID
     * @return
     */
    Present unpack(Long presentId);

    /**
     * 修改红包
     *
     * @param present
     * @return
     */
    Present updatePresent(Present present);

    /**
     * 更新红包的状态
     * @param auditStatus
     * @param presentPackId
     * @return
     */
    boolean updatePresentAuditStatus(AuditStatusEnum auditStatus, Long presentPackId);

    /**
     * 获取用户未拆红包数
     *
     * @param uid
     * @return
     */
    Integer selectPack(Long uid);

    /**
     * 统计某个批次红包消耗某个批次积分总数
     *
     * @param presentPackID   红包批次ID
     * @param presentItemType 红包内容类型
     * @return 返回的总数
     */
    long getTotalPointByPresentPackID(Long presentPackID, PresentItemTypeEnum presentItemType);
}
