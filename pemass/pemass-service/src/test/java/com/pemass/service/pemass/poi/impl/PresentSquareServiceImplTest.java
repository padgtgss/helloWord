package com.pemass.service.pemass.poi.impl;

import com.pemass.service.pemass.poi.PresentSquareService;
import common.AbstractPemassServiceTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by estn on 15/8/6.
 */
public class PresentSquareServiceImplTest extends AbstractPemassServiceTest {

    @Resource
    private PresentSquareService presentSquareService;
    @Test
    public void testSelectByIssueId() throws Exception {

    }

    @Test
    public void testSelectByUserId() throws Exception {

    }

    @Test
    public void testGrabPresent() throws Exception {
        presentSquareService.grabPresent(9L, 16L);
    }

    @Test
    public void testSelectAllPresentSquare() throws Exception {

    }

    @Test
    public void testSelectPresentSquareList() throws Exception {

    }

    @Test
    public void testDeletePresentSquare() throws Exception {

    }

    @Test
    public void testHasGrabPresent() throws Exception {

    }

    @Test
    public void testGetPresentSquareById() throws Exception {

    }

    @Test
    public void testUpdatePresentSquare() throws Exception {

    }
}