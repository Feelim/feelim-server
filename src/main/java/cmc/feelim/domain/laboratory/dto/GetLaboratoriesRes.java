package cmc.feelim.domain.laboratory.dto;

import cmc.feelim.domain.Address;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.review.dto.GetReviewRes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

@Getter
@Setter
public class GetLaboratoriesRes implements Comparator<GetLaboratoriesRes> {

    /** 이름, 주소, 별점(개수), 거리, 리뷰 개수, 가게 이미지만 나오도록 수정수정 **/

    private Long id;
    private String name;
    private Address address;
    private List<String> images = new ArrayList<>();
    private int reviewNum;
    private double star;
    @JsonBackReference
    private Point point;

    private Double x;
    private Double y;
    private double distance;

    public GetLaboratoriesRes(ProcessingLaboratory processingLaboratory) {
        this.id = processingLaboratory.getId();
        this.name = processingLaboratory.getName();
        this.address = processingLaboratory.getAddress();
        this.point = processingLaboratory.getPoint();
        this.reviewNum = processingLaboratory.getReviews().size();
        this.x = processingLaboratory.getPoint().getX();
        this.y = processingLaboratory.getPoint().getY();

        //별점 로직
        double total = 0;
        for(int i = 0; i < processingLaboratory.getReviews().size(); i++) {
            total += processingLaboratory.getReviews().get(i).getStar();
            this.star = total / processingLaboratory.getReviews().size();
        }

        //사진
        processingLaboratory.getBills().stream()
                .forEach(back -> {images.add(back.getUrl());});

    }

    @Override
    public int compare(GetLaboratoriesRes o1, GetLaboratoriesRes o2) {
        return 0;
    }

    @Override
    public Comparator<GetLaboratoriesRes> reversed() {
        return Comparator.super.reversed();
    }

    @Override
    public Comparator<GetLaboratoriesRes> thenComparing(Comparator<? super GetLaboratoriesRes> other) {
        return Comparator.super.thenComparing(other);
    }

    @Override
    public <U> Comparator<GetLaboratoriesRes> thenComparing(Function<? super GetLaboratoriesRes, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return Comparator.super.thenComparing(keyExtractor, keyComparator);
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<GetLaboratoriesRes> thenComparing(Function<? super GetLaboratoriesRes, ? extends U> keyExtractor) {
        return Comparator.super.thenComparing(keyExtractor);
    }

    @Override
    public Comparator<GetLaboratoriesRes> thenComparingInt(ToIntFunction<? super GetLaboratoriesRes> keyExtractor) {
        return Comparator.super.thenComparingInt(keyExtractor);
    }

    @Override
    public Comparator<GetLaboratoriesRes> thenComparingLong(ToLongFunction<? super GetLaboratoriesRes> keyExtractor) {
        return Comparator.super.thenComparingLong(keyExtractor);
    }

    @Override
    public Comparator<GetLaboratoriesRes> thenComparingDouble(ToDoubleFunction<? super GetLaboratoriesRes> keyExtractor) {
        return Comparator.super.thenComparingDouble(keyExtractor);
    }
}
