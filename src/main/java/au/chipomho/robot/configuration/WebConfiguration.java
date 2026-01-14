package au.chipomho.robot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SuppressWarnings({"PMD.AtLeastOneConstructor"})
public class WebConfiguration implements WebMvcConfigurer {


  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/swagger-ui/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/5.31.0/");

    registry.addResourceHandler("/openapi.yaml")
        .addResourceLocations("classpath:/static/");
  }

}
