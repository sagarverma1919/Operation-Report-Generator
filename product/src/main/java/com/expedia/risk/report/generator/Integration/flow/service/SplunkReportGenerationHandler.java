package com.expedia.risk.report.generator.Integration.flow.service;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@MessageEndpoint
public class SplunkReportGenerationHandler {

    @ServiceActivator
    public void reportGenerator(Message<Object> message) {
        System.out.println(message.getPayload() + "," + message.getHeaders());

        return;
    }
}
