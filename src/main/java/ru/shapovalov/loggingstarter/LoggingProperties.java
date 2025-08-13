package ru.shapovalov.loggingstarter;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging")
public class LoggingProperties {

    private Boolean logExecTime;
    private WebLoggingProperties webLogging = new WebLoggingProperties();

    public Boolean getLogExecTime() {
        return logExecTime;
    }

    public void setLogExecTime(Boolean logExecTime) {
        this.logExecTime = logExecTime;
    }

    public WebLoggingProperties getWebLogging() {
        return webLogging;
    }

    public void setWebLogging(WebLoggingProperties webLogging) {
        this.webLogging = webLogging;
    }

    public static class WebLoggingProperties {

        private Boolean enabled = true;
        private Boolean logBody = false;
        private List<String> excludedPaths = new ArrayList<>();
        private WebLoggingMaskingProperties masking = new WebLoggingMaskingProperties();

        public List<String> getExcludedPaths() {
            return excludedPaths;
        }

        public void setExcludedPaths(List<String> excludedPaths) {
            this.excludedPaths = excludedPaths;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Boolean getLogBody() {
            return logBody;
        }

        public void setLogBody(Boolean logBody) {
            this.logBody = logBody;
        }

        public WebLoggingMaskingProperties getMasking() {
            return masking;
        }

        public void setMasking(WebLoggingMaskingProperties masking) {
            this.masking = masking;
        }
    }

    public static class WebLoggingMaskingProperties {

        private Boolean enabled = true;
        private List<String> headers = new ArrayList<>();

        public List<String> getHeaders() {
            return headers;
        }

        public void setHeaders(List<String> headers) {
            this.headers = headers.stream()
                    .map(String::trim)
                    .map((String::toLowerCase))
                    .toList();
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
