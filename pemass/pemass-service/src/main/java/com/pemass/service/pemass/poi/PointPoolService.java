package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.domain.Expression;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;
import com.pemass.persist.domain.jpa.poi.PointPool;
import com.pemass.persist.enumeration.PointTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by estn.zuo on 14-10-10.
 */
public interface PointPoolService {

    /**
     * 保存数据
     *
     * @param pointPool
     * @return
     */
    PointPool insert(PointPool pointPool);

    /**
     * 修改数据
     *
     * @param pointPool
     * @return
     */
    PointPool update(PointPool pointPool);

    /**
     * 根据id，查询一个实体
     *
     * @param id
     * @return
     */
    PointPool getById(Long id);

    /**
     * 根据条件分页获取发行的积分
     * @param fieldValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectAllEntitiesByPage(Map<String, Object> fieldValueMap, long pageIndex, long pageSize);

    /**
     * 根据商户id和积分类型，查询出该商户可以认购的积分池信息
     *
     * @param organizationId
     * @param pointType
     * @return
     */
    List<PointPool> selectPointPoolByOrganization(Long organizationId, PointTypeEnum pointType);

    /**
     * 根据积分类型，查询出该类型的所有特约积分
     *
     * @param expressions
     * @return
     */
    List<PointPool> selectPointPoolByExpression(List<Expression> expressions);

    /**
     * 获取特约积分
     *
     * @return
     */
    List<PointPool> selectArrangementPointPools();

    /**
     * 根据某个字段查询
     *
     * @param fieldName
     * @param fieldValue
     * @return
     */
    List<PointPool> selectPointPoolByField(String fieldName, Object fieldValue);


    /**
     * 回收E积分
     */
    void recyclePointE(Long pointPoolId, Integer amount);


    /**
     * 回收一元购积分
     *
     * @param onePointConsumeDetail
     */
    void recyclePointO(OnePointConsumeDetail onePointConsumeDetail);

    /**
     * 获取特约积分详情，目前只有体育总局
     *
     * @return
     */
    PointPool getPointPool();
}
