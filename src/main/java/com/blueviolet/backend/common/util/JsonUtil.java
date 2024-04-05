package com.blueviolet.backend.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public <T> String toJson(
            T request
    ) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * [예시 (Example)]
     *
     * <pre>
     * {@code
     * OkResponse<TokenResponseV1> signInResponse =
     *      jsonUtil.fromJson(signInJsonResponse, new TypeReference<>() {});
     * }
     * </pre>
     *
     * @param json
     * @param typeRef
     * @return
     * @param <T>
     */
    public <T> T fromJson(
            String json,
            TypeReference<T> typeRef
    ) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            return objectMapper.readValue(json, typeRef);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * [예시 (Example)]
     *
     * <pre>
     * {@code
     * OkResponse<TokenResponseV1> signInResponse =
     *      jsonUtil.fromJson(signInJsonResponse, OkResponse.class, TokenResponseV1.class);
     * }
     * </pre>
     *
     * @param json
     * @param parametrized
     * @param parameterClass
     * @return
     * @param <T>
     */
    public <T> T fromJson(
            String json,
            Class<?> parametrized,
            Class<?> parameterClass
    ) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            JavaType type =
                    objectMapper
                            .getTypeFactory()
                            .constructParametricType(parametrized, parameterClass);
            return objectMapper.readValue(json, type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
