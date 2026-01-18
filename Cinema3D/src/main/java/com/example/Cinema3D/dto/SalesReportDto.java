package com.example.Cinema3D.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesReportDto(
        LocalDate date,
        long ticketsSold,
        BigDecimal revenue
) {}
