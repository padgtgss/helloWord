package com.pemass.service.pemass.poi.impl;

import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.domain.jpa.poi.PointPool;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.pemass.poi.PointPoolService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/18.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class PointPoolServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private PointPoolService pointPoolService;

    @Test
    public void testSelectPointPoolByOrganization() throws Exception {

        List<PointPool> list = pointPoolService.selectPointPoolByOrganization(1L, PointTypeEnum.P);
        System.out.println(list.size()+"-----------------");

    }

    @Test
    public void testSelectPointPoolByPointType() throws Exception{
        List<Expression> expressionList = new ArrayList<Expression>();
        Expression exp = new Expression("area", Operation.LeftLike,"%:00:00:00:00");
        Expression exp2 = new Expression("pointType",Operation.Equal,PointTypeEnum.P);
        expressionList.add(exp);
        expressionList.add(exp2);

        List<PointPool> result = pointPoolService.selectPointPoolByExpression(expressionList);
        System.out.println("------------------"+result.size());

    }

}