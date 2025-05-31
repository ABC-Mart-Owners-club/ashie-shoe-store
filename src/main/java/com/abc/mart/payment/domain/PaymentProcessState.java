package com.abc.mart.payment.domain;

public enum PaymentProcessState {
    REQUESTED, FAILED, APPROVED, UNEXPECTED_FAILURE
    //더 많아질 수 있지만 우선 간소히..
}
