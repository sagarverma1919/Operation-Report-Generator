package com.expedia.risk.report.generator.Integration.flow.service;

import java.io.File;
import java.io.IOException;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.expedia.risk.report.generator.model.Report;
import com.fasterxml.jackson.databind.ObjectMapper;

@MessageEndpoint
public class JsonToObjectMappingHandler {

    @ServiceActivator
    public String jsonToObjectMapping(Message<Object> messsage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = (File) messsage.getPayload();
        return "Hello World";
        //Report report = mapper.readValue(file, Report.class);
        //return report;
    }


}
