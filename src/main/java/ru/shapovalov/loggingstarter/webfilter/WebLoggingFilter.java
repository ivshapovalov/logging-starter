package ru.shapovalov.loggingstarter.webfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.shapovalov.loggingstarter.LoggingProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WebLoggingFilter extends HttpFilter {

    private static final Logger log = LoggerFactory.getLogger(WebLoggingFilter.class);

    private final LoggingProperties loggingProperties;

    private final PathMatcher pathMatcher;

    private final boolean isLogBodyEnabled;
    private final boolean isMaskingHeadersEnabled;
    private final List<String> maskingHeaders;

    public WebLoggingFilter(LoggingProperties loggingProperties, PathMatcher pathMatcher) {
        this.loggingProperties = loggingProperties;
        this.pathMatcher = pathMatcher;
        this.isLogBodyEnabled = isLogBodyEnabled();
        this.isMaskingHeadersEnabled = isMaskingEnabled();
        this.maskingHeaders = maskingHeaders();
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI() + WebLoggingUtils.formatQueryString(request);
        String requestHeaders = WebLoggingUtils.inlineRequestHeaders(request, isMaskingHeadersEnabled ? maskingHeaders : Collections.emptyList());

        if (WebLoggingUtils.isExcludedPath(loggingProperties,pathMatcher,requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        log.info("Запрос (method={}, url={}, headers={}]", method, requestURI, requestHeaders);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            super.doFilter(request, responseWrapper, chain);
            String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

            String responseHeaders = WebLoggingUtils.inlineResponseHeaders(response, isMaskingHeadersEnabled ? maskingHeaders : Collections.emptyList());
            if (isLogBodyEnabled) {
                log.info("Ответ (method={}, url={}, headers={}, status={}, body={}]", method, requestURI, responseHeaders, response.getStatus(), responseBody);
            } else {
                log.info("Ответ (method={}, url={}, headers={}, status={}]", method, requestURI, responseHeaders, response.getStatus());
            }
        } finally {
            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean isLogBodyEnabled() {
        return loggingProperties.getWebLogging().getLogBody();
    }

    private boolean isMaskingEnabled() {
        return loggingProperties.getWebLogging().getMasking().getEnabled();
    }

    private List<String> maskingHeaders() {
        return loggingProperties.getWebLogging().getMasking().getHeaders();
    }
}
