package com.parameta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeRequestParams(
        @NotBlank
        String nombres,
        @NotBlank
        String apellidos,
        @NotBlank
        String tipoDocumento,
        @NotBlank
        String numeroDocumento,
        @NotBlank
        String fechaNacimiento,
        @NotBlank
        String fechaVinculacion,
        @NotBlank
        String cargo,
        @NotNull
        Double salario
) {}
