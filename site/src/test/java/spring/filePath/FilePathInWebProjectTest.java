package spring.filePath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Copyright @ 2015 zhong-ying Co.Ltd
 * All right reserved.
 * Function：FilePathInWebProjectTest Tester
 *
 * @author asdf2014
 * @since 2015/12/31 0031
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/service/spring.service.xml",
        "classpath:/configuration/properties.xml"})
//should not be classpath:**/spring.service.xml
public class FilePathInWebProjectTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private FilePathInWebProject filePathInWebProject;

    @Test
    public void test() throws Exception {
        filePathInWebProject.showDate();
    }
}
