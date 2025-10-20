package com.example.benefits.model;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private List<Enrollment> enrollments;

}
