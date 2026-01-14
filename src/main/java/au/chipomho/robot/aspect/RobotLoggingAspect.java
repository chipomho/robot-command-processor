package au.chipomho.robot.aspect;

import au.chipomho.robot.annotations.Monitored;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

@Aspect
@Component
@Slf4j
@SuppressWarnings({"PMD.AtLeastOneConstructor"})
public class RobotLoggingAspect {

  @Around("@within(au.chipomho.robot.annotations.Monitored) || " +
      "@annotation(au.chipomho.robot.annotations.Monitored) ")
  public Object logExecution(final ProceedingJoinPoint joinPoint) throws Throwable {
    final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    final Monitored annotation = Stream.of(method.getDeclaringClass().getAnnotation(Monitored.class) )
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);

    //get the operation being observed
    final String operation = StringUtils.isNotBlank(Objects.nonNull(annotation)?annotation.operation():null) ?
        annotation.operation() : String.format("%s.%s",  method.getDeclaringClass().getSimpleName() , method.getName());

    final StopWatch watch = new StopWatch();
    watch.start();
      final Object result = joinPoint.proceed();
      watch.stop();
      if (log.isInfoEnabled()) {
        log.info("Executing{} Duration={}ms ", operation, watch.getTotalTimeMillis() );
      }
      return result;
  }


}
