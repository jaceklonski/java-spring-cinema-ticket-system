package com.example.Cinema3D.service;

import com.example.Cinema3D.dao.SalesReportDao;
import com.example.Cinema3D.dto.SalesReportDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final SalesReportDao salesReportDao;

    public ReportService(SalesReportDao salesReportDao) {
        this.salesReportDao = salesReportDao;
    }

    public List<SalesReportDto> getSalesReport(LocalDate from, LocalDate to) {
        return salesReportDao.getSalesReport(from, to);
    }
}
