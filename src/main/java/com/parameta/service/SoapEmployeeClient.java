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

    /**
     * Recibe CreateEmployeeRequest y con marshaller lo convierte a XML, lo envia e endpointUri
     *  recibe el XML de respuesta y lo convierte de vuelta en CreateEmployeeResponse
     * @param req
     * @return
     */
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest req) {
        return (CreateEmployeeResponse) template.marshalSendAndReceive(endpointUri, req);
    }
}
