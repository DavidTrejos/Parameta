package com.parameta.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
@EnableWs
public class SoapConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext ctx) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(ctx);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "employee") // => /ws/employee.wsdl
    public DefaultWsdl11Definition wsdl(XsdSchema employeeSchema) {
        DefaultWsdl11Definition d = new DefaultWsdl11Definition();
        d.setPortTypeName("EmployeePort");
        d.setLocationUri("/ws");
        d.setTargetNamespace("http://parameta.com/employee");
        d.setSchema(employeeSchema);
        return d;
    }

    @Bean
    public XsdSchema employeeSchema() {
        return new SimpleXsdSchema(new ClassPathResource("soap/schema/employee.xsd"));
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller m = new Jaxb2Marshaller();
        m.setContextPath("com.parameta.soap.generated");
        return m;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        WebServiceTemplate t = new WebServiceTemplate();
        t.setMarshaller(marshaller);
        t.setUnmarshaller(marshaller);
        return t;
    }
}

