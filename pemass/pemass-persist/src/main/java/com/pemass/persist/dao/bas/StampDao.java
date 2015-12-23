/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.bas;

/**
 * @Description: StampDao
 * @Author: estn.zuo
 * @CreateTime: 2015-08-19 23:52
 */
public interface StampDao {


    boolean setApiStamp(String stamp, int expire);

    String getApiStamp(String stamp);

    boolean setPortalStamp(String stamp, int expire);

    Long incPortalStampValue(String stamp);

}
