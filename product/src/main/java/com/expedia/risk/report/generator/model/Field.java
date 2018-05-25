package com.expedia.risk.report.generator.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Field {

    @JsonProperty("name")
    private String name;
    @JsonProperty("groupby_rank")
    private String groupbyRank;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("groupby_rank")
    public String getGroupbyRank() {
        return groupbyRank;
    }

    @JsonProperty("groupby_rank")
    public void setGroupbyRank(String groupbyRank) {
        this.groupbyRank = groupbyRank;
    }


}