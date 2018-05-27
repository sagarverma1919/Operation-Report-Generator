package com.expedia.risk.report.generator.Integration.flow.service;

import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;

import com.expedia.risk.report.generator.config.SplunkCredentials;
import com.expedia.risk.report.generator.splunk.SplunkReportGenerator;

@MessageEndpoint
public class SplunkReportGenerationHandler {

    @Autowired
    private SplunkCredentials splunkCredentials;

    @Autowired
    private SplunkReportGenerator splunkReportGenerator;


}
