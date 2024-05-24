package io.spring.slice.application.domain.board.repository;

import io.spring.slice.application.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQuerydslRepository {
}
