/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.biz;


import com.pemass.common.core.pojo.Body;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.News;
import com.pemass.persist.enumeration.NewsTypeEnum;
import com.pemass.pojo.biz.NewsPojo;
import com.pemass.pojo.sys.BodyPojo;
import com.pemass.service.pemass.biz.NewsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: OrderController
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 09:40
 */
@Controller
@RequestMapping("/news")
public class NewsController {

    private Log logger = LogFactory.getLog(NewsController.class);

    @Resource
    private NewsService newsService;


    /**
     * 获取新闻列表--云钱包首页banner
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @ResponseBody
    public Object getNewslist(String newsType,Long pageIndex,Long pageSize) {
        Map<String,Object> param = new HashMap<String, Object>();
        if (newsType.equals("CLOUD_MONEY_BANNER"))
        {
            param.put("newsType", NewsTypeEnum.CLOUD_MONEY_BANNER);
        }else if (newsType.equals("COMMUNICATION"))
        {
            param.put("newsType", NewsTypeEnum.COMMUNICATION);
        }


        DomainPage domainPage = newsService.getNewsList(param, pageIndex, pageSize);
        List<News> newsList = domainPage.getDomains();
        List<NewsPojo> newsPojoList = new ArrayList<NewsPojo>();
        for (int i=0;i<newsList.size();i++)
        {
            News news = newsList.get(i);
            NewsPojo newsPojo = new NewsPojo();
            MergeUtil.merge(news,newsPojo);
            newsPojo.setContent(null);//将内容值为空
            newsPojo.setSequence(null);
            newsPojo.setNewsType(null);
            newsPojo.setIssueTime(news.getIssueTime());
            newsPojoList.add(newsPojo);
        }
        domainPage.getDomains().clear();
        domainPage.setDomains(newsPojoList);
        return domainPage;
    }


    /**
     * 获取新闻详情
     * @param newsId
     * @return
     */
    @RequestMapping(value = "/{newsId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getNewsInfo(@PathVariable("newsId")Long newsId) {

        News news = newsService.getNewsInfo(newsId);
        NewsPojo newsPojo = new NewsPojo();
        MergeUtil.merge(news,newsPojo);
        newsPojo.setIssueTime(news.getIssueTime());
        newsPojo.setSequence(null);
        newsPojo.setNewsType(null);

        /**转换详情内容*/
        List<BodyPojo> bodyPojos = new ArrayList<BodyPojo>();
        for (Body body : news.getContent()) {
            BodyPojo bodyPojo = new BodyPojo();
            MergeUtil.merge(body, bodyPojo);
            bodyPojos.add(bodyPojo);
        }
        newsPojo.setContent(bodyPojos);

        return newsPojo;
    }
}

