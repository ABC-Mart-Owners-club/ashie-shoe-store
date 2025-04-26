package com.abc.mart.member.domain;

import com.abc.mart.common.annotation.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AggregateRoot
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Getter
    String memberId;

    @Getter
    String name;

    @Getter
    String email;
    String phoneNum;

    public static Member of(String memberId, String name, String email, String phoneNum) {
        return new Member(memberId, name, email, phoneNum);
    }
}
