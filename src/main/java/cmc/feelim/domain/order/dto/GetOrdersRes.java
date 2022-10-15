package cmc.feelim.domain.order.dto;

import cmc.feelim.domain.order.Order;
import cmc.feelim.domain.order.OrderState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetOrdersRes {
    private Long id;
    private String name;
    private String info;
    private OrderState state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public GetOrdersRes(Order order) {
        this.id = order.getId();
        this.name = order.getLaboratory().getName();
        this.info = order.getInfo();
        this.state = order.getOrderState();
        this.createdAt = order.getCreatedAt();
    }
}
