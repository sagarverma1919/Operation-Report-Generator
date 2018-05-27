package com.expedia.risk.report.generator.Integration.flow.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.expedia.risk.report.generator.config.SplunkCredentials;
import com.expedia.risk.report.generator.splunk.SplunkReportGenerator;

public class SplunkReportGenerationHandler {

    @Autowired
    private SplunkCredentials splunkCredentials;

    @Autowired
    private SplunkReportGenerator splunkReportGenerator;


}
