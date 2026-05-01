package com.example.testProject.entity;


import com.example.testProject.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Business business;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}
