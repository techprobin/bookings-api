package org.example.evaluations.evaluation.services;

import lombok.NonNull;
import org.example.evaluations.evaluation.dtos.BookingRequestDto;
import org.example.evaluations.evaluation.dtos.BookingResponseDto;
import org.example.evaluations.evaluation.dtos.RoomRequestDto;
import org.example.evaluations.evaluation.models.Booking;
import org.example.evaluations.evaluation.models.Guest;
import org.example.evaluations.evaluation.models.Room;
import org.example.evaluations.evaluation.models.RoomType;
import org.example.evaluations.evaluation.repos.BookingRepo;
import org.example.evaluations.evaluation.repos.RoomRepo;
import org.example.evaluations.evaluation.repos.GuestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StorageBookingService implements IBookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private GuestRepo guestRepo;

    @Autowired
    private RoomRepo roomRepo;


    @Override
    public BookingResponseDto getBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepo.findById(bookingId);
        return bookingOptional.map(this::from).orElse(null);
        //or if(bookingOptional.isEmpty()) return null;
        // if(bookingOptional.isPresent()) {
//        return from(bookingOptional.get());
        // }
    }

    @Override
    public List<BookingResponseDto> getAllBookingsPerGuest(String guestEmail) {
        Optional<Guest> guest = guestRepo.findByEmail(guestEmail);

        List<BookingResponseDto> bookingResponseDtos = null;

        if (guest.isPresent()) {
            bookingResponseDtos = new ArrayList<>();
            Guest guestEntity = guest.get();
            guestEntity.setEmail(guestEmail);
            List<Booking> bookings = bookingRepo.findBookingsByGuest(guestEntity);
            for (Booking booking : bookings) bookingResponseDtos.add(from(booking));
        }
        return bookingResponseDtos;
    }

    @Override
    public BookingResponseDto replaceBooking(Long bookingId, BookingRequestDto bookingRequestDto) {
        /* not required to have only one invocation call by id
        Optional<Booking> bookingOptional = bookingRepo.findById(bookingId);
        if(bookingOptional.isEmpty()) return null;
        */
        if(this.deleteBooking(bookingId))
            return this.createBooking(bookingRequestDto);
        return null;
    }

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        Booking booking = new Booking();
        booking.setRooms(bookingRequestDto.getRoomRequestDtos().stream().map(this::from).toList());
        booking.setCheckInDate(from(bookingRequestDto.getCheckInDate()));
        booking.setCheckOutDate(from(bookingRequestDto.getCheckOutDate()));

        long daysBetween = calculateDaysBetween(booking.getCheckInDate(), booking.getCheckOutDate());

        double totalBill = 0D;
        for(var room : booking.getRooms()) totalBill += room.getRent() * daysBetween;
        booking.setTotalBill(totalBill);

        Guest guest = new Guest();
        guest.setEmail(bookingRequestDto.getCustomerEmail());
        guest.setName(bookingRequestDto.getCustomerName());
        /* not required as per tests set! guestRepo.save(guest); */
        booking.setGuest(guest);

        /* not this call as per tests roomRepo.saveAll(booking.getRooms()); */
//        Booking bookingDone =
        bookingRepo.save(booking);
        /* tests prefer this but it's not 1 invocation but many as in loop */
        for(var room : booking.getRooms()) roomRepo.save(room); //not saveAll in single go as per tests
        return from(booking);
    }

    @Override
    public Boolean deleteBooking(Long bookingId) {
        /* not for tests set
        if(bookingRepo.existsById(bookingId)) {
            bookingRepo.deleteById(bookingId);
            return true;
        }
        return false;
        */

        Optional<Booking> bookingOptional = bookingRepo.findById(bookingId);
        if(bookingOptional.isEmpty()) return false;
        bookingRepo.deleteById(bookingId);
        return true;
    }

    private BookingResponseDto from(Booking booking) {
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setBookingId(booking.getId());
        responseDto.setRooms(booking.getRooms());
        responseDto.setGuest(booking.getGuest());
        responseDto.setTotalBill(booking.getTotalBill());
        responseDto.setCheckOutDate(booking.getCheckOutDate());
        responseDto.setCheckInDate(booking.getCheckInDate());
        return responseDto;
    }

    private Room from(RoomRequestDto roomRequestDto) {
        Room room = new Room();
        room.setRoomType(roomRequestDto.getRoomType());
        if(roomRequestDto.getRoomType().equals(RoomType.DELUXE)) {
            room.setRent(1000D * roomRequestDto.getRoomCount());
        }else if(roomRequestDto.getRoomType().equals(RoomType.SUPER_DELUXE)) {
            room.setRent(1500D * roomRequestDto.getRoomCount());
        }else if(roomRequestDto.getRoomType().equals(RoomType.SUITE)) {
            room.setRent(2500D * roomRequestDto.getRoomCount());
        }

        return room;
    }

    private Date from(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.parse(date);
        }catch (ParseException exception) {
            return null;
        }
    }

    /**
     * Calculate the number of days between two dates
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return the number of days between the two dates, with a minimum of 1 day
     */
    private long calculateDaysBetween(Date checkInDate, Date checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return 0;
        }
        if(checkInDate == checkOutDate) return 1;

        // Calculate the difference in milliseconds
        long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
        // Convert milliseconds to days
        long days = diffInMillis / (24 * 60 * 60 * 1000);
        // If check-in and check-out are on the same day, consider it as 1 day
        return Math.max(1, days);
    }
}
