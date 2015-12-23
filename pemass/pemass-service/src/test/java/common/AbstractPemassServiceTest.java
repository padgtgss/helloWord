/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package common;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @Description: PEMASS系统中，所有的测试类继承本类
 * @Author: estn.zuo
 * @CreateTime: 2015-07-18 10:58
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class AbstractPemassServiceTest extends AbstractTestNGSpringContextTests {
}
