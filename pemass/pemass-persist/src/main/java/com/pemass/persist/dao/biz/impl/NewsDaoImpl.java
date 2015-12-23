/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.NewsDao;
import org.springframework.stereotype.Repository;

/**
 * @Description: NewsDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2014-12-09 10:04
 */
@Repository(value = "newsDao")
public class NewsDaoImpl extends JPABaseDaoImpl implements NewsDao {



}

