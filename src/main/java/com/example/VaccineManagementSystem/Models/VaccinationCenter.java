package com.example.VaccineManagementSystem.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Entity
@FieldDefaults(level= AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vaccination_center")
@Builder
public class VaccinationCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String centreName;


    private LocalTime openingTime;


    private LocalTime closingTime;


    private String address;


    private int doseCapacity;


}
