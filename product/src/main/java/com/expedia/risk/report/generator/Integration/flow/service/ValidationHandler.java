package com.expedia.risk.report.generator.Integration.flow.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.expedia.risk.report.generator.config.SplunkCredentials;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.splunk.SplunkReportGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@MessageEndpoint
public class ValidationHandler {

    @Autowired
    SplunkReportGenerator splunkReportGenerator;

    @Autowired
    SplunkCredentials splunkCredentials;

    @Value("${com.expedia.report.generator.temp.location}")
    private String tempDirectory;

    @ServiceActivator
    public File performValidation(Message<Object> message) throws IOException {
        Report report = splunkReportGenerator.getUpdatedReportDetails((Report) message.getPayload(), splunkCredentials);
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(tempDirectory);
        mapper.writeValue(file,report);
        return file;
    }
}
