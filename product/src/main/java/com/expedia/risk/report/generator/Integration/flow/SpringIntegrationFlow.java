package com.expedia.risk.report.generator.Integration.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageHeaders;

@Configuration
public class SpringIntegrationFlow {

    /*@Bean
    public IntegrationFlow fileWriteToLocalUploadFolder() {
        return IntegrationFlows.from()
                .handle()
                .get();
    }

    @Bean
    public IntegrationFlow fileFromLocalUploadFolderReportGenerator() {
        return IntegrationFlows.from()
                .enrichHeaders() // Add the recipient email address
                .handle("") //Call Splunk
                .handle
                .get();
    }

    @Bean
    public IntegrationFlow errorHandlingChannel() {
        return IntegrationFlows.from(MessageHeaders.ERROR_CHANNEL)
                .handle("") //Send mail to recipient
                .get();
    }*/
}
