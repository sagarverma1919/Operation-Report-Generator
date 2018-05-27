package com.expedia.risk.report.generator.Integration.flow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import com.expedia.risk.report.generator.config.SplunkCredentials;
import com.expedia.risk.report.generator.confluence.ConfluenceCreator;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.splunk.SplunkReportGenerator;

@MessageEndpoint
public class SplunkReportGenerationHandler {

    @ServiceActivator
    public Report getStatisticalData(Message<Object> message)
    {
         Report report=ConfluenceCreator.populateRequestWith3Weeks();
         return report;
    }


}
