package org.example.evaluations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.evaluations.evaluation.controllers.BookingController;
import org.example.evaluations.evaluation.dtos.BookingRequestDto;
import org.example.evaluations.evaluation.dtos.BookingResponseDto;
import org.example.evaluations.evaluation.dtos.RoomRequestDto;
import org.example.evaluations.evaluation.models.Guest;
import org.example.evaluations.evaluation.models.Room;
import org.example.evaluations.evaluation.models.RoomType;
import org.example.evaluations.evaluation.services.IBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerMvcTest {

    @MockBean
    private IBookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllBookingsForGuest() throws Exception {
        BookingResponseDto booking = new BookingResponseDto();
        booking.setBookingId(1L);
        Guest guest = new Guest();
        guest.setEmail("guest");
        guest.setName("guest");
        booking.setGuest(guest);
        booking.setTotalBill(1000D);
        Room room = new Room();
        room.setRent(1000D);
        room.setRoomType(RoomType.DELUXE);
        room.setId(1L);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        booking.setRooms(rooms);

        List<BookingResponseDto> bookings = Collections.singletonList(booking);

        when(bookingService.getAllBookingsPerGuest(anyString())).thenReturn(bookings);

        mockMvc.perform(get("/booking/guest/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(bookings)));
    }

    @Test
    public void testGetBooking() throws Exception {
        BookingResponseDto booking = new BookingResponseDto();
        booking.setBookingId(2L);
        Guest guest = new Guest();
        guest.setEmail("guest2");
        guest.setName("guest2");
        booking.setGuest(guest);
        booking.setTotalBill(2500D);
        Room room = new Room();
        room.setRent(2500D);
        room.setRoomType(RoomType.SUITE);
        room.setId(3L);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        booking.setRooms(rooms);

        when(bookingService.getBooking(anyLong())).thenReturn(booking);

        mockMvc.perform(get("/booking/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(booking)));
    }

    @Test
    public void testReplaceBooking() throws Exception {
        RoomRequestDto roomRequestDto = new RoomRequestDto();
        roomRequestDto.setRoomCount(2);
        roomRequestDto.setRoomType(RoomType.DELUXE);
        List<RoomRequestDto> rooms = new ArrayList<>();
        rooms.add(roomRequestDto);
        BookingRequestDto bookingRequest = new BookingRequestDto("guest2","guest2",rooms,"2024-10-10","2024-10-15");

        BookingResponseDto booking = new BookingResponseDto();
        booking.setBookingId(2L);
        Guest guest = new Guest();
        guest.setEmail("guest2");
        guest.setName("guest2");
        booking.setGuest(guest);
        booking.setTotalBill(2500D);
        Room room = new Room();
        room.setRent(2500D);
        room.setRoomType(RoomType.SUITE);
        room.setId(3L);
        List<Room> roomsResponse = new ArrayList<>();
        roomsResponse.add(room);
        booking.setRooms(roomsResponse);

        when(bookingService.replaceBooking(anyLong(), any(BookingRequestDto.class))).thenReturn(booking);

        mockMvc.perform(put("/booking/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(booking)));
    }

    @Test
    public void testCreateBooking() throws Exception {
        RoomRequestDto roomRequestDto = new RoomRequestDto();
        roomRequestDto.setRoomCount(2);
        roomRequestDto.setRoomType(RoomType.DELUXE);
        List<RoomRequestDto> rooms = new ArrayList<>();
        rooms.add(roomRequestDto);
        BookingRequestDto bookingRequest = new BookingRequestDto("guest2","guest2",rooms,"2024-10-10","2024-10-15");

        BookingResponseDto booking = new BookingResponseDto();
        booking.setBookingId(2L);
        Guest guest = new Guest();
        guest.setEmail("guest2");
        guest.setName("guest2");
        booking.setGuest(guest);
        booking.setTotalBill(2500D);
        Room room = new Room();
        room.setRent(2500D);
        room.setRoomType(RoomType.SUITE);
        room.setId(3L);
        List<Room> roomsResponse = new ArrayList<>();
        roomsResponse.add(room);
        booking.setRooms(roomsResponse);

        when(bookingService.createBooking(any(BookingRequestDto.class))).thenReturn(booking);

        mockMvc.perform(post("/booking")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(booking)));
    }

    @Test
    public void testDeleteBooking() throws Exception {
        when(bookingService.deleteBooking(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/booking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
