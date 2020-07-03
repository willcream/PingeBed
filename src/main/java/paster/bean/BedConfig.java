package paster.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import paster.utlis.FileUtils;

/**
 * created by Tianci.Wu on 2020/7/3
 */
@Configuration
public class BedConfig extends WebMvcConfigurationSupport {
    @Value("${bed.uploadPath}")
    private String uploadPath;

    @Value("${bed.staticPath}")
    private String staticPath;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        String storePath = FileUtils.fixPath(uploadPath);
        registry.addResourceHandler(staticPath).addResourceLocations("file:"+storePath);
    }


}
