/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.bas;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.bas.Resources;

import java.util.List;

/**
 * @Description: ResourcesDao
 * @Author: estn.zuo
 * @CreateTime: 2014-11-03 16:11
 */
public interface ResourcesDao extends BaseDao {

    Integer deleteBatchByUUID(String targetUUID);


    List<Resources> selectAllEntities(String targetUUID);

}
