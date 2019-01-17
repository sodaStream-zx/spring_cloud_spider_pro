package loadercore.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-18-0:23
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LoadConfigsControllerTest {
    @Autowired
    LoadConfigsController loadConfigsController;

    @Test
    public void readConfigFormMysql() {
        //   loadConfigsController.readConfigFormMysql();
    }
}