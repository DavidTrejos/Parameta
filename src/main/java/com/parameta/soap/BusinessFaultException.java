
package com.parameta.soap;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT)
public class BusinessFaultException extends RuntimeException {
    public BusinessFaultException(String message) { super(message); }
}
