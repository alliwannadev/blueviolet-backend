package com.blueviolet.backend.common.constant;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    /**
     * 1. 기본적인 주문 상태 흐름 <br>
     * 주문 요청 -> 주문 완료 -> 출고 요청 -> 출고 처리 중 -> 출고 완료 -> 배송 시작 -> 배송 완료 -> 구매 확정 <br> <br>
     *
     * 2. 상품을 교환하는 경우 <br>
     * 주문 요청 -> ... -> 배송 완료 -> 교환 요청 -> 교환 완료 또는 교환 취소 <br> <br>
     *
     * 3. 환불하는 경우 <br>
     * 주문 요청 -> ... -> 배송 완료 -> 환불 요청 -> 환불 처리 중 -> 환불 완료 또는 환불 취소
     */

    // 기본적인 주문 프로세스
    ORDER_REQUEST("ORDER_REQ", "주문 요청"),
    ORDER_COMPLETED("ORDER_CPD", "주문 완료"),
    RELEASE_REQUEST(
            ReleaseStatus.RELEASE_REQUEST.getCode(),
            ReleaseStatus.RELEASE_REQUEST.getDescription()
    ),
    RELEASE_IN_PROGRESS(
            ReleaseStatus.RELEASE_IN_PROGRESS.getCode(),
            ReleaseStatus.RELEASE_IN_PROGRESS.getDescription()
    ),
    RELEASE_COMPLETED(
            ReleaseStatus.RELEASE_COMPLETED.getCode(),
            ReleaseStatus.RELEASE_COMPLETED.getDescription()
    ),
    DELIVERY_START("DLY_START", "배송 시작"),
    DELIVERY_COMPLETED("DLY_CPD", "배송 완료"),
    PURCHASE_CONFIRMED("PCE_CFD", "구매 확정"),

    // 교환
    EXCHANGE_REQUEST(
            ExchangeStatus.EXCHANGE_REQUEST.getCode(),
            ExchangeStatus.EXCHANGE_REQUEST.getDescription()
    ),
    EXCHANGE_COMPLETED(
            ExchangeStatus.EXCHANGE_COMPLETED.getCode(),
            ExchangeStatus.EXCHANGE_COMPLETED.getDescription()
    ),
    EXCHANGE_CANCELLED(
            ExchangeStatus.EXCHANGE_CANCELLED.getCode(),
            ExchangeStatus.EXCHANGE_CANCELLED.getDescription()
    ),

    // 환불
    REFUND_REQUEST(
            RefundStatus.REFUND_REQUEST.getCode(),
            RefundStatus.REFUND_REQUEST.getDescription()
    ),
    REFUND_IN_PROGRESS(
            RefundStatus.REFUND_IN_PROGRESS.getCode(),
            RefundStatus.REFUND_IN_PROGRESS.getDescription()
    ),
    REFUND_COMPLETED(
            RefundStatus.REFUND_COMPLETED.getCode(),
            RefundStatus.REFUND_COMPLETED.getDescription()
    ),
    REFUND_CANCELLED(
            RefundStatus.REFUND_CANCELLED.getCode(),
            RefundStatus.REFUND_CANCELLED.getDescription()
    );

    private final String code;
    private final String description;

    public static OrderStatus getByCode(String code) {
        return Arrays.stream(OrderStatus.values())
                .filter(status -> code.equalsIgnoreCase(status.code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        MessageFormat.format("존재하지 않는 코드({0})를 전달했습니다.", code)
                ));
    }
}
