/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.log;


import com.pemass.persist.domain.mongo.log.LogMessage;

/**
 * @Description: LogService
 * @Author: estn.zuo
 * @CreateTime: 2014-12-12 16:54
 */
public interface LogService {

    boolean insert(LogMessage logMessage);
}
