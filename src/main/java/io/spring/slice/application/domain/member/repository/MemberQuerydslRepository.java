package io.spring.slice.application.domain.member.repository;

import io.spring.slice.application.domain.member.Member;

import java.util.List;

public interface MemberQuerydslRepository {

    List<Member> getMembersGroupByName();

}
