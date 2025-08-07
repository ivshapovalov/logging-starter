package ru.shapovalov.loggingstarter.webfilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;

import java.util.*;
import java.util.stream.Collectors;

public class WebLoggingUtils {

    public static final String MASKED_VALUE = "***";

    private WebLoggingUtils() {
    }

    public static String inlineRequestHeaders(HttpServletRequest request, List<String> maskingHeaders) {
        return Collections.list(request.getHeaderNames()).stream()
                .filter(Objects::nonNull)
                .flatMap(header -> {
                            List<String> headerValues = Collections.list(request.getHeaders(header));
                            if (maskingHeaders.contains(header.trim().toLowerCase())) {
                                return headerValues.stream()
                                        .map(value -> header + "=" + MASKED_VALUE);
                            } else {
                                return headerValues.stream()
                                        .map(value -> header + "=" + value);
                            }
                        }
                )
                .collect(Collectors.joining(","));
    }

    public static String inlineResponseHeaders(HttpServletResponse response, List<String> maskingHeaders) {
        return response.getHeaderNames().stream()
                .filter(Objects::nonNull)
                .flatMap(header -> {
                    Collection<String> headerValues = response.getHeaders(header);
                    if (maskingHeaders.contains(header.trim().toLowerCase())) {
                        return headerValues.stream()
                                .map(value -> header + "=" + MASKED_VALUE);
                    } else {
                        return headerValues.stream()
                                .map(value -> header + "=" + value);
                    }
                })
                .collect(Collectors.joining(","));
    }

    public static String formatQueryString(HttpServletRequest request) {
        return Optional.ofNullable(request.getQueryString())
                .map(qs -> "?=" + qs)
                .orElse(Strings.EMPTY);
    }
}
