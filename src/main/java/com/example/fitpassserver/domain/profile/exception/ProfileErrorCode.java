package com.example.fitpassserver.domain.profile.exception;


import com.example.fitpassserver.global.apiPayload.code.BaseErrorCode;
import com.example.fitpassserver.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProfileErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE404", "해당 멤버의 프로필을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.status)
                .isSuccess(false) // 에러이므로 항상 false
                .code(this.code)
                .message(this.message)
                .build();
    }
}