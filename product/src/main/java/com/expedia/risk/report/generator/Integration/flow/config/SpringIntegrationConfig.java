package com.expedia.risk.report.generator.Integration.flow.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizer;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizingMessageSource;
import org.springframework.integration.aws.support.filters.S3RegexPatternFileListFilter;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.IgnoreHiddenFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.amazonaws.services.s3.AmazonS3;


@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackages = "com.expedia.risk.report.generator")
public class SpringIntegrationConfig {


    @Value("${com.expedia.report.generator.local.upload.folder}")
    String localUploadFolder;


    @Bean("s3MessageSource")
    public MessageSource<File> s3MessageSource(
            S3InboundFileSynchronizer s3InboundFileSynchronizer,
            @Value("${com.expedia.report.generator.local.upload.sync.folder}")
                    String localUpload) {
        S3InboundFileSynchronizingMessageSource messageSource = new S3InboundFileSynchronizingMessageSource(s3InboundFileSynchronizer);
        messageSource.setLocalDirectory(new File(localUpload));
        messageSource.setAutoCreateLocalDirectory(true);
        messageSource.setLocalFilter(new IgnoreHiddenFileListFilter());
        return messageSource;
    }

    @Bean
    public S3InboundFileSynchronizer s3InboundFileSynchronizer(
            AmazonS3 s3Client,
            @Value("${com.expedia.report.generator.remoteFilePattern}")
                    String s3SynchronizerRemoteFilePattern,
            @Value("${com.expedia.report.generator.s3.bucket}")
                    String s3Bucket) {
        S3InboundFileSynchronizer s3InboundFileSynchronizer = new S3InboundFileSynchronizer(s3Client);
        s3InboundFileSynchronizer.setDeleteRemoteFiles(true);
        s3InboundFileSynchronizer.setFilter(new S3RegexPatternFileListFilter(s3SynchronizerRemoteFilePattern));

        s3InboundFileSynchronizer.setRemoteDirectory(s3Bucket);
        return s3InboundFileSynchronizer;
    }

    @Bean("localUploadFolderFileWritingMessageHandler")
    public MessageHandler localUploadFolderFileWritingMessageHandler() {
        return Files.outboundAdapter(new File(localUploadFolder))
                .autoCreateDirectory(true)
                .fileExistsMode(FileExistsMode.REPLACE)
                .deleteSourceFiles(true)
                .get();
    }

    @Bean("localUploadFolderMessageSource")
    public MessageSource<File> localUploadFolderMessageSource() {
        return Files.inboundAdapter(new File(localUploadFolder))
                .ignoreHidden(true)
                .nioLocker()
                .useWatchService(true)
                .scanEachPoll(false)
                .preventDuplicates(false)
                .get();
    }

    @Bean
    public MessageHandler messageHandler() {
        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getPayload() + "," + message.getHeaders());
                return;
            }
        };
        return messageHandler;
    }
}
