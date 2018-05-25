package com.expedia.risk.report.generator.Integration.flow.config;

import java.io.File;

import javax.validation.executable.ValidateOnExecution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizer;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizingMessageSource;
import org.springframework.integration.aws.support.filters.S3RegexPatternFileListFilter;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.IgnoreHiddenFileListFilter;

import com.amazonaws.services.s3.AmazonS3;


@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackages = "com.expedia.risk.report.generator" )
public class SpringIntegrationConfig {


    @Bean("s3MessageSource")
    public MessageSource<File> s3MessageSource(
            S3InboundFileSynchronizer s3InboundFileSynchronizer,
            @Value("${com.expedia.report.generator.local.upload.folder}")
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
                    String s3Bucket){
        S3InboundFileSynchronizer s3InboundFileSynchronizer = new S3InboundFileSynchronizer(s3Client);
        s3InboundFileSynchronizer.setDeleteRemoteFiles(true);
        s3InboundFileSynchronizer.setFilter(new S3RegexPatternFileListFilter(s3SynchronizerRemoteFilePattern));

        s3InboundFileSynchronizer.setRemoteDirectory(s3Bucket);
        s3InboundFileSynchronizer.setPreserveTimestamp(true);
        return s3InboundFileSynchronizer;
    }
}
