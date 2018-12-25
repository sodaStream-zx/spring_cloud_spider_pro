package parsercore.dbUtils.gainDao;

import commoncore.entity.responseEntity.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import parsercore.dbUtils.gainDao.IGain.IRedisDao;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-25-15:42
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisDaoTest {

    @Autowired
    IRedisDao iRedisDao;

    @Test
    public void getResponseDataFromRedis() {
        for (int i = 0; i < 5; i++) {
            ResponseData responseData = iRedisDao.getResponseDataFromRedis("responseList");
            System.out.println("responseData = " + responseData.toString());
        }
    }

    @Test
    public void addDomainRuleToRedis() {
    }

    @Test
    public void getDomainRuleFromRedis() {
    }
}