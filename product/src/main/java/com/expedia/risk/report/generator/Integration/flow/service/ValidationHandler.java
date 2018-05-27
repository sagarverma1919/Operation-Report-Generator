package com.expedia.risk.report.generator.Integration.flow.service;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.expedia.risk.report.generator.model.Report;

@MessageEndpoint
public class ValidationHandler {

    @ServiceActivator
    public void performValidation(Message<Object> message)
    {
        //Report report = (Report) message.getPayload();
    }
}
