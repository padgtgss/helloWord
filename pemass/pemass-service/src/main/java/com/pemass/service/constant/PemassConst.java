/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.constant;

import com.pemass.persist.enumeration.SystemEnvironmentEnum;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: pemass常量定义
 * @Author: estn.zuo
 * @CreateTime: 2014-11-03 15:23
 */
public class PemassConst {

    /**
     * 管理后台加密使用
     * <p/>
     * <p>PEMASS_PORTAL</p>为审核后台配置在审核后台接口密钥管理的Name
     */
    public static final String PEMASS_PORTAL = "PEMASS_PORTAL";

    /**
     * 审核后台加密使用
     * <p/>
     * <p>PEMASS_MANAGER_PORTAL</p>为审核后台配置在审核后台接口密钥管理的Name
     */
    public static final String PEMASS_MANAGER_PORTAL = "PEMASS_MANAGER_PORTAL";

    /**
     * 由于首页滚动图片为独立数据，米有一个载体，所以直接给一个UUID值来代表首页滚动图片
     * <p/>
     * <stong>*不可更改*<stong/>
     */
    public static final String INDEX_IMAGE_UUID = "ece3764056b54eefa9f98ae18f0c285a";

    /**
     * 通过UUIDUtil.randomUUID()方法生成的一个UUID,用于标识云钱包Loading的首页图片
     * <p/>
     * <stong>*不可更改*</stong>
     */
    public static final String LOADING_IMAGE_UUID = "d4ec2fca497e4b97b8291481c829afca";

    /**
     * AES加密算法密钥
     * <p/>
     * <stong>*不可更改*<stong/>
     */
    public static String AES_CIPHER = "838e35fc9b784cbd";


    /**
     * 用于存储验证码集合
     * <p/>
     * 规则如下：
     * <p/>
     * 1.C端用户注册,key:"REG_"+手机号码<p>
     * 2.用户后台注册,key:"REG_PORTAL"+邮箱
     * 3.用户后台绑定手机,key;"TEL_PORTAL"+用户名
     * 4.找回密码,key:"FORGET_"+用户名
     */
    public static Map<String, String> VALIDATE_CODE = new ConcurrentHashMap<String, String>();

    /**
     * 积分区域分隔符
     */
    public static final String AREA_SEPARATOR_SYMBOL = ":";

    //=========== 审核后台账户角色 ================
    /**
     * 超级管理员(总公司管理员)
     */
    public static final String ROLE_SUPER = "ROLE_SUPER";

    /**
     * 管理员(分公司管理员)
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 财务
     */
    public static final String ROLE_FINANCE = "ROLE_FINANCE";

    /**
     * 二次征信审核管理员
     */
    public static final String ROLE_SECOND_ADMIN = "ROLE_SECOND_ADMIN";

    //=========== B端用户权限 ================
    /**
     * 收银员
     */
    public static final String ROLE_CASHIER = "ROLE_CASHIER";

    /**
     * 检票员
     */
    public static final String ROLE_TICKETER = "ROLE_TICKETER";

    /**
     * 普通用户角色
     */
    public static final String ROLE_USER = "ROLE_USER";


    public static String REGISTER_MAIL_TITLE;

    public static String REGISTER_MAIL_CONTENT;

    /**
     * 自动创建用户默认密码
     */
    public static String DEFAULT_PASSWORD = "123456";

    /**
     * 商品路径分隔符
     */
    public static String PATH_SEPARATOR_SYMBOL = "/";

    /**
     * 短信模板
     */
    public static Properties SMS_PROPERTIES = new Properties();

    /**
     * 云钱包最新下载地址
     */
    public static String CLOUDMONEY_LATEST_URL;


    /**
     * 当前系统环境
     */
    public static SystemEnvironmentEnum SYSTEM_ENVIRONMENT;

    //====================================================================================================


    /**
     * 尝试最大次数
     */
    public static Integer ATTEMPT_MAX_TIMES;

    /**
     * 积分区域
     * <p/>
     * 通用
     */
    public static String AREA_GENERAL = "00:00:00:00:00";

    /**
     * DES加密key
     */
    public static String FIRST_KEY = "dff11af6e4e04c50";

    public static String SECOND_KEY = "owbthxvjmsrraqppvv";

    public static String THIRD_KEY = "abumnpilaupsxqsugu";

}
