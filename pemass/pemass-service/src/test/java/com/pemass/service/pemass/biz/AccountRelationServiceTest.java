package com.pemass.service.pemass.biz;

import com.pemass.persist.dao.biz.AccountRelationDao;
import com.pemass.persist.dao.biz.ProductDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:applicationContext-persist.xml","classpath*:applicationContext-jms.xml"})
public class AccountRelationServiceTest extends AbstractTestNGSpringContextTests {
   @Resource
    private AccountRelationService accountRelationService;
    @Resource
    private AccountRelationDao accountRelationDao;
    @Resource
    private ProductDao productDao;

    @Test
    public void testGetAccountRelationList() throws Exception {
//        DomainPage domainPage = accountRelationDao.screeningOfMerchants("2",1L,100L);
//        System.out.println(domainPage);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("organization.id", 1L);
//        map.put("productStatus", 2);
//        List<Product> products = productDao.getShelvesOfProducts(map);
        productDao.getRelatedProducts(1L);
    }

    @Test
    public void testGetConfigInfo() throws Exception {
        //accountRelationService.updateOverrule(28L);
    }

    @Test
    public void testUpdateRelieveRelation() throws Exception {
        //accountRelationService.updateRelieveRelation(1l,9l);
    }

    @Test
    public void testUpdateRepealApplication() throws Exception {

    }

    @Test
    public void testInsertApplyFor() throws Exception {

    }

    @Test
    public void testUpdateOverrule() throws Exception {

    }

    @Test
    public void testUpdatePass() throws Exception {
            //accountRelationService.updatePass(1L);
    }

    @Test
    public void testUpdateApplyForAgain() throws Exception {

    }

    @Test
    public void testGetAccountConfigList() throws Exception {

    }
}