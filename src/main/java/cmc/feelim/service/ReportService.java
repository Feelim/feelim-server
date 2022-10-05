package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.post.PostRepository;
import cmc.feelim.domain.report.Report;
import cmc.feelim.domain.report.ReportRepository;
import cmc.feelim.domain.report.dto.PostReportReq;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;

    /** 게시물 신고 **/
    @Transactional
    public Long save(Long postId, PostReportReq postReportReq) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);

        if(!post.isPresent()) {
            throw new BaseException(BaseResponseStatus.SELECT_POST);
        }
        if(postReportReq.getReason() == null) {
            throw new BaseException(BaseResponseStatus.ENTER_REASON);
        }

        Report report = new Report(post.get(), postReportReq);

        return reportRepository.save(report).getId();
    }
}
