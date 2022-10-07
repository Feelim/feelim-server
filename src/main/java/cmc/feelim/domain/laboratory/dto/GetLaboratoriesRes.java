package cmc.feelim.domain.laboratory.dto;

import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.review.dto.GetReviewRes;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GetLaboratoriesRes {
    private Long id;
    private String name;
    private String introduction;
    private String phone;
    private List<String> bills = new ArrayList<>();
    private String url;
    private List<GetReviewRes> reviews;
    private int reviewNum;
    private double star;
    private int distance;

    public GetLaboratoriesRes(ProcessingLaboratory processingLaboratory) {
        this.id = processingLaboratory.getId();
        this.name = processingLaboratory.getName();
        this.introduction = processingLaboratory.getIntroduction();
        this.phone = processingLaboratory.getPhone();
        this.url = processingLaboratory.getHomepage();
        this.reviews = processingLaboratory.getReviews().stream()
                        .map(GetReviewRes::new)
                                .collect(Collectors.toList());
        this.reviewNum = processingLaboratory.getReviews().size();

        //별점 로직 추가
        double total = 0;
        for(int i = 0; i < processingLaboratory.getReviews().size(); i++) {
            total += processingLaboratory.getReviews().get(i).getStar();
            this.star = total / processingLaboratory.getReviews().size();
        }

        //사진
        processingLaboratory.getBills().stream()
                .forEach(bill -> {bills.add(bill.getUrl());});

        //거리

    }
}
