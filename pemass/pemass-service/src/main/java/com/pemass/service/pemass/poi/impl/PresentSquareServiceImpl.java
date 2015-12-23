/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.IssueStrategyDao;
import com.pemass.persist.dao.poi.PresentSquareDao;
import com.pemass.persist.domain.jpa.poi.IssueStrategy;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.domain.jpa.poi.PresentSquare;
import com.pemass.persist.enumeration.PresentStatusEnum;
import com.pemass.service.exception.PoiError;
import com.pemass.service.pemass.poi.PresentService;
import com.pemass.service.pemass.poi.PresentSquareService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: PresentSquareServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2014-12-10 10:35
 */
@Service
public class PresentSquareServiceImpl implements PresentSquareService {

    @Resource
    private PresentSquareDao presentSquareDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentService presentService;

    @Resource
    private IssueStrategyDao issueStrategyDao;

    @Override
    public List<PresentSquare> selectByIssueId(Long issueStrategyId) {
        return presentSquareDao.getEntitiesByField(PresentSquare.class, "issueStrategyId", issueStrategyId);
    }

    @Override
    public List<Object[]> selectByUserId(Long uid) {
        Preconditions.checkNotNull(uid);
        return presentSquareDao.selectByUserId(uid);
    }

    @Transactional
    @Override
    public Present grabPresent(Long uid, Long issueStrategyId) {
        Preconditions.checkNotNull(uid);
        Preconditions.checkNotNull(issueStrategyId);

        IssueStrategy strategy = jpaBaseDao.getEntityById(IssueStrategy.class, issueStrategyId);

        /** 1.判断该用户是否已经领取 */
        if (issueStrategyDao.hasAlreadyGrab(uid, issueStrategyId, strategy.getEndTime())) {
            throw new BaseBusinessException(PoiError.PRESENT_HAS_ALREADY_GRAB);
        }
        /** 2.取出一个未领取的红包 */
        Long presentSquareId = issueStrategyDao.popupPresentSquareId(issueStrategyId);
        if (presentSquareId == null) {
            throw new BaseBusinessException(PoiError.PRESENT_HAS_GRAB_FINISH);
        }

        /** 3.用户领取红包 */
        PresentSquare presentSquare = jpaBaseDao.getEntityById(PresentSquare.class, presentSquareId);
        presentSquare.setIsClaim(1);
        presentSquare.setUserId(uid);

        jpaBaseDao.merge(presentSquare);
        Present present = presentService.getById(presentSquare.getPresentId());
        present.setUserId(uid);
        present.setPresentStatus(PresentStatusEnum.HAS_ISSUE);
        jpaBaseDao.merge(present);

        return present;

    }

    @Transactional
    @Override
    public Present grabAndUnpack(Long userId, Long issueStrategyId) {
        Present present = this.grabPresent(userId, issueStrategyId);
        present = presentService.unpack(present.getId());
        return present;
    }

    @Override
    public DomainPage selectAllPresentSquare(long pageIndex, long pageSize) {

        return presentSquareDao.selectPresentSquare(pageIndex, pageSize);
    }

    @Override
    public List<PresentSquare> selectPresentSquareList(Map<String, Object> map) {
        return jpaBaseDao.getEntitiesByFieldList(PresentSquare.class, map);
    }

    @Override
    public void deletePresentSquare(Long presentSquareId) {
        PresentSquare presentSquare = jpaBaseDao.getEntityById(PresentSquare.class, presentSquareId);
        presentSquare.setAvailable(AvailableEnum.UNAVAILABLE);
        jpaBaseDao.merge(presentSquare);
    }

    @Override
    public boolean hasGrabPresent(Long userId, Long issueStrategyId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userId);
        param.put("issueStrategyId", issueStrategyId);
        List presentSquareList = jpaBaseDao.getEntitiesByFieldList(PresentSquare.class, param);
        return presentSquareList.size() > 0;
    }

    @Override
    public PresentSquare getPresentSquareById(Long presentSquareId) {
        return jpaBaseDao.getEntityById(PresentSquare.class, presentSquareId);
    }

    @Override
    public PresentSquare updatePresentSquare(PresentSquare presentSquare) {
        jpaBaseDao.merge(presentSquare);
        return presentSquare;
    }

    @Override
    public Integer getTotalByIssueStrategyId(Long issueStrategyId) {
        return presentSquareDao.getTotalByIssueStrategyId( issueStrategyId);
    }

}