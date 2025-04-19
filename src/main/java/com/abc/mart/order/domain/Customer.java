package com.abc.mart.order.domain;

import com.abc.mart.common.annotation.ValueObject;
import com.abc.mart.member.domain.Member;
import lombok.AllArgsConstructor;

@ValueObject
@AllArgsConstructor
public class Customer {
    String memberId;
    String name;
    String email;

    public static Customer from(Member member){
        return new Customer(member.getMemberId(), member.getName(), member.getEmail());
    }
}
