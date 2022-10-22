package cmc.feelim.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    COMMENT("에 새로운 댓글이 달렸어요!"),
    ANSWER("질문에 새로운 답변이 달렸어요!"),
    LIKES("님이 필름을 좋아합니다."),

    READY("현상소에서 고객님의 주문을 확인하고 있어요."),
    MOVE("고객님의 필름이 현상소로 이동하고 있어요."),
    DEVELOPMENT("필름을 현상/스캔하고 있어요."),
    FINISH("현상/스캔 완료되었어요.");


    private final String message;

    NotificationType(String message) {
        this.message = message;
    }
}
