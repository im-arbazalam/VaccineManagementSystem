package com.example.VaccineManagementSystem.Models;

import com.example.VaccineManagementSystem.Enum.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctors")
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int docId;


    String name;


    int age;


    @Enumerated(EnumType.STRING)
    Gender gender;


    @Column(unique = true)
    String emailId;
}
