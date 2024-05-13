package com.blueviolet.backend.common.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.UUID;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class MdcLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        UUID uuid = UUID.randomUUID();
        MDC.put("requestId", uuid.toString());
        MDC.put("endpoint", String.format("%s %s", request.getMethod(), request.getRequestURI()));

        doFilterWithWrappedRequestAndResponse(
                new RequestWrapper(request),
                new ContentCachingResponseWrapper(response),
                filterChain
        );

        MDC.clear();
    }

    protected void doFilterWithWrappedRequestAndResponse(
            RequestWrapper request,
            ContentCachingResponseWrapper response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logRequest(request);
        filterChain.doFilter(request, response);
        logResponse(response);
        response.copyBodyToResponse();
    }

    private void logRequest(RequestWrapper request) {
        String httpMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        String authorization = request.getHeader("Authorization");
        String requestBody = getRequestBody(request.getInputStream());

        String logMessage =
                "\n---> [REQUEST - START]" +
                MessageFormat.format(
                        "\n {0} {1}",
                        httpMethod,
                        StringUtils.isBlank(queryString) ? requestUri : StringUtils.join(requestUri, "?", queryString)
                ) +
                (StringUtils.isBlank(authorization) ? "" : MessageFormat.format("\n Authorization: {0}", authorization)) +
                (StringUtils.isBlank(requestBody) ? "" : MessageFormat.format("\n {0}", requestBody)) +
                "\n---> [REQUEST - END]";

        log.info(logMessage);
    }

    private String getRequestBody(
            InputStream inputStream
    ) {
        try {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            return new String(content);
        } catch (IOException ioException) {
            return "";
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String responseBody = new String(response.getContentAsByteArray());
        int statusCode = response.getStatus();
        HttpStatus httpStatus = HttpStatus.resolve(statusCode);

        log.info(
                """
                
                <--- [RESPONSE - START]
                 {}
                 {}
                <--- [RESPONSE - END]
                """,
                httpStatus,
                responseBody
        );
    }
}
