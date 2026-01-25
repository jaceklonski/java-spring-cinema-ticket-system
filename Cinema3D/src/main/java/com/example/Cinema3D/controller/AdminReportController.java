package com.example.Cinema3D.controller;

import com.example.Cinema3D.dao.SalesReportDao;
import com.example.Cinema3D.dto.SalesReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminReportController {

    private final SalesReportDao salesReportDao;

    // =========================
    // VIEW
    // =========================
    @GetMapping("/admin/reports")
    public String reportForm() {
        return "admin-report";
    }

    // =========================
    // CSV EXPORT
    // =========================
    @GetMapping("/admin/reports/sales")
    public ResponseEntity<byte[]> generateCsv(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        List<SalesReportDto> report = salesReportDao.getSalesReport(from, to);

        StringBuilder csv = new StringBuilder();
        csv.append("date,tickets_sold,revenue\n");

        for (SalesReportDto r : report) {
            csv.append(r.date())
                    .append(",")
                    .append(r.ticketsSold())
                    .append(",")
                    .append(r.revenue())
                    .append("\n");
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=sales-report-" + from + "_to_" + to + ".csv"
                )
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }
}
