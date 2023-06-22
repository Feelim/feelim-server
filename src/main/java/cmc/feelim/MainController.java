package cmc.feelim;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class MainController {
//    @ApiOperation("health check")
//    @GetMapping("/")
//    public BaseResponse<String> main() throws BaseException {
//        return new BaseResponse<>("health check OK!");
//    }

    @ApiOperation("health check")
    @GetMapping("/health")
    public BaseResponse<String> checkHealth() throws BaseException {
        return new BaseResponse<>("health check OK!");
    }

}
