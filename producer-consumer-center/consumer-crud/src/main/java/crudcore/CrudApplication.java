package crudcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-02-15:49
 */
@SpringBootApplication
@ComponentScan(basePackages = {"crudcore", "commoncore"})
@EnableJpaRepositories(basePackages = {"commoncore.entity.loadEntity"})
public class CrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }
}
