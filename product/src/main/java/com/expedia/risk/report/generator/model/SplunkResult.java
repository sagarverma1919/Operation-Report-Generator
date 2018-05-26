/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "preview",
        "init_offset",
        "messages",
        "fields",
        "results"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplunkResult {

    @JsonProperty("preview")
    private String preview;
    @JsonProperty("init_offset")
    private String initOffset;

    @JsonProperty("messages")
    private List<String> messages;

    @JsonProperty("fields")
    private List<Field> field;

    @JsonProperty("results")
    private List<Map<String,String>> results;

    public String getPreview() {
        return preview;
    }

    public void setPreview(final String preview) {
        this.preview = preview;
    }

    public String getInitOffset() {
        return initOffset;
    }

    public void setInitOffset(final String initOffset) {
        this.initOffset = initOffset;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(final List<String> messages) {
        this.messages = messages;
    }

    public List<Field> getField() {
        return field;
    }

    public void setField(final List<Field> field) {
        this.field = field;
    }

    public List<Map<String, String>> getResults() {
        return results;
    }

    public void setResults(final List<Map<String, String>> results) {
        this.results = results;
    }
}
