package com.parameta.controller;

import com.parameta.dto.EmployeeRequestParams;
import com.parameta.dto.EmployeeResponseDTO;
import com.parameta.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/register")
    public ResponseEntity<EmployeeResponseDTO> register(@Valid @ModelAttribute EmployeeRequestParams params) {
        return ResponseEntity.ok(service.process(params));
    }
}

