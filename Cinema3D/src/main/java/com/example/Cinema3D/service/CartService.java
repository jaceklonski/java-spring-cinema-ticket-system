package com.example.Cinema3D.service;

import com.example.Cinema3D.entity.CartItem;
import com.example.Cinema3D.entity.ScreeningSeat;
import com.example.Cinema3D.entity.SeatStatus;
import com.example.Cinema3D.entity.TicketType;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String CART = "CART";

    private final ScreeningSeatRepository screeningSeatRepository;

    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART, cart);
        }
        return cart;
    }

    @Transactional
    public void addSeats(
            HttpSession session,
            Long screeningId,
            List<Integer> rows,
            List<Integer> seats
    ) {
        List<CartItem> cart = getCart(session);

        for (int i = 0; i < rows.size(); i++) {

            ScreeningSeat ss = screeningSeatRepository
                    .findByScreeningIdAndSeatRowAndSeatNumber(
                            screeningId,
                            rows.get(i),
                            seats.get(i)
                    )
                    .orElseThrow(() -> new NotFoundException("Seat not found"));

            ScreeningSeat locked = screeningSeatRepository
                    .findForUpdate(
                            screeningId,
                            ss.getSeat().getId()
                    )
                    .orElseThrow(() -> new NotFoundException("Seat not found"));

            if (locked.getStatus() != SeatStatus.FREE) {
                throw new IllegalStateException("Seat already taken");
            }

            locked.setStatus(SeatStatus.RESERVED);
            locked.setReservedAt(LocalDateTime.now());
            screeningSeatRepository.save(locked);

            CartItem item = new CartItem();
            item.setScreeningId(screeningId);
            item.setRow(rows.get(i));
            item.setSeat(seats.get(i));
            item.setTicketType(null);

            cart.add(item);
        }
    }

    public void setTicketTypes(List<CartItem> cart, List<TicketType> ticketTypes) {
        if (cart.size() != ticketTypes.size()) {
            throw new IllegalStateException("Ticket types mismatch");
        }

        for (int i = 0; i < cart.size(); i++) {
            cart.get(i).setTicketType(ticketTypes.get(i));
        }
    }

    public void clear(HttpSession session) {
        session.removeAttribute(CART);
    }
}
