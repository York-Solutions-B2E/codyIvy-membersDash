package com.example.benefits.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private LocalDate coverageStart;

    @Column(nullable = false)
    private LocalDate coverageEnd;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "enrollmentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accumulator> accumulators;
}
