package cmc.feelim.domain.laboratory.dto;

import cmc.feelim.domain.Address;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.review.dto.GetReviewRes;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GetLaboratoryRes {
    private Long id;
    private String name;
    private String introduction;
    private Address address;
    private String phone;
    private List<String> bills = new ArrayList<>();
    private String url;
    private List<GetReviewRes> reviews;
    private int reviewNum;
    private double star;
    private int distance;

    public GetLaboratoryRes(ProcessingLaboratory laboratory) {
        this.id = laboratory.getId();
        this.name = laboratory.getName();
        this.phone = laboratory.getPhone();
        this.url = laboratory.getHomepage();
        this.reviews = laboratory.getReviews().stream()
                .map(GetReviewRes::new)
                .collect(Collectors.toList());
        this.reviewNum = laboratory.getReviews().size();
        this.address = laboratory.getAddress();

        //별점 로직 추가
        double total = 0;
        for(int i = 0; i < laboratory.getReviews().size(); i++) {
            total += laboratory.getReviews().get(i).getStar();
            this.star = total / laboratory.getReviews().size();
        }

        //거리 추가

        //사진
        laboratory.getBills().stream()
                .forEach(bill -> {bills.add(bill.getUrl());});
    }
}
