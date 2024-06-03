package io.spring.slice.application.domain.board.repository;

import io.spring.slice.application.domain.board.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardQuerydslRepository {

    Slice<Board> findAllLessThanCursorIdOrderByIdDesc(Long cursorId, Pageable pageable);

    Slice<Board> searchLessThanCursorIdOrderByIdDescWith(Long cursorId, Pageable pageable, String keyword);
}
