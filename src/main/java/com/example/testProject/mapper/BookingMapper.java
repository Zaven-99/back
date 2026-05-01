package com.example.testProject.mapper;

import com.example.testProject.dto.booking.BookingResponseDTO;
import com.example.testProject.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface BookingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "businessId", source = "business.id")
    @Mapping(target = "employeeId", source = "employee.id")
    BookingResponseDTO toDTO(Booking booking);

}