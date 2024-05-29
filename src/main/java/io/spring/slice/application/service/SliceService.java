package io.spring.slice.application.service;

import io.spring.slice.application.common.CursorResult;
import io.spring.slice.application.domain.board.Board;
import io.spring.slice.application.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SliceService {

    private final BoardRepository boardRepository;


    @Transactional(readOnly = true)
    public CursorResult<Board> getSlice(Long cursorId, Pageable pageable) {
        Slice<Board> boardList = boardRepository.findAllLessThanCursorIdOrderByIdDesc(cursorId, pageable);
        Boolean hasNext = boardList.isEmpty()
                ? null
                : boardList.hasNext();

        log.info("조회한 board의 개수 = {}", boardList.stream().count());
        log.info("마지막 페이지 여부 = {}", boardList.isLast());
        return new CursorResult<>(boardList, hasNext);
    }


    @Transactional(readOnly = true)
    public CursorResult<Board> getSearch(Long cursorId, Pageable pageable, String keyword) {
        Slice<Board> boardList = boardRepository.searchLessThanCursorIdOrderByIdDescWith(cursorId, pageable, keyword);
        Boolean hasNext = boardList.isEmpty()
                ? null
                : boardList.hasNext();

        log.info("조회한 board의 개수 = {}", boardList.stream().count());
        log.info("마지막 페이지 여부 = {}", boardList.isLast());
        return new CursorResult<>(boardList, hasNext);
    }
}
