package cmc.feelim.domain.order;

import cmc.feelim.domain.Address;
import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.order.dto.PostOrderReq;
import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String phone;

    @Email
    private String email;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory_id")
    private ProcessingLaboratory laboratory;

    @NotNull
    private String info;

    private String demand;

    @Enumerated(EnumType.STRING)
    private OrderState orderState = OrderState.READY;

    public Order(User user, ProcessingLaboratory laboratory, PostOrderReq postOrderReq) {
        this.user = user;
        this.laboratory = laboratory;
        this.phone = postOrderReq.getPhone();
        this.email = postOrderReq.getEmail();
        this.address = postOrderReq.getAddress();
        this.info = postOrderReq.getInfo();
        this.demand = postOrderReq.getDemand();
    }
}
