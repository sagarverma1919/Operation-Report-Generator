package com.expedia.risk.report.generator.Integration.flow;

import java.io.File;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.messaging.MessageHandler;

import com.expedia.risk.report.generator.Integration.flow.service.JsonToObjectMappingHandler;
import com.expedia.risk.report.generator.Integration.flow.service.SplunkReportGenerationHandler;

@Configuration
@IntegrationComponentScan(basePackages = "com.expedia.risk.report.generator")
public class SpringIntegrationFlow {

    @Bean
    public IntegrationFlow fileTransferFromS3ToLocalUploadFolder(
            @Qualifier("s3MessageSource")
                    MessageSource<File> s3MessageSource,
            MessageHandler localUploadFolderFileWritingMessageHandler
    ) {
        return IntegrationFlows.from(s3MessageSource, pollingMessageSourceUsing(300, 1))
                .handle(localUploadFolderFileWritingMessageHandler)
                .get();
    }

    @Bean
    public IntegrationFlow fileFromLocalUploadFolderReportGenerator(
            JsonToObjectMappingHandler jsonToObjectMappingHandler,
            @Qualifier("localUploadFolderMessageSource") MessageSource<File> localUploadFolderMessageSource,
            SplunkReportGenerationHandler splunkReportGenerationHandler
    ) {
        return IntegrationFlows.from(localUploadFolderMessageSource, pollingMessageSourceUsing(5000, 3))
                .handle(jsonToObjectMappingHandler)
                .handle(splunkReportGenerationHandler)
                //  .handle()
                .get();
    }
    /*
    @Bean
    public IntegrationFlow errorHandlingChannel() {
        return IntegrationFlows.from(MessageHeaders.ERROR_CHANNEL)
                .handle("") //Send mail to recipient
                .get();
    }*/


    private Consumer<SourcePollingChannelAdapterSpec> pollingMessageSourceUsing(
            long fixedDelayMillis, int maxMessagesPerPoll
    ) {
        return inboundChannelAdapter ->
                inboundChannelAdapter.poller(Pollers.fixedDelay(fixedDelayMillis)
                        .maxMessagesPerPoll(maxMessagesPerPoll));
    }
}
