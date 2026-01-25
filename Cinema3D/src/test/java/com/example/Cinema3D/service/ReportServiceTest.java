package com.example.Cinema3D.service;

import com.example.Cinema3D.dao.SalesReportDao;
import com.example.Cinema3D.dto.SalesReportDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    SalesReportDao salesReportDao;

    @InjectMocks
    ReportService reportService;

    @Test
    void getSalesReport_returnsDataFromDao() {
        // given
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);

        SalesReportDto dto = new SalesReportDto(
                LocalDate.of(2024, 1, 10),
                120L,
                new BigDecimal("2450.50")
        );

        List<SalesReportDto> expectedResult = List.of(dto);

        when(salesReportDao.getSalesReport(from, to))
                .thenReturn(expectedResult);

        // when
        List<SalesReportDto> result =
                reportService.getSalesReport(from, to);

        // then
        assertSame(expectedResult, result);
        verify(salesReportDao).getSalesReport(from, to);
        verifyNoMoreInteractions(salesReportDao);
    }
}
