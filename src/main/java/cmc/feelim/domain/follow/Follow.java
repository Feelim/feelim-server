package cmc.feelim.domain.follow;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fromUserId")
    private User fromUser; //팔로우를 하는 유저

    @ManyToOne
    @JoinColumn(name = "toUserId")
    private User toUser; //팔로우 당하는 유저
}