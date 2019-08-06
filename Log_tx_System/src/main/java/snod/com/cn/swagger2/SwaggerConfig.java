package snod.com.cn.swagger2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lvjj
 * SwaggerConfig
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 可以定义多个组，比如本类中定义把多个接口模块分开 
     */
    @Bean
    public Docket txLogApi() {
    	 List<Parameter> pars = new ArrayList<Parameter>(); 
     	 ParameterBuilder snPar = new ParameterBuilder(); 
     	 ParameterBuilder fileTypePar = new ParameterBuilder();
     	 ParameterBuilder logTypePar = new ParameterBuilder();
     	 ParameterBuilder deviceNamePar = new ParameterBuilder();
     	 snPar.name("sn").description("sn").
     	 modelRef(new ModelRef("string")).parameterType("header").required(false).build();
     	 
     	fileTypePar.name("fileType").description("语言").
     	 modelRef(new ModelRef("string")).parameterType("header").required(false).build();

     	logTypePar.name("logType").description("渠道").
     	 modelRef(new ModelRef("string")).parameterType("header").required(false).build();

     	deviceNamePar.name("deviceName").description("版本").
     	 modelRef(new ModelRef("int")).parameterType("header").required(false).build();
          
     	 pars.add(snPar.build());
     	 pars.add(fileTypePar.build());
     	 pars.add(logTypePar.build());
     	 pars.add(deviceNamePar.build());
        return new Docket(DocumentationType.SWAGGER_2)
        		 .groupName("tx_log")
        		 .apiInfo(logtxInfo())
                 .select()
                 .apis(RequestHandlerSelectors.basePackage("snod.com.cn.controller"))
                 .paths(PathSelectors.regex("/tx_log/.*"))
                 .build()
                 .globalOperationParameters(pars);
        }
    
    
    
    @Bean
    public Docket otaPackageApi() {
    	
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("OtaUpdater")
        		 .apiInfo(otaPackageInfo())
                 .select()
                 .apis(RequestHandlerSelectors.basePackage("snod.com.cn.controller"))
                 .paths(PathSelectors.regex("/OtaUpdater/.*"))
                 .build();
        }

    private ApiInfo logtxInfo() {
        ApiInfo apiInfo = new ApiInfo("三诺数字科技有限公司",//大标题
                "日志平台",//小标题
                "1.0",//版本
                "NO terms of service",
                "吕俊杰",//作者
                "",//链接显示文字
                ""//网站链接
        );
        return apiInfo;
    } 
    
    
    private ApiInfo otaPackageInfo() {
        ApiInfo apiInfo = new ApiInfo("三诺数字科技有限公司",//大标题
                "ota升级包测试接口",//小标题
                "1.0",//版本
                "NO terms of service",
                "吕俊杰",//作者
                "",//链接显示文字
                ""//网站链接
        );
        return apiInfo;
    } 
}