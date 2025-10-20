package com.example.benefits.repository;

import com.example.benefits.model.Accumulator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import com.example.benefits.model.Enrollment;

public interface AccumulatorRepository extends JpaRepository<Accumulator, UUID> {
    List<Accumulator> findByEnrollmentId(UUID enrollmentId);
    List<Accumulator> findByEnrollment(Enrollment enrollment);

}