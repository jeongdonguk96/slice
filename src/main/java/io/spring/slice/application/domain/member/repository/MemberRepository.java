package io.spring.slice.application.domain.member.repository;

import io.spring.slice.application.domain.member.Member;
import io.spring.slice.application.domain.member.repository.MemberQuerydslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {

}
