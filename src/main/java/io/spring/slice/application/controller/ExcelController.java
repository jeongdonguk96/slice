package io.spring.slice.application.controller;

import io.spring.slice.application.domain.member.Member;
import io.spring.slice.application.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExcelController {

    private final MemberRepository memberRepository;


    @GetMapping(value = "/members/count")
    public void downloadMembersCountExcel(HttpServletResponse response) {
        long result = memberRepository.count();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("총 회원 수");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Count");

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(result);

            response.setContentType("ms-vnd/excel");
            response.setHeader("Content-Disposition", "attachment;filename=members_count.xls");

            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/members")
    public void downloadMembersExcel(HttpServletResponse response) {
        List<Member> results = memberRepository.getMembersGroupByName();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("전체 회원");

            Font font = workbook.createFont();
            font.setFontName("맑은 고딕");

            CellStyle defaultCellStyle = workbook.createCellStyle();
            defaultCellStyle.setFont(font);
            defaultCellStyle.setAlignment(HorizontalAlignment.CENTER);
            defaultCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            defaultCellStyle.setBorderTop(BorderStyle.THIN);
            defaultCellStyle.setBorderBottom(BorderStyle.THIN);
            defaultCellStyle.setBorderLeft(BorderStyle.THIN);
            defaultCellStyle.setBorderRight(BorderStyle.THIN);

            CellStyle borderCellStyle = workbook.createCellStyle();
            borderCellStyle.cloneStyleFrom(defaultCellStyle);
            borderCellStyle.setBorderTop(BorderStyle.THICK);
            borderCellStyle.setBorderBottom(BorderStyle.THICK);
            borderCellStyle.setBorderLeft(BorderStyle.THICK);
            borderCellStyle.setBorderRight(BorderStyle.THICK);

            Row headerRow = sheet.createRow(0);
            Field[] memberFields = Member.class.getDeclaredFields();

            for (int i = 0; i < memberFields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(memberFields[i].getName());
                if (i == 0 || i == memberFields.length - 1) {
                    cell.setCellStyle(borderCellStyle);
                } else {
                    cell.setCellStyle(defaultCellStyle);
                }
            }

            int rowNum = 1;

            for (Member member : results) {
                Row dataRow = sheet.createRow(rowNum++);

                for (int i = 0; i < memberFields.length; i++) {
                    memberFields[i].setAccessible(true);
                    Object field = memberFields[i].get(member);

                    Cell cell = dataRow.createCell(i);
                    if (field instanceof Number) {
                        cell.setCellValue(((Number) field).doubleValue());
                    } else {
                        cell.setCellValue(field != null ? field.toString() : "");
                    }
                    if (i == 0 || i == memberFields.length - 1 || rowNum == 2 || rowNum == results.size() + 1) {
                        cell.setCellStyle(borderCellStyle);
                    } else {
                        cell.setCellStyle(defaultCellStyle);
                    }
                }
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=members_count.xlsx");

            workbook.write(response.getOutputStream());
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
