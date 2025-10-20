package com.example.benefits.repository;

import com.example.benefits.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProviderRepository extends JpaRepository<Provider, UUID> {
    // Add custom queries as needed

}
