package com.parameta.service;

import com.parameta.soap.generated.CreateEmployeeRequest;
import com.parameta.soap.generated.CreateEmployeeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

@Service
public class SoapEmployeeClient {

    @Value("${soap.employee.endpoint-uri}")
    private String endpointUri;

    private final WebServiceTemplate template;

    public SoapEmployeeClient(WebServiceTemplate template) {
        this.template = template;
    }

    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest req) {
        return (CreateEmployeeResponse) template.marshalSendAndReceive(endpointUri, req);
    }
}
