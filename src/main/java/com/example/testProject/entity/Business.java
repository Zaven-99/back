package com.example.testProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "businesses")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private String email;
    private String phone;

    // 📍 GEO LOCATION (для "near me")
    private Double latitude;
    private Double longitude;

    // ⭐ рейтинг (средний)
    private Double rating = 0.0;

    // 🔥 услуги бизнеса
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<ServiceEntity> services;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}