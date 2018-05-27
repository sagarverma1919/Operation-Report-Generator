package com.expedia.risk.report.generator.Integration.flow.service;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;


@MessageEndpoint
public class ConfluencePageHandler {

    @ServiceActivator
    public String createConfluencePage(Message<Object> message)
    {

        return  null;
    }
}
