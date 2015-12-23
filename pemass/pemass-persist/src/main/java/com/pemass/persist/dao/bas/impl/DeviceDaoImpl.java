package com.pemass.persist.dao.bas.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.bas.DeviceDao;
import com.pemass.persist.enumeration.DeviceTypeEnum;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: DeviceDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-09-29 11:33
 */
@Repository
public class DeviceDaoImpl extends JPABaseDaoImpl implements DeviceDao {
    @Override
    public Long getDownloadAmountByType(DeviceTypeEnum deviceType) {
        DateTime maxDate = DateTime.now().minusDays(1).millisOfDay().withMaximumValue();

        String sql = "select count(d.id) from bas_device d where d.available = 1 and d.device_type = ?1 and d.create_time <= ?2 ";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, deviceType.toString());
        query.setParameter(2, maxDate.toString("yyyy-MM-dd HH:mm:ss"));

        List result = query.getResultList();
        Long totalCount = 0L;
        if (result.get(0) != null) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }
}
