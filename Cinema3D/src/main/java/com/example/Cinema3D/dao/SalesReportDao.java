package com.example.Cinema3D.dao;

import com.example.Cinema3D.dto.SalesReportDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SalesReportDao {

    private final JdbcTemplate jdbcTemplate;

    public SalesReportDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ===============================
    // SELECT + query() + RowMapper
    // ===============================
    public List<SalesReportDto> getSalesReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT
                DATE(ss.reserved_at) AS date,
                COUNT(*) AS tickets_sold,
                SUM(
                    CASE ss.ticket_type
                        WHEN 'NORMAL' THEN 30
                        WHEN 'DISCOUNT' THEN 20
                        WHEN 'FAMILY' THEN 15
                        ELSE 0
                    END
                ) AS revenue
            FROM screening_seat ss
            WHERE ss.status = 'SOLD'
              AND ss.reserved_at BETWEEN ? AND ?
            GROUP BY DATE(ss.reserved_at)
            ORDER BY date
        """;

        return jdbcTemplate.query(
                sql,
                SALES_REPORT_ROW_MAPPER,
                from.atStartOfDay(),
                to.atTime(23, 59, 59)
        );
    }

    // ===============================
    // UPDATE (wymagane punktowo)
    // ===============================
    public int markSeatsAsSoldForScreening(Long screeningId) {
        String sql = """
            UPDATE screening_seat
            SET status = 'SOLD'
            WHERE screening_id = ?
              AND status = 'RESERVED'
        """;

        return jdbcTemplate.update(sql, screeningId);
    }

    // ===============================
    // EXPLICIT ROW MAPPER
    // ===============================
    private static final RowMapper<SalesReportDto> SALES_REPORT_ROW_MAPPER =
            new RowMapper<>() {
                @Override
                public SalesReportDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new SalesReportDto(
                            rs.getDate("date").toLocalDate(),
                            rs.getLong("tickets_sold"),
                            rs.getBigDecimal("revenue")
                    );
                }
            };
}
