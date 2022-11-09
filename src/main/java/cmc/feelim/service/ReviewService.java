package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.config.s3.S3FileUploadService;
import cmc.feelim.domain.Status;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AuthService authService;
    private final LaboratoryRepository laboratoryRepository;
    private final S3FileUploadService fileUploadService;

    /** 현상소 후기 작성 **/
    @Transactional
    public Long create(Long laboratoryId, PostReviewReq postReviewReq) throws BaseException {
        ProcessingLaboratory laboratory = laboratoryRepository.findById(laboratoryId)
                .orElseThrow( () -> new BaseException(BaseResponseStatus.NO_LABORATORY));

        User user = authService.getUserFromAuth();
        Review review = new Review(laboratory, user, postReviewReq);

        if(!postReviewReq.getImages().isEmpty()) {
            review.updateImage(fileUploadService.uploadFromReview(postReviewReq.getImages(), review));
        }

        return reviewRepository.save(review).getId();
    }

    /** 후기 수정 **/
    @Transactional
    public Long modify(long laboratoryId, long reviewId, PatchReviewReq patchReviewReq) throws BaseException {
        ProcessingLaboratory laboratory = laboratoryRepository.findById(laboratoryId)
                .orElseThrow( () -> new BaseException(BaseResponseStatus.NO_LABORATORY));

        User user = authService.getUserFromAuth();
        Review review = reviewRepository.findById(reviewId).orElseThrow( () ->
                new BaseException(BaseResponseStatus.NO_REVIEW));

        if(review.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }

        review.update(patchReviewReq);

        if(!patchReviewReq.getImages().isEmpty()) {
            review.updateImage(fileUploadService.uploadFromReview(patchReviewReq.getImages(), review));
        }

        return review.getId();
    }

    /** 리뷰 삭제 **/
    @Transactional
    public Long delete(Long laboratoryId, Long reviewId) throws BaseException {
        ProcessingLaboratory laboratory = laboratoryRepository.findById(laboratoryId)
                .orElseThrow( () -> new BaseException(BaseResponseStatus.NO_LABORATORY));

        User user = authService.getUserFromAuth();
        Review review = reviewRepository.findById(reviewId).orElseThrow( () ->
                new BaseException(BaseResponseStatus.NO_REVIEW));

        if(review.getUser() != user) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }

        review.changeStatus(Status.DELETED);
        return review.getId();
    }

}
