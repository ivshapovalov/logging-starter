package ru.shapovalov.loggingstarter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.PathMatcher;
import ru.shapovalov.loggingstarter.aspect.LogExecutionAspect;
import ru.shapovalov.loggingstarter.webfilter.WebLoggingFilter;
import ru.shapovalov.loggingstarter.webfilter.WebLoggingRequestBodyAdvice;

@AutoConfiguration
@ConditionalOnProperty(prefix = "logging", value = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingStarterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "logging", value = "log-exec-time", havingValue = "true")
    public LogExecutionAspect logExecutionAspect() {
        return new LogExecutionAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", value = "enabled", havingValue = "true")
    public WebLoggingFilter webLoggingFilter(LoggingProperties loggingProperties,
                                             PathMatcher pathMatcher) {
        return new WebLoggingFilter(loggingProperties, pathMatcher);
    }

    @Bean
    @ConditionalOnProperty(prefix = "logging.web-logging", name = {"enabled", "log-body"}, havingValue = "true")
    public WebLoggingRequestBodyAdvice webLoggingRequestBodyAdvice() {
        return new WebLoggingRequestBodyAdvice();
    }
}
