package cmc.feelim.domain.order.dto;

import cmc.feelim.domain.Address;
import cmc.feelim.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderRes {
    private Long id;
    private Address address;

    private String userName;
    private String userPhone;
    private String email;

    private String laboratoryName;
    private Address laboratoryAddress;
    private String info;
    private String demand;

    private String price;


    public GetOrderRes(Order order) {
        this.id = order.getId();
        this.address = order.getAddress();
        this.userName = order.getName();
        this.userPhone = order.getPhone();
        this.email = order.getEmail();

        this.laboratoryName = order.getLaboratory().getName();
        this.laboratoryAddress = order.getLaboratory().getAddress();
        this.info = order.getInfo();
        this.demand = order.getDemand();

        //나중에 가격 정보 추가
    }
}
