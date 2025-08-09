package com.parameta.repository;

import com.parameta.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface EmployeeRepository extends JpaRepository<Employee, Long> {
        boolean existsByNumeroDocumento(String numeroDocumento);
    }
