package parsercore;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-21-15:49
 */
@SpringBootApplication
@ComponentScan(basePackages = {"commoncore", "parsercore"})
@EnableJpaRepositories(basePackages = "commoncore.entity.paresEntity")
@EntityScan(basePackages = "commoncore.entity.paresEntity")
public class ParesEngineApplication {
    private static final Logger LOG = Logger.getLogger(ParesEngineApplication.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void show() {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        LOG.info("环境监视：\n");
        //获取redis连接，连接失败则退出程序
        RedisConnection redisCon = null;
        try {
            redisCon = redisTemplate.getConnectionFactory().getConnection();
        } catch (Exception e) {
            LOG.error("redis 连接失败");
            redisCon.close();
            System.exit(0);
        } finally {
            redisCon.close();
        }
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (
                SQLException e) {
            LOG.error("数据库连接失败");
            System.exit(0);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("数据库关闭连接失败");
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ParesEngineApplication.class, args);
    }
}
