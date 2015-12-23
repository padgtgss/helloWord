/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.service.pemass.bas.VersionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * @Description: VersionServiceTest
 * @Author: estn.zuo
 * @CreateTime: 2014-11-24 16:00
 */
@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:applicationContext-persist.xml","classpath*:applicationContext-jms.xml"})
public class VersionServiceTest extends AbstractTestNGSpringContextTests {

    @Resource
    private VersionService versionService;

    @Test
    public void testCompareWithLatestVersion() throws Exception {
    }

    @Test
    public void testInsert() throws Exception {
        //Version version = new Version();
        //version.setAppType("B");
        //version.setBuildNumber(2);
        //version.setDownloadUrl("https://");
        //version.setLatestVersion("0.0.1");
        //version.setEnforce(false);
        //version.setBuildTime(new Date());
        //
        //versionService.insert(version);
    }
}
