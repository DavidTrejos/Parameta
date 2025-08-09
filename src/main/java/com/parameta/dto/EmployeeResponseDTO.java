package com.parameta.dto;

public record EmployeeResponseDTO(
        String nombres,
        String apellidos,
        String tipoDocumento,
        String numeroDocumento,
        String fechaNacimiento,
        String fechaVinculacion,
        String cargo,
        Double salario,
        AgeDetail edadActual,
        TenureDetail tiempoVinculacion
) {}
