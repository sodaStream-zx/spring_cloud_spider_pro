package parsercore;

import commoncore.customUtils.SerializeUtil;
import commoncore.entity.loadEntity.RedisDbKeys;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-21-15:49
 */
@SpringBootApplication
@ComponentScan(basePackages = {"commoncore", "parsercore"})
@EnableJpaRepositories(basePackages = "commoncore.entity")
public class ParesEngineApplication {
    private static final Logger LOG = Logger.getLogger(ParesEngineApplication.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisDbKeys redisDbKeys;
    @Value(value = "${redisDb.config}")
    private String redisDbConfig;

    @PostConstruct
    public void show() throws SQLException {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        LOG.info("redis连接 数据库连接测试：\n");

        RedisConnection redisCon = redisTemplate.getConnectionFactory().getConnection();
        redisCon.close();
        LOG.info("mysql连接 数据库连接测试：\n");

        Connection connection = dataSource.getConnection();
        connection.close();
        LOG.info("redis数据库表约定加载");
        this.loadRedisConfig();
    }

    public static void main(String[] args) {
        SpringApplication.run(ParesEngineApplication.class, args);
    }

    /**
     * desc: 加载redisDb配置
     **/
    public void loadRedisConfig() {
        //加载redis配置
        String redisDbStr = (String) redisTemplate.opsForValue().get(redisDbConfig);
        Optional<RedisDbKeys> rs = SerializeUtil.deserializeToObject(redisDbStr);
        if (rs.isPresent()) {
            redisDbKeys.configOwn(rs.get());
        }
        LOG.info("redis数据表清单：：" + redisDbKeys.toString());
    }
}
