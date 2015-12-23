/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.redis.App;
import common.AbstractPemassServiceTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * @Description: AppServiceTest
 * @Author: estn.zuo
 * @CreateTime: 2015-04-09 15:02
 */
public class AppServiceTest extends AbstractPemassServiceTest {

    @Resource
    private AppService appService;

    @Test
    public void testGetAppSecretById() throws Exception {
        App ret = appService.getAppById("40e9fc15ce9140fb96b58ce3e38a4f18");
        System.out.println(ret);
    }

    @Test
    public void testSaveApp() throws Exception {
        App app = new App();
        app.setAppname("yunpos1");
        System.out.println(appService.saveApp(app));
    }

    @Test
    public void testgetuuid() throws Exception {

    }


}
