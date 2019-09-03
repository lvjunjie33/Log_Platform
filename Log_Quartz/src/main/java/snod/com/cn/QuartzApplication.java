package snod.com.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan
@ComponentScan(basePackages = { "snod.com.cn" })
// @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
// DataSourceTransactionManagerAutoConfiguration.class })
@SpringBootApplication
@MapperScan("snod.com.cn.dao")
public class QuartzApplication{
    public static void main( String[] args )
    {
    	SpringApplication.run(QuartzApplication.class);
    }
}
