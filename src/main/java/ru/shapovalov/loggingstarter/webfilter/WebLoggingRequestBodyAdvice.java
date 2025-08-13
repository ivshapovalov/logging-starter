package ru.shapovalov.loggingstarter.webfilter;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import ru.shapovalov.loggingstarter.LoggingProperties;

@ControllerAdvice
public class WebLoggingRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebLoggingRequestBodyAdvice.class);

    @Autowired
    private PathMatcher pathMatcher;

    @Autowired
    private LoggingProperties loggingProperties;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
            MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI() + WebLoggingUtils.formatQueryString(request);

        if (loggingProperties.getWebLogging().getLogBody()) {
            log.info("Тело запроса (method={}, url={}, body={}]", method, requestURI, body);
        }

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        String requestURI = request.getRequestURI() + WebLoggingUtils.formatQueryString(request);
        if (WebLoggingUtils.isExcludedPath(loggingProperties, pathMatcher, requestURI)) {
            return false;
        }
        return true;
    }
}
