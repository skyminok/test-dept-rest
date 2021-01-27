package com.example.org;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository
        extends JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {
}
