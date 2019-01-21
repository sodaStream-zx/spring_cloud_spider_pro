package crudcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 一杯咖啡
 * @desc
 * @createTime 2019-01-02-15:49
 */
@SpringBootApplication
@ComponentScan(basePackages = {"crudcore", "commoncore"})
public class CrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }
}
