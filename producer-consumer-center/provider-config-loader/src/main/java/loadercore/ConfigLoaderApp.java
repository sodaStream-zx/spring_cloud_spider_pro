package loadercore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2018-12-26-12:26
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"loadercore", "commoncore"})
@EnableJpaRepositories(basePackages = "commoncore.entity")
@EntityScan(basePackages = "commoncore.entity.loadEntity")
public class ConfigLoaderApp {
    @Autowired
    RedisTemplate redisTemplate;

    @PostConstruct
    public void initComponent() {
        RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigLoaderApp.class, args);
    }

}
