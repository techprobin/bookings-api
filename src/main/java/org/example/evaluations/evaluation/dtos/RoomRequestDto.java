package org.example.evaluations.evaluation.dtos;

import lombok.Data;
import org.example.evaluations.evaluation.models.RoomType;

@Data
public class RoomRequestDto {
    RoomType roomType;
    int roomCount;
}
