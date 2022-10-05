package cmc.feelim.domain.report;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.report.dto.PostReportReq;
import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    //신고한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //신고당한 게시물
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @NotNull(message = "신고할 게시물을 선택해주세요.")
    private Post post;

    //신고 내용
    @Enumerated(EnumType.STRING)
    @NotNull(message = "신고 이유를 선택해주세요.")
    private Reason reason;

    @Column(length = 100)
    private String etc;

    public Report(Post post, PostReportReq postReportReq) {
        this.post = post;
        this.reason = postReportReq.getReason();
        this.etc = postReportReq.getEtc();
    }
}
