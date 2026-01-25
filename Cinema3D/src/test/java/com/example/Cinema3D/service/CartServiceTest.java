package com.example.Cinema3D.service;

import com.example.Cinema3D.entity.CartItem;
import com.example.Cinema3D.entity.ScreeningSeat;
import com.example.Cinema3D.entity.SeatStatus;
import com.example.Cinema3D.entity.TicketType;
import com.example.Cinema3D.exception.NotFoundException;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    ScreeningSeatRepository screeningSeatRepository;

    @Mock
    HttpSession session;

    @InjectMocks
    CartService cartService;

    List<CartItem> cart;

    @BeforeEach
    void setup() {
        cart = new ArrayList<>();
    }

    private void givenCartInSession() {
        when(session.getAttribute("CART")).thenReturn(cart);
    }

    @Test
    void getCart_createsNewCartWhenNotExists() {
        when(session.getAttribute("CART")).thenReturn(null);

        List<CartItem> result = cartService.getCart(session);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(session).setAttribute(eq("CART"), any(List.class));
    }

    @Test
    void getCart_returnsExistingCart() {
        givenCartInSession();

        List<CartItem> result = cartService.getCart(session);

        assertSame(cart, result);
    }

    @Test
    void addSeats_successfullyAddsSeat() {
        givenCartInSession();

        ScreeningSeat seat = new ScreeningSeat();
        seat.setStatus(SeatStatus.FREE);

        when(screeningSeatRepository.lockSeat(1L, 1, 2))
                .thenReturn(Optional.of(seat));

        cartService.addSeats(
                session,
                1L,
                List.of(1),
                List.of(2)
        );

        assertEquals(1, cart.size());

        CartItem item = cart.get(0);
        assertEquals(1L, item.getScreeningId());
        assertEquals(1, item.getRow());
        assertEquals(2, item.getSeat());
        assertNull(item.getTicketType());

        assertEquals(SeatStatus.RESERVED, seat.getStatus());
        assertNotNull(seat.getReservedAt());

        verify(screeningSeatRepository).save(seat);
    }

    @Test
    void addSeats_throwsWhenSeatNotFound() {
        givenCartInSession();

        when(screeningSeatRepository.lockSeat(anyLong(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                cartService.addSeats(
                        session,
                        1L,
                        List.of(1),
                        List.of(1)
                )
        );
    }

    @Test
    void addSeats_throwsWhenSeatAlreadyTaken() {
        givenCartInSession();

        ScreeningSeat seat = new ScreeningSeat();
        seat.setStatus(SeatStatus.RESERVED);

        when(screeningSeatRepository.lockSeat(1L, 1, 1))
                .thenReturn(Optional.of(seat));

        assertThrows(IllegalStateException.class, () ->
                cartService.addSeats(
                        session,
                        1L,
                        List.of(1),
                        List.of(1)
                )
        );
    }

    @Test
    void removeSeat_freesSeatAndRemovesFromCart() {
        givenCartInSession();

        ScreeningSeat seat = new ScreeningSeat();
        seat.setStatus(SeatStatus.RESERVED);
        seat.setReservedAt(LocalDateTime.now());

        when(screeningSeatRepository.lockSeat(1L, 1, 1))
                .thenReturn(Optional.of(seat));

        CartItem item = new CartItem();
        item.setScreeningId(1L);
        item.setRow(1);
        item.setSeat(1);
        cart.add(item);

        cartService.removeSeat(session, 1L, 1, 1);

        assertTrue(cart.isEmpty());
        assertEquals(SeatStatus.FREE, seat.getStatus());
        assertNull(seat.getReservedAt());
        assertNull(seat.getReservedBy());

        verify(screeningSeatRepository).save(seat);
    }

    @Test
    void removeSeat_ignoresWhenNotInCart() {
        givenCartInSession();

        ScreeningSeat seat = new ScreeningSeat();
        seat.setStatus(SeatStatus.FREE);

        when(screeningSeatRepository.lockSeat(1L, 1, 1))
                .thenReturn(Optional.of(seat));

        cartService.removeSeat(session, 1L, 1, 1);

        assertTrue(cart.isEmpty());
        verify(screeningSeatRepository, never()).save(any());
    }

    @Test
    void setTicketTypes_success() {
        CartItem c1 = new CartItem();
        CartItem c2 = new CartItem();

        List<CartItem> cart = List.of(c1, c2);
        List<TicketType> types = List.of(
                TicketType.NORMAL,
                TicketType.STUDENT
        );

        cartService.setTicketTypes(cart, types);

        assertEquals(TicketType.NORMAL, c1.getTicketType());
        assertEquals(TicketType.STUDENT, c2.getTicketType());
    }

    @Test
    void setTicketTypes_throwsOnSizeMismatch() {
        List<CartItem> cart = List.of(new CartItem());
        List<TicketType> types = List.of();

        assertThrows(IllegalStateException.class, () ->
                cartService.setTicketTypes(cart, types)
        );
    }

    @Test
    void clear_removesCartFromSession() {
        cartService.clear(session);

        verify(session).removeAttribute("CART");
        verifyNoMoreInteractions(session);
    }
}
