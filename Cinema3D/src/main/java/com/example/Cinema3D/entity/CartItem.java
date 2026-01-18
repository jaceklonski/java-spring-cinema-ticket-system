package com.example.Cinema3D.entity;

public class CartItem {

    private Long screeningId;
    private int row;
    private int seat;
    private TicketType ticketType;

    // ✅ WYMAGANY przez Spring / sesję / settery
    public CartItem() {
    }

    // ✅ WYGODNY konstruktor
    public CartItem(int row, int seat, TicketType ticketType) {
        this.row = row;
        this.seat = seat;
        this.ticketType = ticketType;
    }

    public Long getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
}
