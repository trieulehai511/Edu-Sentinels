package com.chrollo_dev.EduSentinel.common.exception;

import com.chrollo_dev.EduSentinel.common.dto.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Xử lý Custom Exception (Lỗi logic mình tự ném ra)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<APIResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();

        APIResponse APIResponse = new APIResponse();
        APIResponse.setCode(errorCode.getCode());
        APIResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(APIResponse);
    }

    // 2. Xử lý Validation Exception (@NotBlank, @Size...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleValidation(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            // Map message từ DTO (ví dụ "USERNAME_INVALID") sang Enum
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ex) {
            // Nếu quên define trong Enum thì fallback về lỗi mặc định
        }

        APIResponse APIResponse = new APIResponse();
        APIResponse.setCode(errorCode.getCode());
        APIResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .badRequest()
                .body(APIResponse);
    }

    // 3. Xử lý lỗi hệ thống (NullPointer, DB connection...)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleUnwantedException(Exception e) {
        APIResponse APIResponse = new APIResponse();
        APIResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        APIResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage() + ": " + e.getMessage());

        return ResponseEntity.internalServerError().body(APIResponse);
    }
}