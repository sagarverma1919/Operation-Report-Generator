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
import com.expedia.risk.report.generator.model.Service;
import com.expedia.risk.report.generator.model.SplunkResult;
import com.expedia.risk.report.generator.model.WeeklyDetails;
import com.expedia.risk.report.generator.util.CommonUtil;
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
                            populateFieldsAndResultInReport(weeklyDetails, resultOnWeeklyBasis, report.getNoOfWeeks());
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
        Map<String, Integer> fieldMap = new HashMap<>(fieldList.size());
        int index = 0;
        for (final Field field : fieldList) {
            fieldMap.put(field.getName(), index++);
        }
        List<Map<String, String>> result = new ArrayList<>();
        for (int count = 0; count < noOfWeeks; count++) {
            final SplunkResult splunkResult = resultOnWeeklyBasis.get(count);
            final List<Map<String, String>> results = splunkResult.getResults();
            Map<String, String> weeklyResultMap = new HashMap<>();
            for (Map<String, String> dataPerWeek : results) {
                String key = "";
                String value = "";
                for (Map.Entry<String, String> entry : dataPerWeek.entrySet()) {
                    final int position = fieldMap.get(entry.getKey());
                    final String entryValue = entry.getValue();
                    if (NumberUtil.isNumeric(entryValue)) {
                        if (value.isEmpty()) {
                            value = value + position + "_" + entryValue;
                        } else {
                            value = value + "," + position + "_" + entryValue;
                        }
                    } else {
                        if (key.isEmpty()) {
                            key = key + position + "_" + entryValue;
                        } else {
                            key = key + "," + position + "_" + entryValue;
                        }
                    }
                }
                weeklyResultMap.put(key, value);
            }
            result.add(weeklyResultMap);
        }
        index = 0;
        Map<String, String> mergedDetailsMap = new HashMap<>();
        for (Map<String, String> stringMap : result) {
            System.out.println("week: " + index++);
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                System.out.println(entry.getKey() + "  ===  " + entry.getValue());
                mergedDetailsMap.put(entry.getKey(), "");
            }
        }
        Map<String, String> mergeMap = new HashMap<>();
        Map<String, String> newVals = new HashMap<>();
        for (Map.Entry<String, String> entryVal : mergedDetailsMap.entrySet()) {
            for (int i = 0; i < noOfWeeks; i++) {
                final Map<String, String> weekData = result.get(i);
                if (entryVal.getValue().isEmpty()) {
                    if (weekData.get(entryVal.getKey()) == null) {
                        mergedDetailsMap.put(entryVal.getKey(), String.valueOf(i++));
                    } else {
                        mergedDetailsMap.put(entryVal.getKey(), weekData.get(entryVal.getKey()));
                    }
                } else {
                    if (weekData.get(entryVal.getKey()) == null) {
                        final String[] split = entryVal.getValue().split(",");
                        for (String s : split) {
                            mergeMap.put(split[0], split[1]);
                            newVals.put(split[0], "0");
                        }
                    } else {
                        if (NumberUtil.isNumeric(entryVal.getValue())) {
                            final int i1 = Integer.parseInt(entryVal.getValue());
                            final String[] newValues = weekData.get(entryVal.getKey()).split(",");
                            String s = "0";
                            for (int j = 0; j <= i1; j++) {
                                s = s + "#" + 0;
                            }
                            for (String val : newValues) {
                                final String[] split = val.split("_");
                                newVals.put(split[0], split[1]);
                                mergeMap.put(split[0], s);
                            }
                        } else {
                            final String[] values = entryVal.getValue().split(",");
                            for (String val : values) {
                                final String[] split = val.split("_");
                                mergeMap.put(split[0], split[1]);
                            }
                            final String[] newValues = weekData.get(entryVal.getKey()).split(",");
                            for (String val : newValues) {
                                final String[] split = val.split("_");
                                newVals.put(split[0], split[1]);
                            }
                        }
                    }
                    for (Map.Entry<String, String> entryVal1 : mergeMap.entrySet()) {
                        if (newVals.get(entryVal1.getKey()) == null) {
                            mergeMap.put(entryVal1.getKey(), entryVal1.getValue() + "#" + 0);
                        } else {
                            mergeMap.put(entryVal1.getKey(), entryVal1.getValue() + "#" + newVals.get(entryVal1.getKey
                                    ()));
                            newVals.remove(entryVal1.getKey());
                        }
                    }
                    for (Map.Entry<String, String> entryVal1 : newVals.entrySet()) {
                        if (mergeMap.get(entryVal1.getValue()) == null) {
                            mergeMap.put(entryVal1.getKey(), 0 + "#" + entryVal1.getValue());
                        }
                    }
                    String s = "";
                    for (Map.Entry<String, String> entryVal1 : mergeMap.entrySet()) {
                        s = s + entryVal1.getKey() + "_" + entryVal1.getValue() + ",";
                    }
                    s = s.substring(0, s.length() - 1);
                    mergedDetailsMap.put(entryVal.getKey(), s);
                }
            }
        }

        for (Map.Entry<String, String> entry : mergedDetailsMap.entrySet()) {
            System.out.println(entry.getKey() + "  ===  " + entry.getValue());
        }
        final List<String> considatedSplunkResult = CommonUtil.getConsidatedSplunkResult(mergedDetailsMap, fieldMap);

        weeklyDetails.setResults(considatedSplunkResult);
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
            final String earliestDate = DateUtil.getPreviousDate(latestDate, format, ZONE_ID, 7);
            final String splunkResult = splunkQueryExecutor
                    .execute(splunkCredentials.getSplunkCredentials(), query, latestDate.format(format.withZone
                            (newYokZoneId)), earliestDate);
            splunkResults.put(index, splunkQueryExecutor.getParsedResult(splunkResult));
            latestDate = LocalDateTime.parse(earliestDate, format);
        }
        return splunkResults;
    }
}
