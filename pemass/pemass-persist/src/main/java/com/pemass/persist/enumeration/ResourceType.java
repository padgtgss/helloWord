package com.pemass.persist.enumeration;/**
 * Created by estn.zuo on 14-10-14.
 */

/**
 * @Description: 资源类型
 * @Author: estn.zuo
 * @CreateTime: 2014-10-14 11:22
 */
public enum ResourceType {
    /**
     * 0-图片资源
     */
    IMAGE,
    /**
     * 1-视频资源
     */
    VIDEO,
    /**
     * 3-文件类型资源
     * <p/>
     * 文件类型的资源包括（图片、PDF、word等）<br/>
     * 该资源无法直接显示，只能提供下载链接
     */
    FILE,
}
