package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.booking.BookingRequestDTO;
import com.example.testProject.dto.booking.BookingResponseDTO;
import com.example.testProject.entity.*;
import com.example.testProject.enums.BookingStatus;
import com.example.testProject.mapper.BookingMapper;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.UserRepository;
import com.example.testProject.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            BusinessRepository businessRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBusiness(business);
        booking.setDateTime(dto.getDateTime());
        booking.setStatus(BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);

        return BookingMapper.toDTO(saved);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUser(Long userId) {
        return bookingRepository.findByUser_Id(userId)
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> getBookingsByBusiness(Long businessId) {
        return bookingRepository.findByBusiness_Id(businessId)
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());
    }
}