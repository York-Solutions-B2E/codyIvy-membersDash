package com.example.benefits.repository;

import com.example.benefits.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    // Add custom queries as needed
}