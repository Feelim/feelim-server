package cmc.feelim.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    private BaseResponse<String> handleException(BaseException e) {
        return new BaseResponse<String>(e.getStatus());
    }

}
