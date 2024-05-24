package io.spring.slice.application.controller;

import io.spring.slice.application.common.CursorResult;
import io.spring.slice.application.domain.board.Board;
import io.spring.slice.application.service.SliceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SliceController {

    private static final int DEFAULT_SIZE = 10;

    private final SliceService sliceService;


    @GetMapping("/slice")
    public CursorResult<Board> getSlice(
            @RequestParam(value = "cursorId", required = false) Long cursorId
    ) {

        return sliceService.getSlice(cursorId, PageRequest.of(0, DEFAULT_SIZE));
    }

}
