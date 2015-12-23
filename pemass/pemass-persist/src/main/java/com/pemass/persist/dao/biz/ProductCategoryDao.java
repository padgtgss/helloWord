package com.pemass.persist.dao.biz;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.biz.ProductCategory;

import java.util.List;

/**
 * Created by Administrator on 2015-4-27.
 */
public interface ProductCategoryDao extends BaseDao {

    List<ProductCategory> getSecondLevelCategory();
}
