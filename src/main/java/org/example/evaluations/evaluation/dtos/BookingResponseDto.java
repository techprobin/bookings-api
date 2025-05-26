package org.example.evaluations.evaluation.dtos;

import lombok.Data;
import org.example.evaluations.evaluation.models.Guest;
import org.example.evaluations.evaluation.models.Room;

import java.util.Date;
import java.util.List;

@Data
public class BookingResponseDto {
    private Long bookingId;

    private Double totalBill;

    private Guest guest;

    private List<Room> rooms;

    private Date checkInDate;

    private Date checkOutDate;
}
