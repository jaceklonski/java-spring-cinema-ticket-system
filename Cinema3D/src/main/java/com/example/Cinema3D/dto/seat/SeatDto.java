package com.example.Cinema3D.dto.seat;

import jakarta.validation.constraints.Min;

public class SeatDto {

    @Min(1)
    private int row;

    @Min(1)
    private int number;

    public SeatDto() {
    }

    public SeatDto(int row, int number) {
        this.row = row;
        this.number = number;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
