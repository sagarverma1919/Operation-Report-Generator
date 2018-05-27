/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator;

import java.io.File;
import java.io.IOException;

import com.expedia.risk.report.generator.config.SplunkCredentials;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.splunk.SplunkReportGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Main {

    public static void main(String[] args) {
        /*Report report = new Report();
        report.setEmailId("abc@expedia.com");
        report.setReportName("Ops Report");
        report.setNoOfWeeks(3);
        final ArrayList<Service> servicesList = new ArrayList<>(1);
        List<ServiceDetails> serviceDetailsList = new ArrayList<>(2);
        final ServiceDetails serviceDetails = new ServiceDetails();
        // serviceDetails.setServiceName("ListService");
        List<WeeklyDetails> weeklyDetailsList = new ArrayList<>(2);
        final WeeklyDetails weeklyDetails = new WeeklyDetails();
        final WeeklyDetails weeklyDetails2 = new WeeklyDetails();

        final ArrayList<String> resultList = new ArrayList<>(4);
        resultList.add("addItems:POST:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
        resultList.add("items:POST:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
        resultList.add("items:GET:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
        resultList.add("lookup:POST:4,2,3:33.4,33.4,23.1:33.4,33.4,23.1:2:");
        final ArrayList<Field> fieldList = new ArrayList<>();
        final Field field = new Field();
        field.setName("Operation Name");
        fieldList.add(field);
        weeklyDetails.setQuery(
                "search index=fraud sourcetype=risk_listservice_perf ActivityName=endpoint* ActivityStep=rs (OperationName=lookup OR OperationName=addItems OR OperationName=items) | stats count as TransactionCount, exactperc99(Duration) as TP99, exactperc99.9(Duration) as TP99.9 by OperationName, RequestMethod");
        weeklyDetails.setExtraColumns("SLA", new int[]{1, 2, 3, 4});
        weeklyDetails.setExtraColumns("Comments", null);
        weeklyDetails.setTableName("Lookup per client");
        weeklyDetails.setResults(resultList);
        weeklyDetails.setFields(fieldList);
        weeklyDetails2.setQuery(
                "search index=fraud sourcetype=risk_listservice_perf ActivityName=endpoint* ActivityStep=rs (OperationName=lookup OR OperationName=addItems OR OperationName=items) | stats count as TransactionCount, exactperc99(Duration) as TP99, exactperc99.9(Duration) as TP99.9 by OperationName, RequestMethod");
        weeklyDetails2.setExtraColumns("SLA", new int[]{1, 2, 3, 4});
        weeklyDetails2.setExtraColumns("Comments", null);
        weeklyDetails2.setTableName("Operational Metrics");
        weeklyDetails2.setResults(resultList);
        weeklyDetails2.setFields(fieldList);
        weeklyDetailsList.add(weeklyDetails);
        weeklyDetailsList.add(weeklyDetails2);
        serviceDetails.setWeeklyDetails(weeklyDetailsList);
        //serviceDetailsList.add(serviceDetails);
        final Service service = new Service();
        service.setServiceName("ListService");
        final Service service2 = new Service();
        service2.setServiceName("FWS");
        //service.setServiceDetailsList(serviceDetailsList);
        service.setWeeklyDetails(weeklyDetailsList);
        servicesList.add(service);
        servicesList.add(service2);
        report.setServices(servicesList);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(report);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        DateTimeFormatter format =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = now.minusDays(7);
        ZoneId newYokZoneId = ZoneId.of("America/Los_Angeles");
        System.out.println("TimeZone : " + newYokZoneId);

        System.out.println(String.format(
                "Now:  %s\nThen: %s",
                now.format(format.withZone(newYokZoneId)),
                then.format(format.withZone(newYokZoneId))
        ));

        System.out.println("================");
        SplunkResult splunkResult = new SplunkResult();
        final ArrayList<Field> field1 = new ArrayList<>();
        final Field field2 = new Field();
        field2.setName("TP99");
        field1.add(field2);
        field1.add(field);
        splunkResult.setField(field1);
        final ArrayList<Result> results = new ArrayList<>();
        final Result result = new Result();
       *//* result.addValue("OperationName", "addItems");
        result.addValue("TP99", "668");*//*
        results.add(result);
       *//* splunkResult.setResults(results);*//*
        try {
            String json = ow.writeValueAsString(splunkResult);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/


        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        SplunkReportGenerator splunkReportGenerator = new SplunkReportGenerator();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Report splunkResult1 = mapper.readValue(new File("/Users/jasmkaur/Desktop/input.json"), Report
                    .class);

            final Report updatedReportDetails =
                    splunkReportGenerator.getUpdatedReportDetails(splunkResult1, new SplunkCredentials());

            System.out.println("=================Final Result==================================");
            String json = ow.writeValueAsString(updatedReportDetails);
            System.out.println(json);

        } catch (IOException e) {
                    e.printStackTrace();
        }

    }
}
