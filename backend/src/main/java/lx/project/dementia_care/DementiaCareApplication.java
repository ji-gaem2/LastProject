package lx.project.dementia_care;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("lx.project.dementia_care.mapper")
public class DementiaCareApplication {
    public static void main(String[] args) {
        SpringApplication.run(DementiaCareApplication.class, args);
    }
}