package au.chipomho.robot.configuration;

import au.chipomho.robot.filters.TransactionHeaderFilter;
import au.chipomho.robot.properties.TableTopProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;

@EnableConfigurationProperties()
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration()
@SuppressWarnings({"PMD.VisibilityModifier","PMD.AtLeastOneConstructor"})
public class RobotCommandProcessorConfiguration {

  @Autowired
  private TableTopProperties tableTop;

  @PostConstruct
  protected void init(){
    //refuse to start if the table top is misconfigured.
    if (tableTop.getWidth() * tableTop.getHeight() <= 0) {
      throw new BeanCreationException("Table Top Dimensions (width x height) must be greater than 0");
    }
  }


  @Bean()
  public FilterRegistrationBean<TransactionHeaderFilter> transactionHeaderFilter(){
    final FilterRegistrationBean<TransactionHeaderFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new TransactionHeaderFilter());
    //registrationBean.addUrlPatterns("/**");
    registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registrationBean;
  }

}
