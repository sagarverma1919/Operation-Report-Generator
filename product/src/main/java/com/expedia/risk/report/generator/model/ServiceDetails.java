/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceDetails {


    @JsonProperty("Details")
    private List<WeeklyDetails> weeklyDetails;


    public List<WeeklyDetails> getWeeklyDetails() {
        return weeklyDetails;
    }

    public void setWeeklyDetails(final List<WeeklyDetails> weeklyDetails) {
        this.weeklyDetails = weeklyDetails;
    }
}

