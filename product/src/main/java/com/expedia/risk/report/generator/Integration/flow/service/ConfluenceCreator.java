package com.expedia.risk.report.generator.Integration.flow.service;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.expedia.risk.report.generator.Integration.flow.config.Body;
import com.expedia.risk.report.generator.Integration.flow.config.ConfluenceRequest;
import com.expedia.risk.report.generator.Integration.flow.config.SSLUtil;
import com.expedia.risk.report.generator.Integration.flow.config.Space;
import com.expedia.risk.report.generator.Integration.flow.config.Storage;
import com.expedia.risk.report.generator.Integration.flow.config.Version;

public class ConfluenceCreator
{
    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException
    {

        final String SERVER_URI = "https://confluence/rest/api/content/895067948";
        final String DOWNLOAD_SERVER_URI = "https://confluence/rest/api/content/894255080/child/attachment";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic ");
        headers.add("contentType","application/json");

        ConfluenceRequest request = createConfRequest();
        HttpEntity<ConfluenceRequest> entity = new HttpEntity<>(request, headers);

        SSLUtil.turnOffSslChecking();
        Object object = restTemplate.exchange(SERVER_URI, HttpMethod.PUT,entity, Object.class);
        System.out.println(object);

        //HttpEntity<Object> entity2 = new HttpEntity<>(headers);
        //SSLUtil.turnOffSslChecking();
        //ResponseEntity<Object> downloadAttachment = restTemplate.exchange(DOWNLOAD_SERVER_URI, HttpMethod.GET, entity2, Object.class);
        //System.out.println(downloadAttachment);
    }

    private static ConfluenceRequest createConfRequest()
    {
        ConfluenceRequest request = new ConfluenceRequest();
        Space space = new Space();
        space.setKey("~shgoyal");
        request.setSpace(space);
        request.setTitle("Test Page 3");
        request.setType("page");

        Storage storage = new Storage();
        storage.setRepresentation("storage");
        //storage.setValue("<p>test</p>");
        storage.setValue(ConfluenceUtility.createTableHeader("Test Page 2","List Service","OperationalMetrics"));
        Body body = new Body();
        body.setStorage(storage);
        request.setBody(body);

        request.setId("895067948");

        Version version = new Version();
        version.setNumber(5);
        request.setVersion(version);
        return request;
    }
}
