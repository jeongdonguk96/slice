package io.spring.slice.application.service;

import io.spring.slice.application.common.CursorResult;
import io.spring.slice.application.domain.board.Board;
import io.spring.slice.application.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SliceService {

    private final BoardRepository boardRepository;


    @Transactional(readOnly = true)
    public CursorResult<Board> getSlice(
            Long cursorId,
            Pageable pageable
    ) {

        Slice<Board> boardList = getBoardList(cursorId, pageable);
        Boolean hasNext = boardList.isEmpty()
                ? null
                : boardList.hasNext();

        return new CursorResult<>(boardList, hasNext);
    }


    private Slice<Board> getBoardList(
            Long cursorId,
            Pageable pageable
    ) {

        return boardRepository.findAllLessThanCursorIdOrderByIdDesc(cursorId, pageable);
    }

}
