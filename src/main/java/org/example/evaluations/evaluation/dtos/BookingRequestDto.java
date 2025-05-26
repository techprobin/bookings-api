package org.example.evaluations.evaluation.dtos;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class BookingRequestDto {
    @NonNull
    private String customerName;

    @NonNull
    private String customerEmail;

    @NonNull
    private List<RoomRequestDto> roomRequestDtos;

    @NonNull
    private String checkInDate;

    @NonNull
    private String checkOutDate;
}
