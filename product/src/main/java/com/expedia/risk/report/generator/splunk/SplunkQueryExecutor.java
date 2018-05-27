/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator.splunk;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import com.expedia.risk.report.generator.model.SplunkResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.splunk.Args;
import com.splunk.Service;
import com.splunk.ServiceArgs;

@Component
public class SplunkQueryExecutor {

    public String execute(final ServiceArgs serviceArgs, final String splunkQuery, final String latestTime, final
    String earliestTime) throws IOException {
        Service service = Service.connect(serviceArgs);
        String stringFromStream = null;
        Args args1 = new Args();
        args1.put("output_mode", "json");
        args1.put("earliest_time", earliestTime);
        args1.put("latest_time", latestTime);
        System.out.println("latest: "+ latestTime+ " earliest: "+ earliestTime);
        try (
                InputStream data = service.oneshotSearch(splunkQuery, args1)) {
            stringFromStream = CharStreams.toString(new InputStreamReader(data, Charsets.UTF_8));
            System.out.println(stringFromStream);
        }
        System.out.println();
        return stringFromStream;
    }

    public SplunkResult getParsedResult(final String splunkResult) {
        SplunkResult result = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.readValue(splunkResult, SplunkResult
                    .class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
