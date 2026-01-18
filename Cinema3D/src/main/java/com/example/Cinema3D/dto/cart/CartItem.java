package com.example.Cinema3D.dto.cart;

import com.example.Cinema3D.entity.TicketType;

public class CartItem {

    private int row;
    private int seat;
    private TicketType ticketType;

    public CartItem(int row, int seat, TicketType ticketType) {
        this.row = row;
        this.seat = seat;
        this.ticketType = ticketType;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public TicketType getTicketType() {
        return ticketType;
    }
}
