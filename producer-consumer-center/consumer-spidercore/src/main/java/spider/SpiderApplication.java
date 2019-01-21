package spider;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author 一杯咖啡
 * @desc 爬虫启动类 （springboot 启动后，启动爬虫；检查环境）
 * @createTime ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
@SpringBootApplication
@ComponentScan(basePackages = {"spider", "commoncore"})
@EnableEurekaClient
@EntityScan(basePackages = {"commoncore.entity.spiderEntity"})
public class SpiderApplication {
    private static final Logger LOG = Logger.getLogger(SpiderApplication.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DataSource dataSource;

    /**
     * desc: 初始化配置监视，出异常直接退出
     *
     * @throws SQLException
     **/
    @PostConstruct
    public void monitor() throws SQLException {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        LOG.info("环境监视：\n");
        //获取redis连接，连接失败则退出程序
        redisTemplate.getConnectionFactory().getConnection();
        dataSource.getConnection();

    }

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);
    }
}
