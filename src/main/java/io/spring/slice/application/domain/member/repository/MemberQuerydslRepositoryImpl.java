package io.spring.slice.application.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spring.slice.application.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.spring.slice.application.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQuerydslRepositoryImpl implements MemberQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> getMembersGroupByName() {
        return queryFactory.selectFrom(member)
                .groupBy(member.id)
                .orderBy(member.id.asc())
                .fetch();
    }

}
