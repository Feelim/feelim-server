package cmc.feelim.domain.order;

public enum OrderState {
    READY("현상소에서 고객님의 주문을 확인하고 있어요."),
    MOVE("고객님의 필름이 현상소로 이동하고 있어요."),
    DEVELOPMENT("필름을 현상/스캔하고 있어요."),
    FINISH("현상/스캔 완료되었어요.");

    private final String message;

    OrderState(String message) {
        this.message = message;
    }
}
