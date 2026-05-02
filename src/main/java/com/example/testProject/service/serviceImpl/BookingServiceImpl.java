package com.example.testProject.service.serviceImpl;

import com.example.testProject.dto.availability.AvailabilityRequestDTO;
import com.example.testProject.dto.booking.BookingFilterRequestDTO;
import com.example.testProject.dto.booking.BookingRequestDTO;
import com.example.testProject.dto.booking.BookingResponseDTO;
import com.example.testProject.dto.realtime.DashboardUpdateDTO;
import com.example.testProject.entity.*;
import com.example.testProject.enums.BookingStatus;
import com.example.testProject.mapper.BookingMapper;
import com.example.testProject.repository.*;
import com.example.testProject.security.SecurityUtils;
import com.example.testProject.service.BookingService;
import com.example.testProject.service.availability.AvailabilityService;
import com.example.testProject.service.realtime.RealtimeDashboardService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BusinessRepository businessRepository;
    private final EmployeeRepository employeeRepository;
    private final BookingMapper bookingMapper;
    private final RealtimeDashboardService realtimeDashboardService;
    private final AvailabilityService availabilityService;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BusinessRepository businessRepository,
            EmployeeRepository employeeRepository,
            BookingMapper bookingMapper,
            RealtimeDashboardService realtimeDashboardService,
            AvailabilityService availabilityService
    ) {
        this.bookingRepository = bookingRepository;
        this.businessRepository = businessRepository;
        this.employeeRepository = employeeRepository;
        this.bookingMapper = bookingMapper;
        this.realtimeDashboardService = realtimeDashboardService;
        this.availabilityService = availabilityService;
    }

    // ================= CREATE =================
    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) {

        User user = SecurityUtils.currentUser();

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Employee employee = null;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
        }

        AvailabilityRequestDTO availabilityRequest = new AvailabilityRequestDTO();
        availabilityRequest.setBusinessId(request.getBusinessId());
        availabilityRequest.setServiceId(request.getServiceId());
        availabilityRequest.setDate(request.getDateTime().toLocalDate());

        if (!availabilityService.isAvailable(availabilityRequest)) {
            throw new RuntimeException("Time slot not available");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBusiness(business);
        booking.setEmployee(employee);
        booking.setDateTime(request.getDateTime());
        booking.setPrice(request.getPrice());
        booking.setStatus(BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);

        sendRealtime(business.getId(), "BOOKING_CREATED", bookingMapper.toDTO(saved));

        return bookingMapper.toDTO(saved);
    }

    // ================= UPDATE =================
    @Override
    @Transactional
    public BookingResponseDTO update(Long id, BookingRequestDTO request) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Long userId = SecurityUtils.currentUser().getId();

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be updated");
        }

        AvailabilityRequestDTO availabilityRequest = new AvailabilityRequestDTO();
        availabilityRequest.setBusinessId(request.getBusinessId());
        availabilityRequest.setServiceId(request.getServiceId());
        availabilityRequest.setDate(request.getDateTime().toLocalDate());

        if (!availabilityService.isAvailable(availabilityRequest)) {
            throw new RuntimeException("Time slot not available");
        }

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Employee employee = null;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
        }

        booking.setBusiness(business);
        booking.setEmployee(employee);
        booking.setDateTime(request.getDateTime());
        booking.setPrice(request.getPrice());

        Booking saved = bookingRepository.save(booking);

        sendRealtime(business.getId(), "BOOKING_UPDATED", bookingMapper.toDTO(saved));

        return bookingMapper.toDTO(saved);
    }

    // ================= REALTIME =================
    private void sendRealtime(Long businessId, String type, Object payload) {
        DashboardUpdateDTO update = new DashboardUpdateDTO();
        update.setBusinessId(businessId);
        update.setType(type);
        update.setPayload(payload);

        realtimeDashboardService.sendUpdate(update);
    }

    // ================= GET MY BOOKINGS =================
    @Override
    public List<BookingResponseDTO> getMyBookings() {

        Long userId = SecurityUtils.currentUser().getId();

        return bookingRepository.findByUser_Id(userId)
                .stream()
                .map(bookingMapper::toDTO)
                .toList();
    }

    // ================= BUSINESS BOOKINGS =================
    @Override
    public List<BookingResponseDTO> getBusinessBookings() {

        User user = SecurityUtils.currentUser();

        Business business = businessRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        return bookingRepository.findByBusiness_Id(business.getId())
                .stream()
                .map(bookingMapper::toDTO)
                .toList();
    }

    // ================= GET BY ID =================
    @Override
    public BookingResponseDTO getMyBookingById(Long id) {

        Long userId = SecurityUtils.currentUser().getId();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        return bookingMapper.toDTO(booking);
    }

    // ================= CANCEL =================
    @Override
    @Transactional
    public void cancel(Long id) {

        Long userId = SecurityUtils.currentUser().getId();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new RuntimeException("Already cancelled");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed booking");
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);

        sendRealtime(booking.getBusiness().getId(), "BOOKING_CANCELLED", bookingMapper.toDTO(booking));
    }

    // ================= DELETE =================
    @Override
    @Transactional
    public void delete(Long id) {

        Long userId = SecurityUtils.currentUser().getId();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot delete completed booking");
        }

        bookingRepository.delete(booking);

        sendRealtime(booking.getBusiness().getId(), "BOOKING_DELETED", id);
    }

    // ================= CONFIRM =================
    @Override
    @Transactional
    public BookingResponseDTO confirm(Long id) {

        Long userId = SecurityUtils.currentUser().getId();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getBusiness().getOwner().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        Booking saved = bookingRepository.save(booking);

        sendRealtime(booking.getBusiness().getId(), "BOOKING_CONFIRMED", bookingMapper.toDTO(saved));

        return bookingMapper.toDTO(saved);
    }

    // ================= COMPLETE =================
    @Override
    @Transactional
    public BookingResponseDTO complete(Long id) {

        Long userId = SecurityUtils.currentUser().getId();

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getBusiness().getOwner().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);

        Booking saved = bookingRepository.save(booking);

        sendRealtime(booking.getBusiness().getId(), "BOOKING_COMPLETED", bookingMapper.toDTO(saved));

        return bookingMapper.toDTO(saved);
    }

    // ================= FILTER =================
    @Override
    public List<BookingResponseDTO> filter(BookingFilterRequestDTO request) {

        return bookingRepository.findAll().stream()
                .filter(b -> request.getBusinessId() == null ||
                        b.getBusiness().getId().equals(request.getBusinessId()))

                .filter(b -> request.getEmployeeId() == null ||
                        (b.getEmployee() != null &&
                                b.getEmployee().getId().equals(request.getEmployeeId())))

                .filter(b -> request.getStatus() == null ||
                        b.getStatus().name().equals(request.getStatus()))

                .filter(b -> request.getMinPrice() == null ||
                        b.getPrice() >= request.getMinPrice())

                .filter(b -> request.getMaxPrice() == null ||
                        b.getPrice() <= request.getMaxPrice())

                .filter(b -> request.getFromDate() == null ||
                        !b.getDateTime().toLocalDate().isBefore(request.getFromDate()))

                .filter(b -> request.getToDate() == null ||
                        !b.getDateTime().toLocalDate().isAfter(request.getToDate()))

                .map(bookingMapper::toDTO)
                .toList();
    }
}