package io.spring.slice.application.domain.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spring.slice.application.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.spring.slice.application.domain.board.QBoard.board;
import static io.spring.slice.application.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class BoardQuerydslRepositoryImpl implements BoardQuerydslRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Slice<Board> findAllLessThanCursorIdOrderByIdDesc(Long cursorId, Pageable pageable) {
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.member, member)
                .fetchJoin()
                .where(getLtCursorId(cursorId))
                .orderBy(board.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, boards);
    }


    @Override
    public Slice<Board> searchLessThanCursorIdOrderByIdDescWith(Long cursorId, Pageable pageable, String keyword) {
        List<Board> boards = queryFactory.selectFrom(board)
                .leftJoin(board.member, member)
                .fetchJoin()
                .where(getLtCursorId(cursorId), search(keyword))
                .orderBy(board.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, boards);
    }


    // cursorId의 여부를 확인한 다음, 있다면 cursorId 보다 id가 작은 데이터를 반환한다.
    private BooleanExpression getLtCursorId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }

        return board.id.lt(cursorId);
    }


    // cursorId의 여부를 확인한 다음, 있다면 cursorId 보다 id가 작은 데이터를 반환한다.
    private BooleanExpression search(String keyword) {
        if (keyword == null) {
            return null;
        }

        return board.title.contains(keyword);
    }


    // Slice 객체를 반환하여 무한 스크롤을 처리한다.
    private Slice<Board> checkLastPage(Pageable pageable, List<Board> boards) {
        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 true, 작으면 false
        if (boards.size() > pageable.getPageSize()) {
            boards.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(boards, pageable, hasNext);
    }

}
