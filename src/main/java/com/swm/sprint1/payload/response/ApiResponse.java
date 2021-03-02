package com.swm.sprint1.payload.response;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ApiResponse {
    private String dateTime;
    private boolean success;
    private String errorCode;
    private String message;
    private String detail;
    private Map<String, Object> data = new HashMap<>();

    public ApiResponse(boolean success, String message) {
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, String message, String dataName, Object data) {
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.success = success;
        this.message = message;
        this.putData(dataName, data);
    }

    public ApiResponse(boolean success, String errorCode, String message) {
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
    }

    private void putData(String key, Object value) {
        this.data.put(key, value);
    }
}