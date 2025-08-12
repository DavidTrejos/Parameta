package com.parameta.soap;


import com.parameta.domain.Employee;
import com.parameta.repository.EmployeeRepository;
import com.parameta.soap.generated.CreateEmployeeRequest;
import com.parameta.soap.generated.CreateEmployeeResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;
import java.time.Period;

/**
 * Servidor SOAP, recibe el XML en /ws, valida y persistimos en DB
 */
@Endpoint
public class EmployeeEndpoint {

    private static final String NS = "http://parameta.com/employee";
    private final EmployeeRepository repository;

    public EmployeeEndpoint(EmployeeRepository repository) {
        this.repository = repository;
    }

    @PayloadRoot(namespace = NS, localPart = "CreateEmployeeRequest")
    @ResponsePayload
    public CreateEmployeeResponse create(@RequestPayload CreateEmployeeRequest req) {

        LocalDate nacimiento = LocalDate.parse(req.getFechaNacimiento());
        int years = Period.between(nacimiento, LocalDate.now()).getYears();
        if (years < 18) {
            throw new BusinessFaultException("El empleado debe ser mayor de edad (>= 18).");
        }

        if (repository.existsByNumeroDocumento(req.getNumeroDocumento())) {
            throw new BusinessFaultException("numeroDocumento ya registrado: " + req.getNumeroDocumento());
        }

        Employee e = new Employee();
        e.setNombres(req.getNombres());
        e.setApellidos(req.getApellidos());
        e.setTipoDocumento(req.getTipoDocumento());
        e.setNumeroDocumento(req.getNumeroDocumento());
        e.setFechaNacimiento(LocalDate.parse(req.getFechaNacimiento()));
        e.setFechaVinculacion(LocalDate.parse(req.getFechaVinculacion()));
        e.setCargo(req.getCargo());
        e.setSalario(req.getSalario());

        Employee saved = repository.save(e);

        CreateEmployeeResponse resp = new CreateEmployeeResponse();
        resp.setId(saved.getId());
        resp.setStatus("OK");
        return resp;
    }
}
