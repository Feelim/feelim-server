package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.config.exception.RefineError;
import cmc.feelim.domain.order.dto.PostOrderReq;
import cmc.feelim.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ApiOperation("주문서 작성")
    @PostMapping("laboratory/{laboratoryId}/order-sheet/new")
    public BaseResponse<Long> createOrderSheet(@PathVariable Long laboratoryId, @RequestBody @Validated PostOrderReq postOrderReq, Errors errors) throws BaseException, ConstraintViolationException {

        if(errors.hasErrors()) {
            return new BaseResponse<>(RefineError.refine(errors));
        }

        return new BaseResponse<>(orderService.save(laboratoryId, postOrderReq, errors));
    }
}
