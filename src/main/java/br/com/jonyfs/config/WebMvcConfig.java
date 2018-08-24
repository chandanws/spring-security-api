package br.com.jonyfs.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @PostConstruct
    public void logMessage() {
        LOGGER.info("STARTED");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/dashboardTA-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/dashboardTA-INF/resources/webjars/");

        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");

    }

}
