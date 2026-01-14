package au.chipomho.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@EnableAutoConfiguration()
@ComponentScan(basePackages = {"au.chipomho.robot.properties"},
    includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.boot.context.properties.ConfigurationProperties.class})})
@ComponentScan(basePackages = {"au.chipomho.robot.component","au.chipomho.robot.aspect"},
    includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Component.class,
        org.springframework.stereotype.Service.class, org.aspectj.lang.annotation.Aspect.class})})
@ComponentScan(basePackages = {"au.chipomho.robot.advice"},
    includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.web.bind.annotation.ControllerAdvice.class})})
@ComponentScan(basePackages = {"au.chipomho.robot.service"},
    includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.stereotype.Service.class})})
@ComponentScan(basePackages = {"au.chipomho.robot.controller"},
    includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.web.bind.annotation.RestController.class})})
@ComponentScan(basePackages = {"au.chipomho.robot.configuration"},
    includeFilters = {@ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ANNOTATION, value = {org.springframework.context.annotation.Configuration.class})})
public class RobotCommandProcessorApplication {

  public static void main(final String[] args) {
    final SpringApplication application =
        new SpringApplication(RobotCommandProcessorApplication.class);
    application.setLogStartupInfo(false);
    application.setBanner((Environment environment, Class<?> sourceClass, PrintStream out) -> {
      out.println();
      out.println("==================================================================================================================");
      out.println("    Truck Robot Command Processor " );
      out.println("==================================================================================================================");
      out.println();
    });
    application.run(args);

  }
}
