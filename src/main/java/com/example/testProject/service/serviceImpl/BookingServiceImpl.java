package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.booking.BookingRequestDTO;
import com.example.testProject.dto.booking.BookingResponseDTO;
import com.example.testProject.entity.*;
import com.example.testProject.enums.BookingStatus;
import com.example.testProject.mapper.BookingMapper;
import com.example.testProject.repository.BookingRepository;
import com.example.testProject.repository.BusinessRepository;
import com.example.testProject.repository.EmployeeRepository;
import com.example.testProject.repository.UserRepository;
import com.example.testProject.security.CustomUserDetails;
import com.example.testProject.service.BookingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final EmployeeRepository employeeRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            BusinessRepository businessRepository,
            EmployeeRepository employeeRepository,
            BookingMapper bookingMapper
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.employeeRepository = employeeRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override

    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Employee employee = null;
        if (dto.getEmployeeId() != null) {
            employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBusiness(business);
        booking.setEmployee(employee);
        booking.setDateTime(dto.getDateTime());
        booking.setPrice(dto.getPrice());
        booking.setStatus(BookingStatus.PENDING);

        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUser(Long userId) {
        return bookingRepository.findByUser_Id(userId)
                .stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> getBookingsByBusiness(Long businessId) {
        return bookingRepository.findByBusiness_Id(businessId)
                .stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> getBusinessBookings() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof CustomUserDetails cud)) {
            throw new RuntimeException("Unauthorized");
        }

        Business business = businessRepository.findByOwnerId(cud.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        return getBookingsByBusiness(business.getId());
    }

    @Override
    public BookingResponseDTO confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDTO(saved);
    }

    @Override
    public BookingResponseDTO getById(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<BookingResponseDTO> getAll() {
        return bookingRepository.findAll().stream().map(bookingMapper::toDTO).toList();
    }

    @Override
    public BookingResponseDTO update(Long id, BookingRequestDTO dto) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Employee employee = null;
        if (dto.getEmployeeId() != null) {
            employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
        }

        booking.setBusiness(business);
        booking.setEmployee(employee);
        booking.setDateTime(dto.getDateTime());
        booking.setPrice(dto.getPrice());

        // ❗ user НЕ трогаем вообще

        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}