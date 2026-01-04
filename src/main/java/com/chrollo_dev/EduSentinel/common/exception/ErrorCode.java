package com.chrollo_dev.EduSentinel.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // 1. Nhóm lỗi chung (System)
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống chưa được định nghĩa", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Sai key message (Lỗi code)", HttpStatus.BAD_REQUEST),

    // 2. Nhóm lỗi Auth/User
    USER_EXISTED(1002, "Tên tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Tên tài khoản phải từ {min} đến {max} ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Mật khẩu phải tối thiểu {min} ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1005, "Mật khẩu nhập lại không khớp", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006,"Tên tài khoản không tồn tại", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007,"Lỗi xác thực", HttpStatus.UNAUTHORIZED),

    // 3. Nhóm lỗi nghiệp vụ khác
    HOMEWORK_NOT_FOUND(2001, "Không tìm thấy bài tập", HttpStatus.NOT_FOUND),

    //4. Subject
    SUBJECT_EXISTED(4001, "Môn học đã tồn tại", HttpStatus.BAD_REQUEST),
    SUBJECT_NOT_EXISTED(4001,"Môn học không tồn tại", HttpStatus.BAD_REQUEST),

    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode; // Map luôn HTTP Status vào đây cho gọn

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}