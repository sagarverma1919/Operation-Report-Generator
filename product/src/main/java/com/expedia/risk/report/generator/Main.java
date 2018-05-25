/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator;

import java.util.ArrayList;
import java.util.List;

import com.expedia.risk.report.generator.model.Field;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.model.Service;
import com.expedia.risk.report.generator.model.ServiceDetails;
import com.expedia.risk.report.generator.model.WeeklyDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Main {

    public static void main(String[] args) {
        Report report = new Report();
        report.setEmailId("abc@expedia.com");
        report.setReportName("Ops Report");
        report.setNoOfWeeks(3);
        final ArrayList<Service> servicesList = new ArrayList<>(1);
        List<ServiceDetails> serviceDetailsList = new ArrayList<>(2);
        final ServiceDetails serviceDetails = new ServiceDetails();
       // serviceDetails.setServiceName("ListService");
        List<WeeklyDetails> weeklyDetailsList = new ArrayList<>(2);
        final WeeklyDetails weeklyDetails = new WeeklyDetails();
        weeklyDetails.setQuery(
                "search index=fraud sourcetype=risk_listservice_perf ActivityName=endpoint* ActivityStep=rs (OperationName=lookup OR OperationName=addItems OR OperationName=items) | stats count as TransactionCount, exactperc99(Duration) as TP99, exactperc99.9(Duration) as TP99.9 by OperationName, RequestMethod");
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
        final Field field = new Field();
        field.setName("Operation Name");
        fieldList.add(field);
        weeklyDetails.setFields(fieldList);
        weeklyDetailsList.add(weeklyDetails);
        serviceDetails.setWeeklyDetails(weeklyDetailsList);
        //serviceDetailsList.add(serviceDetails);
        final Service service = new Service();
        service.setServiceName("ListService");
        //service.setServiceDetailsList(serviceDetailsList);
        service.setWeeklyDetails(weeklyDetailsList);
        servicesList.add(service);
        report.setServices(servicesList);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(report);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
