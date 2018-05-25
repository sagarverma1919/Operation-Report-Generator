package com.expedia.risk.report.generator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Service {

    @JsonProperty("ServiceDetails")
    private List<ServiceDetails> serviceDetailsList = null;


    @JsonProperty("ServiceDetails")
    public List<ServiceDetails> getServiceDetailsList() {
        return serviceDetailsList;
    }

    @JsonProperty("ServiceDetails")
    public void setServiceDetailsList(List<ServiceDetails> serviceDetailsList) {
        this.serviceDetailsList = serviceDetailsList;
    }



}
