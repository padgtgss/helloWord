/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.dao.poi.PointPurchaseDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.poi.*;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.poi.PointPoolOrganizationService;
import com.pemass.service.pemass.poi.PointPoolService;
import com.pemass.service.pemass.poi.PointPurchaseService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * @Description: PointPurchaseServiceImpl
 * @Author: cassiel.liu
 * @CreateTime: 2014-10-13 17:29
 */
@Service
public class PointPurchaseServiceImpl implements PointPurchaseService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PointPurchaseDao pointPurchaseDao;

    @Resource
    private PointPoolOrganizationService pointPoolOrganizationService;
    @Resource
    private PointPoolService pointPoolService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private SequenceService sequenceService;

    @Override
    public PointPurchase insert(PointPurchase pointPurchase) {
        /**1.如果选择自动认购方式认购，则指定某一个积分池id*/
        if (pointPurchase.getIsAutomatic() == 1) {
            pointPurchase = this.automaticPurchaseChooseId(pointPurchase);
            pointPurchase.setCharge(0d);
            pointPurchase.setIsClear(1);
            pointPurchase.setAuditStatus(AuditStatusEnum.HAS_AUDIT);
            /*自动认购的时候，修改积分池数据*/
            PointPool targetPool = pointPoolService.getById(pointPurchase.getPointPoolId());
            targetPool.setAmount(targetPool.getAmount() + pointPurchase.getAmount());
            pointPoolService.update(targetPool);
        }
        /**2.设置积分认购编号*/
        String purchaseIdentifier = sequenceService.obtainSequence(SequenceEnum.POINT_PURCHASE);
        pointPurchase.setPurchaseIdentifier(purchaseIdentifier);
        /**3.默认过期时间*/
        pointPurchase.setExpiryTime(SystemConst.FUTURE_TIME);
        /**4.认购积分*/
        pointPurchase.setPurchaseTime(new Date());
        jpaBaseDao.persist(pointPurchase);


        return pointPurchase;
    }


    @Override
    public DomainPage getAllListByPage(Map<String, Object> map, Map<String, Object> fuzzyMap, Long pageIndex, Long pageSize) {
        return pointPurchaseDao.getEntitiesPages(map, fuzzyMap, pageIndex, pageSize);
    }

    @Override
    public PointPurchase getById(Long pointPurchaseId) {
        return jpaBaseDao.getEntityById(PointPurchase.class, pointPurchaseId);
    }


    @Override
    public void updateEntity(PointPurchase pointPurchase) {
        //如果积分认购的审核状态是“审核通过”
        if (pointPurchase.getAuditStatus() == AuditStatusEnum.HAS_AUDIT) {
            //根据积分认购记录中的积分池的id,查询出该积分池的信息,并修改积分池中的数量
            PointPool pointPool = pointPoolService.getById(pointPurchase.getPointPoolId());
            Integer amount = pointPool.getAmount() + pointPurchase.getAmount();
            pointPool.setAmount(amount);
            pointPoolService.update(pointPool);
        }
        //修改“认购记录”
        jpaBaseDao.merge(pointPurchase);
    }

    @Override
    public DomainPage getAuditRecordsByPage(Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        return pointPurchaseDao.getAuditRecordByPages(fuzzyMap, pageIndex, pageSize);
    }

    @Override
    public DomainPage<PointPurchase> getEntitiesByPages(String filedName, Object start, Object end, Map<String, Object> filedMap, long pageIndex, long pageSize) {
        return pointPurchaseDao.getEntitiesPagesByTime(PointPurchase.class, filedName, start, end, filedMap, pageIndex, pageSize);
    }

    @Override
    public DomainPage getPointPaymentDetails(String pointType, Long organizationId, Map<String, Object> fieldMap, long pageIndex, long pageSize) {
        //获取积分明细的总记录数
        Long totalCount = pointPurchaseDao.getPointPaymentDetailsCount(pointType, organizationId, fieldMap);

        //获取积分明细
        List<Object[]> list = pointPurchaseDao.getPointPaymentDetails(pointType, organizationId, fieldMap, pageIndex, pageSize);

        //获取封装后的List集合
        List<Object[]> result = this.encapsulationResult(list);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }

    @Override
    public List getPointPurchaseIdByOrganizationId(Long organizationId) {
        return pointPurchaseDao.getPointPurchaseIdByOrganizationId(organizationId);
    }

    @Override
    public DomainPage getPointUsedDetails(PointPurchase pointPurchase, long pageIndex, long pageSize) {
        return pointPurchaseDao.getPointUsedDetailsByFiled(OrganizationConsumeDetail.class, pointPurchase, pageIndex, pageSize);
    }

    /**
     * 根据条件查询认购记录
     *
     * @param map
     * @return
     */
    @Override
    public List<PointPurchase> getPointPurchaseList(Map<String, Object> map) {
        return jpaBaseDao.getEntitiesByFieldList(PointPurchase.class, map);
    }

    @Override
    public DomainPage getValidPointPurchase(Map<String, Object> conditions, DomainPage domainPage) {
        Preconditions.checkNotNull(conditions);
        Preconditions.checkNotNull(domainPage);
        return pointPurchaseDao.getValidPointPurchase(conditions, domainPage);
    }

    @Override
    public List<PointPurchase> getValidPointPurchaseList(Map<String, Object> conditions) {
        Preconditions.checkNotNull(conditions);
        return pointPurchaseDao.getValidPointPurchaseList(conditions);
    }

    @Override
    public void savePointPurchase(PointPurchase pointPurchase) {
        jpaBaseDao.persist(pointPurchase);
    }

    @Override
    public Long getPurchaseAmountOfDayByType(PointTypeEnum pointType) {
        return pointPurchaseDao.getPurchaseAmountOfDayByType(pointType);
    }

    /**
     * 把获取到的积分收支明细进行封装
     *
     * @param list list集合
     * @return
     */
    private List encapsulationResult(List<Object[]> list) {
        List<Object[]> result = new ArrayList<Object[]>();
        //循环积分明细
        for (Object[] obj : list) {
            // obj[0]--编号/名称
            // obj[1]--积分认购数量或者包红包使用的数量，
            // obj[2]--使用方式,obj[3]--使用时间
            Object[] objects = new Object[4];

            BigInteger bigInteger = (BigInteger) obj[0];
            Long id = bigInteger.longValue();
            String type = "";
            if ("PURCHASE".equals(obj[2])) { //如果是“认购积分”
                type = "认购积分";
                PointPurchase pointPurchase = jpaBaseDao.getEntityById(PointPurchase.class, id);
                if (pointPurchase != null) {
                    objects[0] = pointPurchase.getPurchaseIdentifier();
                }
            } else if ("ORDER".equals(obj[2])) { //如果是“购买商品抵扣”
                type = "商品购买赠送";
                Order order = jpaBaseDao.getEntityById(Order.class, id);
                if (order != null) {
                    objects[0] = order.getOrderIdentifier();
                }
            } else if ("PRESENT".equals(obj[2])) { //如果是“包红包抵扣”
                type = "包红包";
                Present present = jpaBaseDao.getEntityById(Present.class, id);
                if (present != null) {
                    PresentPack presentPack = jpaBaseDao.getEntityById(PresentPack.class, present.getPresentPackId());
                    objects[0] = presentPack.getPackIdentifier();
                }
            } else if ("STRATEGY".equals(obj[2])) { //如果是“营销策略”
                type = "营销策略发放";
                IssueStrategy issueStrategy = jpaBaseDao.getEntityById(IssueStrategy.class, id);
                if (issueStrategy != null) {
                    objects[0] = issueStrategy.getStrategyName();
                }
            } else if ("INTEGRAL_OVERDUE_RECOVERY".equals(obj[2])) { //如果是“积分过期回收”
                type = "积分过期回收";
                PointPool pointPool = jpaBaseDao.getEntityById(PointPool.class, id);
                if (pointPool != null) {
                    objects[0] = pointPool.getIssueIdentifier();
                }
            }
            objects[1] = obj[1];
            objects[2] = type;
            objects[3] = obj[3];
            result.add(objects);
        }
        return result;
    }

    /**
     * 积分自动认购时选择积分池id
     *
     * @return
     */
    private PointPurchase automaticPurchaseChooseId(PointPurchase pointPurchase) {
        /**1.判断是积分类型*/
        if (PointTypeEnum.E.equals(pointPurchase.getPointType()) || PointTypeEnum.O.equals(pointPurchase.getPointType())) {
            List<PointPool> pools = pointPoolService.selectPointPoolByField("pointType", pointPurchase.getPointType());
            if (pools.size() > 0) {
                PointPool pointPool = pools.get(0);
                pointPurchase.setArea(pointPool.getArea());
                pointPurchase.setPointPoolId(pointPool.getId());
            }
        } else {
            List<PointPoolOrganization> list = pointPoolOrganizationService.selectByOrganizationId(pointPurchase.getOrganizationId());
            /**2.自动认购定向积分*/
            if (list.size() > 0) {
                PointPoolOrganization poolOrganization = list.get(0);
                PointPool pointPool = pointPoolService.getById(poolOrganization.getPointPoolId());
                if (pointPool != null) {
                    pointPurchase.setPointPoolId(pointPool.getId());
                    String newArea = "00:00:00:00:*";
                    pointPurchase.setArea(newArea);
                } else {
                    pointPurchase = this.autoPurchaseCommonPoint(pointPurchase);
                }
            } else {
                /**3.获取省定向和通用积分*/
                pointPurchase = this.autoPurchaseCommonPoint(pointPurchase);
            }
        }
        return pointPurchase;
    }

    /**
     * 自动认购通用积分
     *
     * @param pointPurchase
     */
    private PointPurchase autoPurchaseCommonPoint(PointPurchase pointPurchase) {
        List<Expression> exceptions = new ArrayList<Expression>();
        Expression exp = new Expression("pointType", Operation.Equal, pointPurchase.getPointType());
        Expression exp2 = new Expression("area", Operation.LeftLike, "%:00:00:00:00");
        exceptions.add(exp);
        exceptions.add(exp2);
        List<PointPool> pointPools = pointPoolService.selectPointPoolByExpression(exceptions);
        if (pointPools.size() > 0) {
            for (PointPool pointPool : pointPools) {
                List<String> areaList = this.splitArea(pointPool.getArea());
                if (!"00".equals(areaList.get(0))) {
                    Organization organization = organizationService.getOrganizationById(pointPurchase.getOrganizationId());
                    Long provinceId = organization.getProvinceId();
                    Long targetId = Long.parseLong(areaList.get(0));
                    if (provinceId.equals(targetId)) {
                        pointPurchase.setArea("00:00:00:00:00");
                        pointPurchase.setPointPoolId(pointPool.getId());
                        break;
                    }
                    continue;
                } else {
                    pointPurchase.setArea("00:00:00:00:00");
                    pointPurchase.setPointPoolId(pointPool.getId());
                    break;
                }
            }
        }
        return pointPurchase;
    }

    /**
     * 分割区域
     *
     * @param area
     * @return
     */
    private List splitArea(String area) {
        List<String> idList = Splitter.on(SystemConst.REDIS_KEY_SEPARATOR_SYMBOL)
                .trimResults()
                .omitEmptyStrings()
                .splitToList(area);
        return idList;
    }
}
