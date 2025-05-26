package org.example.evaluations.evaluation.controllers;

import java.util.List;

import org.example.evaluations.evaluation.dtos.BookingRequestDto;
import org.example.evaluations.evaluation.dtos.BookingResponseDto;
import org.example.evaluations.evaluation.services.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    //API1 GET /booking/guest/{guestEmail}
    @GetMapping("/guest/{guestEmail}")
    List<BookingResponseDto> getBookingsPerGuest(@PathVariable String guestEmail) {
        return bookingService.getAllBookingsPerGuest(guestEmail);
    }

    //API2 GET /booking/{bookingId}
    @GetMapping("/{bookingId}")
    BookingResponseDto getBookingDetails(@PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId);
    }

    //API3 POST /booking
    @PostMapping
    BookingResponseDto createBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.createBooking(bookingRequestDto);
    }

    //API4 PUT /booking/{bookingId}
    @PutMapping("/{bookingId}")
    BookingResponseDto replaceBooking(@PathVariable Long bookingId, @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.replaceBooking(bookingId, bookingRequestDto);
    }

    //API5 DELETE /booking
    @DeleteMapping("/{bookingId}")
    Boolean deleteBooking(@PathVariable Long bookingId) {
        return bookingService.deleteBooking(bookingId);
    }
}
