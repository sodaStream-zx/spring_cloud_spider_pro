package parsercore.dbUtils.redisDao;

import commoncore.entity.httpEntity.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import parsercore.fetcherCore.generatorcore.IParseDataGenerator;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-25-15:42
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisDaoTest {

    @Autowired
    IParseDataGenerator<ResponseData> iParseDataGenerator;

    @Test
    public void getResponseDataFromRedis() {
        for (int i = 0; i < 5; i++) {
            ResponseData responseData = iParseDataGenerator.getData();
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