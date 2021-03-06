package com.expedia.risk.report.generator.confluence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.expedia.risk.report.generator.model.Body;
import com.expedia.risk.report.generator.model.ConfluenceRequest;
import com.expedia.risk.report.generator.model.Field;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.model.Service;
import com.expedia.risk.report.generator.model.Space;
import com.expedia.risk.report.generator.model.Storage;
import com.expedia.risk.report.generator.model.WeeklyDetails;
import com.expedia.risk.report.generator.util.DateUtil;
import com.expedia.risk.report.generator.util.SSLUtil;

public class ConfluenceCreator
{
   /* public static void main(String[] args)
    {
        try
        {
            Report report = populateRequestWith2Weeks();
            String link = generateConfluencePage(report);
            System.out.print(link);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/


    /**
     * Generate confluence page
     */
    public  String generateConfluencePage(Report report)
    {
        try
        {
            final String SERVER_URI = "https://confluence/rest/api/content";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic Z2F1amFpbjpwcmFzbmF0aDEhMTIzNDU2Nw==");
            headers.add("contentType", "application/json");

            final DateTimeFormatter format =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime latestDate = LocalDateTime.now();
            String previousDate = DateUtil.getPreviousDate(latestDate, format, "Asia/Kolkata", 7);
            String currentDate = DateUtil.getPreviousDate(latestDate, format, "Asia/Kolkata", 1);
            report.setReportName(String.format(report.getReportName(), previousDate, currentDate));
            ConfluenceRequest request = renderConfluencePage(report);
            HttpEntity<ConfluenceRequest> entity = new HttpEntity<>(request, headers);
            SSLUtil.turnOffSslChecking();
            Object object = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, Object.class);
            System.out.println(object);
            String generatedConfluenceLink =
                    "https://confluence/display/" + request.getSpace().getKey() + "/" + report.getReportName();
            return generatedConfluenceLink;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
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
    public static Report populateRequestWith3Weeks()
    {
        {
            Report report = new Report();
            report.setEmailId("abc@expedia.com");
            report.setReportName("Fraud Ops Report %s - %s_Mapper");
            report.setNoOfWeeks(3);
            ArrayList<Service> servicesList = new ArrayList<>(1);
            List<WeeklyDetails> weeklyDetailsList = new ArrayList<>(2);

            //*********************************
            Service service = new Service();
            service.setServiceName("ListService");

            WeeklyDetails weeklyDetails = new WeeklyDetails();
            weeklyDetails.setQuery(
                    "index=fraud sourcetype=risk_listservice_perf ActivityName=endpoint* ActivityStep=rs "
                            + "(OperationName=lookup OR OperationName=addItems OR OperationName=items) | stats count,"
                            + " exactperc99(Duration), exactperc99.9(Duration) by OperationName, RequestMethod");
            weeklyDetails.setExtraColumns("SLA in ms", new String[]{"250", "5,000", "1,000", "1,000"});
            weeklyDetails.setExtraColumns("Comments", null);
            weeklyDetails.setTableName("Operational Metrics");
            ArrayList<String> resultList = new ArrayList<>(4);
            resultList.add("lookup:62,613,025#62,047,872#65,874,552:5#4#4:179#171#144:250: ");
            resultList.add("items (fetch all items in a list):305#411#217:759#1,465#1,563:1,258#2,455#5,225:5000: ");
            resultList.add("items (single item add):370#384#235:59#42#54:74#119#62:1,000: ");
            resultList.add("addItems (multiple items add):12,885#14,966#11,975:264#330#141:472#520#375:1,000: ");
            weeklyDetails.setResults(resultList);
            ArrayList<Field> fieldList = new ArrayList<>();
            Field field = new Field();
            field.setName("Operation Name");
            fieldList.add(field);
            field = new Field();
            field.setName("Total Transactions Count");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99 in ms");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99.9 in ms");
            field.setPartitioned(true);
            fieldList.add(field);
            weeklyDetails.setFields(fieldList);
            weeklyDetailsList.add(weeklyDetails);


            weeklyDetails = new WeeklyDetails();
            weeklyDetails.setQuery(
                    "index=fraud sourcetype=risk_listservice_perf ActivityStep=rs ActivityName=endpoint* "
                            + "OperationName=lookup| stats count by Client_ID, OperationName, RequestMethod");
            weeklyDetails.setExtraColumns("Comments", null);
            weeklyDetails.setTableName("Lookup Requests per Client ");
            resultList = new ArrayList<>(4);
            resultList.add("FraudWS:7#8#7: ");
            resultList.add("VCS:51,130,424#50,893,516#54,285,611: ");
            resultList.add("VOYAGER:10,937,761#10,734,479#11,213,348: ");
            resultList.add("Zeus:544,833#541,502#544,788: ");
            weeklyDetails.setResults(resultList);
            fieldList = new ArrayList<>();
            field = new Field();
            field.setName("Client name");
            fieldList.add(field);
            field = new Field();
            field.setName("Total Transactions Count");
            field.setPartitioned(true);
            fieldList.add(field);
            weeklyDetails.setFields(fieldList);
            weeklyDetailsList.add(weeklyDetails);

            service.setWeeklyDetails(weeklyDetailsList);
            servicesList.add(service);


            //*******************************************
            service = new Service();
            weeklyDetailsList = new ArrayList<>(2);
            service.setServiceName("Fraud Web Service");
            weeklyDetails = new WeeklyDetails();
            weeklyDetails.setQuery(
                    "index=fraud sourcetype=\"fraudws_perf\"  ActivityName=\"endpoint*\" ActivityStep=rs "
                            + "(OperationName=sladetails OR OperationName=cancelOrder OR OperationName=release) | "
                            + "stats count, exactperc99(Duration), exactperc99.9(Duration) by OperationName");
            weeklyDetails.setExtraColumns("SLA", new String[]{"400", "90,000", "90,000"});
            weeklyDetails.setExtraColumns("Comments", null);
            weeklyDetails.setTableName("Operational Metrics ");
            resultList = new ArrayList<>(4);
            resultList.add("sladetails:6,298,551#6,433,023#6,433,023:14#19#13:42#60#41:400: ");
            resultList.add("cancelOrder:6,298,551#6,433,023#6,433,023:14#19#13:42#60#41:90,000: ");
            resultList.add("release:6,298,551#6,433,023#6,433,023:14#19#13:42#60#41:90,000: ");
            weeklyDetails.setResults(resultList);
            fieldList = new ArrayList<>();
            field = new Field();
            field.setName("API");
            fieldList.add(field);
            field = new Field();
            field.setName("Request Count");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99.9");
            field.setPartitioned(true);
            fieldList.add(field);
            weeklyDetails.setFields(fieldList);
            weeklyDetailsList.add(weeklyDetails);
            service.setWeeklyDetails(weeklyDetailsList);
            servicesList.add(service);

            //***************
            service = new Service();
            service.setServiceName("IRIS");
            servicesList.add(service);

            report.setServices(servicesList);
            return report;
        }

    }


    public static Report populateRequestWith2Weeks()
    {
        {
            Report report = new Report();
            report.setEmailId("abc@expedia.com");
            report.setReportName("Fraud Ops Report %s - %s_Mapper_12");
            report.setNoOfWeeks(2);
            ArrayList<Service> servicesList = new ArrayList<>(1);
            List<WeeklyDetails> weeklyDetailsList = new ArrayList<>(2);

            //*********************************
            Service service = new Service();
            service.setServiceName("ListService");

            WeeklyDetails weeklyDetails = new WeeklyDetails();
            weeklyDetails.setQuery("index=fraud sourcetype=risk_listservice_perf ActivityName=endpoint* "
                    + "ActivityStep=rs (OperationName=lookup OR OperationName=addItems OR OperationName=items) | "
                    + "stats count, exactperc99(Duration), exactperc99.9(Duration) by OperationName, RequestMethod");
            weeklyDetails.setExtraColumns("SLA in ms", new String[]{"250", "5,000", "1,000", "1,000"});
            weeklyDetails.setExtraColumns("Comments", null);
            weeklyDetails.setTableName("Operational Metrics");
            ArrayList<String> resultList = new ArrayList<>(4);
            resultList.add("lookup:62613025#62047872:5#4:179#171:250: ");
            resultList.add("items (fetch all items in a list):305#411:759#1465:1258#2455:5000: ");
            resultList.add("items (single item add):370#384:59#42:74#119:1000: ");
            resultList.add("addItems (multiple items add):12885#14966:264#330:472#520:1000: ");
            weeklyDetails.setResults(resultList);
            ArrayList<Field> fieldList = new ArrayList<>();
            Field field = new Field();
            field.setName("Operation Name");
            fieldList.add(field);
            field = new Field();
            field.setName("Total Transactions Count");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99 in ms");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99.9 in ms");
            field.setPartitioned(true);
            fieldList.add(field);
            weeklyDetails.setFields(fieldList);
            weeklyDetailsList.add(weeklyDetails);


            weeklyDetails = new WeeklyDetails();
            weeklyDetails.setQuery(
                    "index=fraud sourcetype=risk_listservice_perf ActivityStep=rs ActivityName=endpoint* "
                            + "OperationName=lookup| stats count by Client_ID, OperationName, RequestMethod");
            weeklyDetails.setExtraColumns("Comments", null);
            weeklyDetails.setTableName("Lookup Requests per Client ");
            resultList = new ArrayList<>(4);
            resultList.add("FraudWS:7#8: ");
            resultList.add("VCS:51130424#50893516: ");
            resultList.add("VOYAGER:10937761#10734479: ");
            resultList.add("Zeus:544833#541502: ");
            weeklyDetails.setResults(resultList);
            fieldList = new ArrayList<>();
            field = new Field();
            field.setName("Client name");
            fieldList.add(field);
            field = new Field();
            field.setName("Total Transactions Count");
            field.setPartitioned(true);
            fieldList.add(field);
            weeklyDetails.setFields(fieldList);
            weeklyDetailsList.add(weeklyDetails);

            service.setWeeklyDetails(weeklyDetailsList);
            servicesList.add(service);


            //*******************************************
            service = new Service();
            weeklyDetailsList = new ArrayList<>(2);
            service.setServiceName("Fraud Web Service");
            weeklyDetails = new WeeklyDetails();
            weeklyDetails.setQuery(
                    "index=fraud sourcetype=\"fraudws_perf\"  ActivityName=\"endpoint*\" ActivityStep=rs "
                            + "(OperationName=sladetails OR OperationName=cancelOrder OR OperationName=release) | "
                            + "stats count, exactperc99(Duration), exactperc99.9(Duration) by OperationName");
            weeklyDetails.setExtraColumns("SLA", new String[]{"400", "90000", "90000"});
            weeklyDetails.setExtraColumns("Comments", null);
            weeklyDetails.setTableName("Operational Metrics ");
            resultList = new ArrayList<>(4);
            resultList.add("sladetails:6298551#6433023:14#19:42#60:400: ");
            resultList.add("cancelOrder:6298551#6433023:14#19:42#60:90000: ");
            resultList.add("release:6298551#6433023:14#19:42#60:90000: ");
            weeklyDetails.setResults(resultList);
            fieldList = new ArrayList<>();
            field = new Field();
            field.setName("API");
            fieldList.add(field);
            field = new Field();
            field.setName("Request Count");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99");
            field.setPartitioned(true);
            fieldList.add(field);
            field = new Field();
            field.setName("TP99.9");
            field.setPartitioned(true);
            fieldList.add(field);
            weeklyDetails.setFields(fieldList);
            weeklyDetailsList.add(weeklyDetails);
            service.setWeeklyDetails(weeklyDetailsList);
            servicesList.add(service);

            //***************
            service = new Service();
            service.setServiceName("IRIS");
            servicesList.add(service);

            report.setServices(servicesList);
            return report;
        }

    }
}
