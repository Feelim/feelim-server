package cmc.feelim.domain.order.dto;

import cmc.feelim.domain.Address;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PostOrderReq {
    @NotBlank(message = "이름을 기입해주세요.")
    private String name;
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;
    @Email
    private String email;
    private Address address;
    @NotBlank(message = "세부 정보를 작성해주세요.")
    private String info;
    private String demand;
}
