package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.CinemaRoomCreateRequest;
import com.example.Cinema3D.entity.CinemaRoom;
import com.example.Cinema3D.entity.Seat;
import com.example.Cinema3D.repository.CinemaRoomRepository;
import com.example.Cinema3D.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CinemaRoomServiceTest {

    @Mock
    CinemaRoomRepository cinemaRoomRepository;

    @Mock
    SeatRepository seatRepository;

    @InjectMocks
    CinemaRoomService cinemaRoomService;

    @Test
    void createRoom_createsRoomAndSeatsCorrectly() {
        CinemaRoomCreateRequest request = new CinemaRoomCreateRequest();
        request.setName("Sala 3D");
        request.setRows(3);
        request.setSeatsPerRow(4);

        ArgumentCaptor<CinemaRoom> roomCaptor =
                ArgumentCaptor.forClass(CinemaRoom.class);
        ArgumentCaptor<Seat> seatCaptor =
                ArgumentCaptor.forClass(Seat.class);

        cinemaRoomService.createRoom(request);

        verify(cinemaRoomRepository).save(roomCaptor.capture());
        CinemaRoom savedRoom = roomCaptor.getValue();

        assertEquals("Sala 3D", savedRoom.getName());

        verify(seatRepository, times(12)).save(seatCaptor.capture());

        for (Seat seat : seatCaptor.getAllValues()) {
            assertNotNull(seat.getRoom());
            assertSame(savedRoom, seat.getRoom());
            assertTrue(seat.getRow() >= 1 && seat.getRow() <= 3);
            assertTrue(seat.getNumber() >= 1 && seat.getNumber() <= 4);
        }
    }
}
