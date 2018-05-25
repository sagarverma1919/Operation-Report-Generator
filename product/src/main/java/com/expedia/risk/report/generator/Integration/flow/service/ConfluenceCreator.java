package com.expedia.risk.report.generator.Integration.flow.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.expedia.risk.report.generator.Integration.flow.config.Body;
import com.expedia.risk.report.generator.Integration.flow.config.ConfluenceRequest;
import com.expedia.risk.report.generator.Integration.flow.config.SSLUtil;
import com.expedia.risk.report.generator.Integration.flow.config.Space;
import com.expedia.risk.report.generator.Integration.flow.config.Storage;
import com.expedia.risk.report.generator.Integration.flow.config.Version;
import com.expedia.risk.report.generator.model.Field;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.model.Service;
import com.expedia.risk.report.generator.model.ServiceDetails;
import com.expedia.risk.report.generator.model.WeeklyDetails;

public class ConfluenceCreator
{
    public static void main(String[] args)
    {
        try
        {
            /*final String SERVER_URI = "https://confluence/rest/api/content/895067948";
            final String DOWNLOAD_SERVER_URI = "https://confluence/rest/api/content/894255080/child/attachment";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic ");
            headers.add("contentType", "application/json");

            ConfluenceRequest request = createConfRequest();
            HttpEntity<ConfluenceRequest> entity = new HttpEntity<>(request, headers);

            SSLUtil.turnOffSslChecking();
            Object object = restTemplate.exchange(SERVER_URI, HttpMethod.PUT, entity, Object.class);
            System.out.println(object);*/
            generateConfluencePage();

            //HttpEntity<Object> entity2 = new HttpEntity<>(headers);
            //SSLUtil.turnOffSslChecking();
            //ResponseEntity<Object> downloadAttachment = restTemplate.exchange(DOWNLOAD_SERVER_URI, HttpMethod.GET,
            // entity2, Object.class);

            //System.out.println(downloadAttachment);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        //  storage.setValue(ConfluenceUtility.createTableHeader("Test Page 2", "List Service", "OperationalMetrics"));
        Body body = new Body();
        body.setStorage(storage);
        request.setBody(body);

        request.setId("895067948");

        Version version = new Version();
        version.setNumber(5);
        request.setVersion(version);
        return request;
    }


    /**
     * Generate confluence page
     */
    private static void generateConfluencePage()
    {
        try
        {
            final String SERVER_URI = "https://confluence/rest/api/content";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic ");
            headers.add("contentType", "application/json");
            Report report = populateRequest();
            ConfluenceRequest request = renderConfluencePage(report);
            HttpEntity<ConfluenceRequest> entity = new HttpEntity<>(request, headers);
            SSLUtil.turnOffSslChecking();
            Object object = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, Object.class);
            System.out.println(object);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * render confluence page content
     */
    private static ConfluenceRequest renderConfluencePage(Report report)
    {

        ConfluenceRequest request = new ConfluenceRequest();
        Space space = new Space();
        space.setKey("~shgoyal");
        request.setSpace(space);
        request.setTitle(report.getReportName());
        request.setType("page");
        Storage storage = new Storage();
        storage.setRepresentation("storage");
        storage.setValue(ConfluenceUtility.renderPage(report));
        Body body = new Body();
        body.setStorage(storage);
        request.setBody(body);
        return request;
    }

    /**
     * populate Request
     */
    private static Report populateRequest()
    {
        {
            Report report = new Report();
            report.setEmailId("abc@expedia.com");
            report.setReportName("Ops Report");
            report.setNoOfWeeks(3);
            final ArrayList<Service> servicesList = new ArrayList<>(1);
            List<ServiceDetails> serviceDetailsList = new ArrayList<>(2);
            final ServiceDetails serviceDetails = new ServiceDetails();
            List<WeeklyDetails> weeklyDetailsList = new ArrayList<>(2);
            final WeeklyDetails weeklyDetails = new WeeklyDetails();
            weeklyDetails.setQuery(
                    "search index=fraud sourcetype=risk_listservice_perf ActivityName=endpoint* ActivityStep=rs "
                            + "(OperationName=lookup OR OperationName=addItems OR OperationName=items) | stats count "
                            + "as TransactionCount, exactperc99(Duration) as TP99, exactperc99.9(Duration) as TP99.9 "
                            + "by OperationName, RequestMethod");
            weeklyDetails.setExtraColumns("SLA", new int[]{1, 2, 3, 4});
            weeklyDetails.setExtraColumns("Comments", null);
            weeklyDetails.setTableName("Operational Metrics");
            final ArrayList<String> resultList = new ArrayList<>(4);
            resultList.add("addItems:POST:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
            resultList.add("items:POST:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
            resultList.add("items:GET:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
            resultList.add("lookup:POST:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
            weeklyDetails.setResults(resultList);
            final ArrayList<Field> fieldList = new ArrayList<>();
            Field field = new Field();
            field.setName("Operation Name");
            fieldList.add(field);
            field = new Field();
            field.setName("Method");
            fieldList.add(field);
            field = new Field();
            field.setName("Tp99");
            field.setPartitioned(true);
            fieldList.add(field);
            weeklyDetails.setFields(fieldList);
            weeklyDetailsList.add(weeklyDetails);
            serviceDetails.setWeeklyDetails(weeklyDetailsList);
            serviceDetailsList.add(serviceDetails);
            final Service service = new Service();
            service.setServiceName("ListService");
            service.setWeeklyDetails(weeklyDetailsList);
            //service.setServiceDetailsList(serviceDetailsList);
            servicesList.add(service);
            report.setServices(servicesList);
            return report;
        }

    }
}
