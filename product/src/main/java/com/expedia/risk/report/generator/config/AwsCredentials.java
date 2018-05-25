package com.expedia.risk.report.generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsCredentials {

    @Bean
    public AWSCredentialsProvider awsCredential(
            @Value("${com.expedia.report.generator.iam.user.profile}") String profile

    ) {
        return new ProfileCredentialsProvider(profile);
    }

    @Bean
    public AmazonS3 s3Client(
            AWSCredentialsProvider awsCredential,
            @Value("${com.expedia.report.generator.s3.bucket.connectionTimeoutMillis}") int connectionTimeoutMillis,
            @Value("${com.expedia.report.generator.s3.bucket.maxErrorRetry}") int maxErrorRetry,
            @Value("${com.expedia.report.generator.aws.region}") String region
    ) {
        ClientConfiguration clientConfiguration = new ClientConfiguration()
                .withConnectionTimeout(connectionTimeoutMillis)
                .withMaxErrorRetry(maxErrorRetry);

        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(awsCredential)
                .build();
    }
}
