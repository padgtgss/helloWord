package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.dao.poi.PointPoolDao;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;
import com.pemass.persist.domain.jpa.poi.PointPool;
import com.pemass.persist.domain.jpa.poi.PointPoolOrganization;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.poi.PointPoolOrganizationService;
import com.pemass.service.pemass.poi.PointPoolService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: PointPoolServiceImpl
 * Author: estn.zuo
 * CreateTime: 2014-10-10 15:19
 */
@Service
public class PointPoolServiceImpl implements PointPoolService {
    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PointPoolDao pointPoolDao;

    @Resource
    private PointPoolOrganizationService pointPoolOrganizationService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private SequenceService sequenceService;

    @Override
    public PointPool insert(PointPool pointPool) {
        //生成积分编号并赋值
        String identifier = sequenceService.obtainSequence(SequenceEnum.POINT_POOL);
        pointPool.setIssueIdentifier(identifier);
        pointPool.setAmount(0);
        //设置积分发行时间
        pointPool.setIssueTime(new Date());
        //设置积分池中积分的过期时间(默认很久以后)
        pointPool.setExpiryTime(SystemConst.FUTURE_TIME);
        //持久化实体
        jpaBaseDao.persist(pointPool);
        return pointPool;
    }


    @Override
    public PointPool update(PointPool pointPool) {
        return jpaBaseDao.merge(pointPool);
    }

    @Override
    public PointPool getById(Long id) {
        Preconditions.checkNotNull(id);
        return jpaBaseDao.getEntityByField(PointPool.class, "id", id);
    }

    @Override
    public DomainPage selectAllEntitiesByPage(Map<String, Object> fieldValueMap, long pageIndex, long pageSize) {
        Preconditions.checkNotNull(fieldValueMap);
        return pointPoolDao.getAllPointPoolByPages(fieldValueMap, pageIndex, pageSize);
    }


    @Override
    public List<PointPool> selectPointPoolByOrganization(Long organizationId, PointTypeEnum pointType) {
        Preconditions.checkNotNull(organizationId);
        Preconditions.checkNotNull(pointType);
        List<PointPool> target = new ArrayList<PointPool>(); //存放结果的集合
        /**1.从积分池与商户的关系表中，获取与该商户有关系的积分池id*/
        List<PointPoolOrganization> pointPoolOrganizations = pointPoolOrganizationService.selectByOrganizationId(organizationId);
        /**2.从积分池中根据这些积分池id查询信息,并添加到target集合中*/
        if (pointPoolOrganizations != null) {
            for (PointPoolOrganization poolOrganization : pointPoolOrganizations) {
                PointPool pointPool = this.getById(poolOrganization.getPointPoolId());
                if (pointPool != null) {
                    if (pointType.equals(pointPool.getPointType())) {
                        target.add(pointPool);
                    }
                }
            }
        }
        /**3.从积分池中查询出不是特约积分的积分*/
        Expression expression = new Expression();
        expression.setFiledName("area");
        expression.setOperation(Operation.LeftLike);
        expression.setFiledValue("%:00:00:00:00");
        List<PointPool> result = jpaBaseDao.getEntitiesByExpression(PointPool.class, expression);
        /**4.循环result集合,把满足条件的数据添加到target集合中*/
        if (result.size() > 0) {
            Organization organization = organizationService.getOrganizationById(organizationId);
            for (PointPool pool : result) {
                //判断积分池中的数据和参数是否一致
                if (pointType.equals(pool.getPointType())) {
                    @SuppressWarnings("unchecked") List<String> list = this.splitArea(pool.getArea());
                    String provinceId = organization.getProvinceId().toString();
                    //判断积分的区域，如果list中的第一个元和“00”是否相等
                    if ("00".equals(list.get(0))) {
                        target.add(pool);
                    } else { //否则，判断list中的第一个元素和该商户的省份id是否相等
                        if (provinceId.equals(list.get(0))) {
                            target.add(pool);
                        }
                    }
                }
            }
        }
        return target;
    }

    @Override
    public List<PointPool> selectPointPoolByExpression(List<Expression> expressions) {
        /**1.检查参数是否为空*/
        Preconditions.checkNotNull(expressions);
        /**2.执行查询并返回结果*/
        @SuppressWarnings("unchecked") List<PointPool> resultList = jpaBaseDao.getEntitiesByExpressionList(PointPool.class, expressions);
        return resultList;
    }

    @Override
    public List<PointPool> selectArrangementPointPools() {
        List<Expression> expressions = Lists.newArrayList();
        Expression expression = new Expression("area", Operation.RightLike, "%?");
        expressions.add(expression);
        return selectPointPoolByExpression(expressions);
    }

    @Override
    public List<PointPool> selectPointPoolByField(String fieldName, Object fieldValue) {
        return jpaBaseDao.getEntitiesByField(PointPool.class, fieldName, fieldValue);
    }

    @Transactional
    @Override
    public void recyclePointE(Long pointPoolId, Integer amount) {
        pointPoolDao.recycle(pointPoolId, amount);
    }

    @Transactional
    @Override
    public void recyclePointO(OnePointConsumeDetail onePointConsumeDetail) {
        pointPoolDao.recycle(onePointConsumeDetail.getAmount());
    }

    @Override
    public PointPool getPointPool() {
        return pointPoolDao.getPointPool();
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
