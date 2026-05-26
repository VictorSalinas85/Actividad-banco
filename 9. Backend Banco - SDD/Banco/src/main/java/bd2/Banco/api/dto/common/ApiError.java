package bd2.Banco.api.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final int status;
    private final String error;
    private final String message;
    private final String codigoError;
    private final String path;
    private final String timestamp;
    private final List<String> details;

    public static ApiError of(int status, String error, String message, String path) {
        return ApiError.builder()
                .status(status).error(error).message(message).path(path)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static ApiError of(int status, String error, String message, String codigoError, String path) {
        return ApiError.builder()
                .status(status).error(error).message(message)
                .codigoError(codigoError).path(path)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static ApiError withDetails(int status, String error, String message, String path, List<String> details) {
        return ApiError.builder()
                .status(status).error(error).message(message).path(path)
                .timestamp(Instant.now().toString()).details(details)
                .build();
    }
}
