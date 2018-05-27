package com.expedia.risk.report.generator.Integration.flow.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.expedia.risk.report.generator.config.SplunkCredentials;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.splunk.SplunkReportGenerator;

@MessageEndpoint
public class SplunkReportGenerationHandler {

    @Autowired
    SplunkReportGenerator splunkReportGenerator;

    @Autowired
    SplunkCredentials splunkCredentials;

    @ServiceActivator
    public Report getStatisticalData(Message<Object> message) throws IOException {

        Report report = splunkReportGenerator.getUpdatedReportDetails((Report) message.getPayload(), splunkCredentials);
        return report;
    }


}
