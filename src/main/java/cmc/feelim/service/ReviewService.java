package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.domain.laboratory.LaboratoryRepository;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.review.Review;
import cmc.feelim.domain.review.ReviewRepository;
import cmc.feelim.domain.review.dto.PatchReviewReq;
import cmc.feelim.domain.review.dto.PostReviewReq;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AuthService authService;
    private final LaboratoryRepository laboratoryRepository;

    /** 현상소 후기 작성 **/
    @Transactional
    public Long create(Long laboratoryId, PostReviewReq postReviewReq) throws BaseException {
        ProcessingLaboratory laboratory = laboratoryRepository.findById(laboratoryId).orElseThrow( () -> {
            try {
                throw new BaseException(BaseResponseStatus.NO_LABORATORY);
            } catch (BaseException e) {
                throw new RuntimeException(e);
            }
        });

        User user = authService.getUserFromAuth();

        Review review = new Review(laboratory, user, postReviewReq);
        return reviewRepository.save(review).getId();
    }

    /** 후기 수정 **/
    public Long modify(long laboratoryId, long reviewId, PatchReviewReq patchReviewReq) throws BaseException {
        ProcessingLaboratory laboratory = laboratoryRepository.findById(laboratoryId).orElseThrow( () -> {
            try {
                throw new BaseException(BaseResponseStatus.NO_LABORATORY);
            } catch (BaseException e) {
                throw new RuntimeException(e);
            }
        });

        User user = authService.getUserFromAuth();
        Review review = reviewRepository.findById(reviewId).orElseThrow( () -> {
            try {
                throw new BaseException(BaseResponseStatus.NO_REVIEW);
            } catch (BaseException e) {
                throw new RuntimeException(e);
            }
        });

        if(review.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }

        return review.update(patchReviewReq);
    }
}
