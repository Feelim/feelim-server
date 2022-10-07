package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.config.exception.RefineError;
import cmc.feelim.domain.laboratory.LaboratoryRepository;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.order.Order;
import cmc.feelim.domain.order.OrderRepository;
import cmc.feelim.domain.order.dto.PostOrderReq;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final LaboratoryRepository laboratoryRepository;
    private static Validator validator;

    /** 주문 저장 **/
    @Transactional
    public Long save(Long laboratoryId, PostOrderReq postOrderReq, Errors errors) throws BaseException {

        User user = authService.getUserFromAuth();
        Optional<ProcessingLaboratory> laboratory = laboratoryRepository.findById(laboratoryId);

        if(!laboratory.isPresent()) {
            throw new BaseException(BaseResponseStatus.NO_LABORATORY);
        }

        Order order = new Order(user, laboratory.get(), postOrderReq);

        return orderRepository.save(order).getId();
    }


}
