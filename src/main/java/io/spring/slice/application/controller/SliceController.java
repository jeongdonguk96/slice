package io.spring.slice.application.controller;

import io.spring.slice.application.common.CursorResult;
import io.spring.slice.application.domain.board.Board;
import io.spring.slice.application.domain.member.repository.MemberRepository;
import io.spring.slice.application.service.SliceService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SliceController {

    private static final int DEFAULT_SIZE = 10;

    private final SliceService sliceService;
    private final MemberRepository memberRepository;


    @GetMapping("/slice")
    public CursorResult<Board> getSlice(
            @RequestParam(value = "cursorId", required = false) Long cursorId
    ) {

        return sliceService.getSlice(cursorId, PageRequest.of(0, DEFAULT_SIZE));
    }

    @GetMapping("/test")
    public void test(HttpServletResponse response) {
        long result = memberRepository.count();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("총 회원 수");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("총 회원 수");

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(result);

            response.setContentType("ms-vnd/excel");
            response.setHeader("Content-Disposition", "attachment;filename=members_count.xlsx");

            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
