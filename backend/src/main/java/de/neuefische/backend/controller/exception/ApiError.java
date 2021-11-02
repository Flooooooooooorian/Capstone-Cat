package de.neuefische.backend.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {

    private String text;
    private String exceptionMessage;
    private LocalDateTime timestamp;

    public ApiError(String text, String exceptionMessage) {
        this.text = text;
        this.exceptionMessage = exceptionMessage;
        this.timestamp = LocalDateTime.now();
    }
}
