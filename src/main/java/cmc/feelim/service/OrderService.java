package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.domain.laboratory.LaboratoryRepository;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.order.Order;
import cmc.feelim.domain.order.OrderRepository;
import cmc.feelim.domain.order.dto.GetOrderRes;
import cmc.feelim.domain.order.dto.GetOrdersRes;
import cmc.feelim.domain.order.dto.PostOrderReq;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.dto.PatchProfileReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /** 현상 신청 리스트 **/
    public List<GetOrdersRes> getAll() throws BaseException {
        User user = authService.getUserFromAuth();
        List<Order> orders = orderRepository.findByUser(user);
        List<GetOrdersRes> getOrdersRes = orders.stream()
                .map(GetOrdersRes::new)
                .collect(Collectors.toList());

        return getOrdersRes;
    }

    /** 현상 신청 상세 내역 보기 **/
    public GetOrderRes getOrder(Long orderId) throws BaseException {
        User user = authService.getUserFromAuth();
        Optional<Order> order = orderRepository.findById(orderId);

        if(order.get().getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }

        GetOrderRes getOrderRes = new GetOrderRes(order.get());
        return getOrderRes;
    }
}
