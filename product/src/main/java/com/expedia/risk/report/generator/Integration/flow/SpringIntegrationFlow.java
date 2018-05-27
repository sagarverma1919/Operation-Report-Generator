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
import org.springframework.messaging.MessageHeaders;

import com.expedia.risk.report.generator.Integration.flow.service.ConfluencePageHandler;
import com.expedia.risk.report.generator.Integration.flow.service.JsonToObjectMappingHandler;
import com.expedia.risk.report.generator.Integration.flow.service.SplunkReportGenerationHandler;
import com.expedia.risk.report.generator.Integration.flow.service.ValidationHandler;

@Configuration
@IntegrationComponentScan(basePackages = "com.expedia.risk.report.generator")
public class SpringIntegrationFlow {

    @Bean
    public IntegrationFlow fileTransferFromS3ToLocalUploadFolder(
            @Qualifier("s3MessageSource")
                    MessageSource<File> s3MessageSource,
            MessageHandler localUploadFolderFileWritingMessageHandler,
            ValidationHandler validationHandler,
            JsonToObjectMappingHandler jsonToObjectMappingHandler
    ) {
        return IntegrationFlows.from(s3MessageSource, pollingMessageSourceUsing(300, 1))
                .handle(jsonToObjectMappingHandler)
                .handle(validationHandler)
                .handle(localUploadFolderFileWritingMessageHandler)
                .get();
    }

    @Bean
    public IntegrationFlow fileFromLocalUploadFolderReportGenerator(
            JsonToObjectMappingHandler jsonToObjectMappingHandler,
            @Qualifier("localUploadFolderMessageSource") MessageSource<File> localUploadFolderMessageSource,
            SplunkReportGenerationHandler splunkReportGenerationHandler,
            ConfluencePageHandler confluencePageHandler,
            MessageHandler snsMessageHandler
    ) {
        return IntegrationFlows.from(localUploadFolderMessageSource, pollingMessageSourceUsing(5000, 3))
                .handle(jsonToObjectMappingHandler)
                .handle(splunkReportGenerationHandler)
                .handle(confluencePageHandler)
                .handle(snsMessageHandler)
                .get();
    }


    @Bean
    public IntegrationFlow errorHandlingChannel(
            MessageHandler snsMessageHandler
    ) {
        return IntegrationFlows.from(MessageHeaders.ERROR_CHANNEL)
                .handle(snsMessageHandler)
                .get();
    }


    private Consumer<SourcePollingChannelAdapterSpec> pollingMessageSourceUsing(
            long fixedDelayMillis, int maxMessagesPerPoll
    ) {
        return inboundChannelAdapter ->
                inboundChannelAdapter.poller(Pollers.fixedDelay(fixedDelayMillis)
                        .maxMessagesPerPoll(maxMessagesPerPoll));
    }
}
