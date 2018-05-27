/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator.splunk;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.expedia.risk.report.generator.config.SplunkCredentials;
import com.expedia.risk.report.generator.model.Field;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.model.Result;
import com.expedia.risk.report.generator.model.Service;
import com.expedia.risk.report.generator.model.SplunkResult;
import com.expedia.risk.report.generator.model.WeeklyDetails;
import com.expedia.risk.report.generator.util.DateUtil;
import com.expedia.risk.report.generator.util.NumberUtil;

@Component
public class SplunkReportGenerator {

    /*@Autowired*/
    private SplunkQueryExecutor splunkQueryExecutor = new SplunkQueryExecutor();

    private static final String ZONE_ID = "America/Los_Angeles";

    public Report getUpdatedReportDetails(final Report report, final SplunkCredentials splunkCredentials) throws
            IOException {
        if (null != report) {
            final List<Service> serviceList = report.getServices();
            if (!CollectionUtils.isEmpty(serviceList)) {
                for (final Service service : serviceList) {
                    final List<WeeklyDetails> weeklyDetailsList = service.getWeeklyDetails();
                    if (!CollectionUtils.isEmpty(weeklyDetailsList)) {
                        for (final WeeklyDetails weeklyDetails : weeklyDetailsList) {
                            final Map<Integer, SplunkResult> resultOnWeeklyBasis =
                                    getResultOnWeeklyBasis(report.getNoOfWeeks(), weeklyDetails.getQuery(),
                                            splunkCredentials);
                            /*populateFieldsAndResultInReport(weeklyDetails, resultOnWeeklyBasis, report.getNoOfWeeks());*/
                        }
                    }
                }
            }
        }
        return report;
    }

    private void populateFieldsAndResultInReport(final WeeklyDetails weeklyDetails,
                                                 final Map<Integer, SplunkResult> resultOnWeeklyBasis,
                                                 final int noOfWeeks) {
        final List<Field> fieldList = resultOnWeeklyBasis.get(0).getField();
        weeklyDetails.setFields(fieldList);
        List<String> manipulatedResult = new ArrayList<>();
        int counter = -1;
        for (int count = 0; count < noOfWeeks; count++) {
            for (final Field field : fieldList) {
                if (getValue(field.getName(), resultOnWeeklyBasis, noOfWeeks, manipulatedResult,
                        resultOnWeeklyBasis.get(count), counter)) {
                    field.setPartitioned(true);
                }
                counter++;
            }
            counter = 0;
        }
        weeklyDetails.setResults(manipulatedResult);
    }

    private boolean getValue(final String name, final Map<Integer, SplunkResult> resultOnWeeklyBasis, final int ofWeeks,
                             final List<String> manipulatedResult, final SplunkResult splunkResult, int counter) {
        /* final SplunkResult splunkResult = resultOnWeeklyBasis.get(0);*/
        final List<Map<String, String>> splunkResultResults = splunkResult.getResults();
        boolean isNumeric = false;
        int index = 0;
        for (final Map<String, String> resultMap : splunkResultResults) {
            final String value = resultMap.get(name);
            isNumeric = NumberUtil.isNumeric(value);
            try {
                String s = manipulatedResult.get(index);
                if (null == s || s.isEmpty()) {
                    s = value;
                    manipulatedResult.remove(index);
                    manipulatedResult.add(index, s);
                } else {
                    String[] split = s.split(":");
                    if (NumberUtil.isNumeric(split[counter])) {
                        try{
                        if(split[counter].split("#").length>=1){
                            split[counter] = split[counter]+"#"+value;
                            s = getValue(split);
                            manipulatedResult.remove(index);
                            manipulatedResult.add(index, s);
                        }}catch (Exception e){
                            s = s + ":" + value;
                            manipulatedResult.remove(index);
                            manipulatedResult.add(index, s);
                        }

                    } else if (!split[counter].equals(value)) {
                        s = s + ":" + value;
                        manipulatedResult.remove(index);
                        manipulatedResult.add(index, s);
                    }
                }


            } catch (Exception e) {
                manipulatedResult.add(value);
            }
            index++;
        }
        return isNumeric;
    }

    private String getValue(final String[] split) {
        String s = "";
        for (String string : split){
            s = s+ ":" +string;
        }
        return s;
    }

    private Map<Integer, SplunkResult> getResultOnWeeklyBasis(final int noOfWeeks, final String query,
                                                              final SplunkCredentials splunkCredentials)
            throws IOException {
        final DateTimeFormatter format =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime latestDate = LocalDateTime.now();
        Map<Integer, SplunkResult> splunkResults = new HashMap<>();
        final ZoneId newYokZoneId = ZoneId.of(ZONE_ID);
        for (int index = 0; index < noOfWeeks; index++) {
            final String earliestDate = DateUtil.getPreviousDate(latestDate, format, ZONE_ID);
            final String splunkResult = splunkQueryExecutor
                    .execute(splunkCredentials.getSplunkCredentials(), query, latestDate.format(format.withZone
                            (newYokZoneId)), earliestDate);
            splunkResults.put(index, splunkQueryExecutor.getParsedResult(splunkResult));
            latestDate = LocalDateTime.parse(earliestDate, format);
        }
        return splunkResults;
    }
}
