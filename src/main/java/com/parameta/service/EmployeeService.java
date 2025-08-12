package com.parameta.service;

import com.parameta.dto.AgeDetail;
import com.parameta.dto.EmployeeRequestParams;
import com.parameta.dto.EmployeeResponseDTO;
import com.parameta.dto.TenureDetail;
import com.parameta.repository.EmployeeRepository;
import com.parameta.soap.generated.CreateEmployeeRequest;
import com.parameta.soap.generated.CreateEmployeeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class EmployeeService {

    //Format estipulado de fecha.
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;

    private final SoapEmployeeClient soapClient;
    private final EmployeeRepository repository;

    public EmployeeService(SoapEmployeeClient soapClient, EmployeeRepository repository) {
        this.soapClient = soapClient;
        this.repository = repository;
    }

    /**
     * Método principal del caso de uso. Donde se orquesta el flujo completo. Mapeo del DTO Rest a la clase SOAP.
     * @param req
     * @return
     */
    public EmployeeResponseDTO process(EmployeeRequestParams req) {
        LocalDate nacimiento = parseDate(req.fechaNacimiento(), "fechaNacimiento");
        LocalDate vinculacion = parseDate(req.fechaVinculacion(), "fechaVinculacion");

        if (Period.between(nacimiento, LocalDate.now()).getYears() < 18) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado debe ser mayor de edad.");
        }

        if (repository.existsByNumeroDocumento(req.numeroDocumento())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Documento ya registrado.");
        }

        CreateEmployeeRequest soapReq = new CreateEmployeeRequest();
        soapReq.setNombres(req.nombres());
        soapReq.setApellidos(req.apellidos());
        soapReq.setTipoDocumento(req.tipoDocumento());
        soapReq.setNumeroDocumento(req.numeroDocumento());
        soapReq.setFechaNacimiento(req.fechaNacimiento());
        soapReq.setFechaVinculacion(req.fechaVinculacion());
        soapReq.setCargo(req.cargo());
        soapReq.setSalario(req.salario());

        CreateEmployeeResponse soapResp = soapClient.createEmployee(soapReq);
        if (soapResp == null || soapResp.getStatus() == null || !"OK".equalsIgnoreCase(soapResp.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Fallo al persistir vía SOAP");
        }

        AgeDetail edad = toAgeDetail(nacimiento, LocalDate.now());
        TenureDetail tenure = toTenureDetail(vinculacion, LocalDate.now());


        return new EmployeeResponseDTO(
                req.nombres(), req.apellidos(), req.tipoDocumento(), req.numeroDocumento(),
                req.fechaNacimiento(), req.fechaVinculacion(), req.cargo(), req.salario(),
                edad, tenure
        );
    }

    private LocalDate parseDate(String s, String field) {
        try {
            return LocalDate.parse(s, ISO);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Formato de " + field + " inválido. Use yyyy-MM-dd");
        }
    }

    private AgeDetail toAgeDetail(LocalDate from, LocalDate to) {
        Period p = Period.between(from, to);
        return new AgeDetail(p.getYears(), p.getMonths(), p.getDays());
    }

    private TenureDetail toTenureDetail(LocalDate from, LocalDate to) {
        Period p = Period.between(from, to);
        return new TenureDetail(p.getYears(), p.getMonths(), p.getDays());
    }
}
