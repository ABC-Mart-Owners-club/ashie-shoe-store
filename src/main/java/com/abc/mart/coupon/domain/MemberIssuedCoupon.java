package com.abc.mart.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberIssuedCoupon {
    private String memberId;
    private String couponId;
    private LocalDateTime issuedAt;
    private boolean used;
}
