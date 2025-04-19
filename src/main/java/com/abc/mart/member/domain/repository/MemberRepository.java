package com.abc.mart.member.domain.repository;

import com.abc.mart.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

    public Member findByMemberId(String memberId);
}
