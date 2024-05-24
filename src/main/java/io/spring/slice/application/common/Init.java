package io.spring.slice.application.common;

import io.spring.slice.application.domain.board.Board;
import io.spring.slice.application.domain.board.repository.BoardRepository;
import io.spring.slice.application.domain.member.Member;
import io.spring.slice.application.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Init {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @PostConstruct
    private void setUp() {
        List<Member> newMemberList = new ArrayList<>();
        List<Board> newBoardList = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            Member newMember = Member.builder()
                    .username("tester " + i)
                    .password("1234")
                    .name("동욱 " + i)
                    .age(30)
                    .build();

            newMemberList.add(newMember);
        }

        for (int i = 1; i <= 500; i++) {
            int j = i-1;
            if (i > 100 && i < 201) {
                j = i-101;
            } else if (i > 200 && i < 301) {
                j = i-201;
            } else if (i > 300 && i < 401) {
                j = i-301;
            } else if (i > 400) {
                j = i-401;
            }

            Board newBoard = Board.builder()
                    .title("title " + i)
                    .content("content " + i)
                    .member(newMemberList.get(j))
                    .build();

            newBoardList.add(newBoard);
        }

        List<Member> memberList = memberRepository.saveAll(newMemberList);
        List<Board> boardList = boardRepository.saveAll(newBoardList);

        log.info("Member 더미 데이터 생성 완료! 데이터 수 = {}", memberList.size());
        log.info("Board 더미 데이터 생성 완료! 데이터 수 = {}", boardList.size());
    }

}
