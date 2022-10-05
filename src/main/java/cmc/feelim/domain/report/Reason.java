package cmc.feelim.domain.report;

import lombok.Getter;

@Getter
public enum Reason {

    CURSE("욕설/비난"),
    FISHING("낚시/놀람/도배"),
    INAPPROPRIATENESS("게시판 성격에 부적합함"),
    PORN("음란물"),
    COMMERCE("상업적 광고 및 판매");

    private final String message;

    Reason(String message) {
        this.message = message;
    }
}
