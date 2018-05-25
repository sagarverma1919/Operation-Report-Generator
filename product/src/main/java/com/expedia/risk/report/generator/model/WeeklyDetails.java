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
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeeklyDetails {

    @JsonProperty("TableName")
    private String tableName;
    @JsonProperty("Query")
    private String query;


    @JsonProperty("Fields")
    private List<Field> fields = null;
    @JsonProperty("Results")
    private List<String> results = null;
    @JsonProperty("ExtraColumn")
    private Map<String, Object> extraColumns = new HashMap<String, Object>();

    @JsonProperty("TableName")
    public String getTableName() {
        return tableName;
    }

    @JsonProperty("TableName")
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @JsonProperty("Query")
    public String getQuery() {
        return query;
    }

    @JsonProperty("Query")
    public void setQuery(String query) {
        this.query = query;
    }

    @JsonProperty("Fields")
    public List<Field> getFields() {
        return fields;
    }

    @JsonProperty("Fields")
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @JsonProperty("Results")
    public List<String> getResults() {
        return results;
    }

    @JsonProperty("Results")
    public void setResults(List<String> results) {
        this.results = results;
    }

    @JsonAnyGetter
    public Map<String, Object> getExtraColumns() {
        return this.extraColumns;
    }

    @JsonAnySetter
    public void setExtraColumns(String name, Object value) {
        this.extraColumns.put(name, value);
    }

}
