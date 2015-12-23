/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz.impl;

import com.pemass.common.core.json.JacksonObjectMapper;
import com.pemass.persist.dao.biz.ProductDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.service.pemass.biz.ProductService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Description: ProductServiceImplTest
 * @Author: estn.zuo
 * @CreateTime: 2014-11-20 11:55
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-persist.xml"})
public class ProductServiceImplTest  extends AbstractTestNGSpringContextTests {
    private ObjectMapper jacksonObjectMapper = new JacksonObjectMapper();

    @Resource
    private ProductService productService;

    @Resource
    private ProductDao productDao;
    @Test
    public void testAddSampleGoods() throws Exception {
//        Product product = new Product();
//        Organization organization = new Organization();
//        organization.setId(1L);
//        product.setOrganization(organization);
//        product.setExpiry_time(new Date());
//        product.setIsInvoice(0);
//        product.setIsReturn(1);
//        product.setMarketPrice(1F);
//        product.setProductName("21");
//        product.setProductStatus(1);
//        product.setStockNumber(1);
//
//        List<Body> bodies = new ArrayList<Body>();
//        Body body = new Body();
//        body.setType(Body.BodyType.IMAGE);
//        body.setValue("sfsd");
//
//        Body body2 = new Body();
//        body2.setType(Body.BodyType.IMAGE);
//        body2.setValue("sfsd");
//
//        bodies.add(body);
//        bodies.add(body2);
//        product.setProductDetail(bodies);
//
//        // product.setProductDetail(productService.getProductDetailBodies(title, textImageUrl, content));
//        product.setProductIdentifier("123123");
//
//        Province province = new Province();
//        province.setId(2L);
//        product.setProvince(province);
//
//        City city = new City();
//        city.setId(3L);
//        product.setCity(city);
//
//        District district = new District();
//        district.setId(1L);
//
//        product.setDistrict(district);
//        product = productService.addSampleGoods(product);
//
//
//
//        System.out.println(product);
    }

    @Test
    public void getProductInfo() throws IOException {
        Product product = productService.getProductInfo(6L);
//        System.out.println(jacksonObjectMapper.writeValueAsString(product));
//        List<Long> list = new ArrayList<Long>();
//        list.add(1L);
//        list.add(2L);
//        list.add(3L);
//        productDao.deleteProduct(list);
    }

    @Test
    public void testGetAllProductAmount(){
        Long amount = productService.getAllProductAmount();
        System.out.println("++++++++所有商品的数量："+amount);

        amount = productService.getShelvesProductAmount();
        System.out.println("========所有上架商品的数量："+amount);
    }
}
