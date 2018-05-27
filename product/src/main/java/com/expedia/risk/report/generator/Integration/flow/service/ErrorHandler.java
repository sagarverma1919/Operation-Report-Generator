package com.expedia.risk.report.generator.Integration.flow.service;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@MessageEndpoint
public class ErrorHandler {

    @ServiceActivator
    public String errorHandling(Message<Object> message)
    {
        return message.getPayload().toString();
    }
}
