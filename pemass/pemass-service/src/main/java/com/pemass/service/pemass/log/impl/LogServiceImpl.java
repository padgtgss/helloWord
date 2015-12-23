/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.log.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.mongo.log.LogMessage;
import com.pemass.persist.domain.redis.Token;
import com.pemass.service.pemass.log.LogService;
import com.pemass.service.pemass.sys.TokenService;
import org.mongodb.morphia.Datastore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: LogServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-12-12 16:55
 */
@Service
public class LogServiceImpl implements LogService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private Datastore morphiaDatastore;

    @Resource
    private TokenService tokenService;

    @Override
    public boolean insert(LogMessage logMessage) {
        if (logMessage == null) {
            return false;
        }

        /*-- 从token中解析用户ID --*/
        if (logMessage.getToken() != null) {
            Token token = tokenService.retrieveToken(logMessage.getToken());
            if (token == null) {
                return true;
            }

            User user = jpaBaseDao.getEntityByField(User.class, "uuid", token.getTargetUUID());
            if (user != null) {
                logMessage.setUid(user.getId());
            }
        }
        morphiaDatastore.save(logMessage);

        return true;
    }

}
