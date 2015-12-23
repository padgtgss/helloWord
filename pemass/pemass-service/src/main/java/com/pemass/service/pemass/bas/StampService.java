/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.bas;

/**
 * @Description: StampService
 * @Author: estn.zuo
 * @CreateTime: 2015-08-19 23:51
 */
public interface StampService {

    void verifyApiStamp(String stamp);

    /**
     * 保存Portal接口邮戳
     * <p/>
     * 将stamp初始值设置为-100
     *
     * @param stamp
     * @return
     */
    boolean setPortalStamp(String stamp);

    /**
     * 每校验一次stamp值加1
     * <p/>
     * 1.如果stamp加1后的值为：-99 表示校验成功
     * 2.如果stamp加1后的值大于-99小于0 表示重复调用
     * 3.如果stamp加1后的值大于0 表示未找到stamp值
     *
     * @param stamp
     */
    void verifyPortalStamp(String stamp);


}
