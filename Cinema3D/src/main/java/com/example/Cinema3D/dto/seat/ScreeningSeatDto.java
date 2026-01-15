package com.example.Cinema3D.dto.seat;

import com.example.Cinema3D.entity.SeatStatus;

public class ScreeningSeatDto {

    private int row;
    private int number;
    private SeatStatus status;

    public ScreeningSeatDto(int row, int number, SeatStatus status) {
        this.row = row;
        this.number = number;
        this.status = status;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public SeatStatus getStatus() {
        return status;
    }
}

