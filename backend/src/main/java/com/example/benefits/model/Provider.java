package com.example.benefits.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "providers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String specialty;
    private String address;
    private String phoneNumber;
}
