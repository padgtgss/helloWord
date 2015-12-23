package com.pemass.service.pemass.bas.impl;

import com.pemass.persist.domain.jpa.bas.Resources;
import com.pemass.persist.enumeration.ResourceType;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.pemass.bas.ResourcesService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath*:applicationContext.xml",
        "classpath:applicationContext-persist.xml"})
public class ResourcesServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private ResourcesService resourcesService;

    @Test
    public void testUpdateBatch() throws Exception {
        resourcesService.updateBatch(null, PemassConst.INDEX_IMAGE_UUID);
    }

    @Test
    public void testSave() throws Exception {
        Resources resources = new Resources();
        resources.setResourceType(ResourceType.IMAGE);
        resources.setTargetUUID("12313");
        resources.setUrl("123");
        resourcesService.saveResources(resources);
    }



}