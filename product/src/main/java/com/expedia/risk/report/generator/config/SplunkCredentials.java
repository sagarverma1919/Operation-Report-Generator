package com.expedia.risk.report.generator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.splunk.SSLSecurityProtocol;
import com.splunk.ServiceArgs;

@Component
@PropertySource("file:/Users/${USER}/.splunk")
public class SplunkCredentials {


    @Value("${com.expedia.sea.user}")
    private String userName;

    @Value("${com.expedia.sea.password}")
    private String password;

    private String splunkHost = "splunklab6";


    public ServiceArgs getSplunkCredentials(){
        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setUsername(userName);
        serviceArgs.setPassword(password);
        serviceArgs.setHost(splunkHost);
        serviceArgs.setSSLSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        return serviceArgs;
    }

}
