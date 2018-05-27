package com.expedia.risk.report.generator.Integration.flow.service;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.amazonaws.util.StringUtils;
import com.expedia.risk.report.generator.confluence.ConfluenceCreator;
import com.expedia.risk.report.generator.model.Report;


@MessageEndpoint
public class ConfluencePageHandler {

    @ServiceActivator
    public String createConfluencePage(Message<Object> message) {
        Report report = (Report) message.getPayload();
        String generatedConfluenceLink = new ConfluenceCreator().generateConfluencePage(report);
        if (StringUtils.isNullOrEmpty(generatedConfluenceLink)) {
            generatedConfluenceLink = "Error while generating confluence report";
        }
        return generatedConfluenceLink;
    }
}
