package com.expedia.risk.report.generator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Service
{

    @JsonProperty("ServiceName")
    private String serviceName;

    /* @JsonProperty("ServiceDetails")
     private List<ServiceDetails> serviceDetailsList = null;
 */
    @JsonProperty("Details")
    private List<WeeklyDetails> weeklyDetails;


    public List<WeeklyDetails> getWeeklyDetails()
    {
        return weeklyDetails;
    }

    public void setWeeklyDetails(final List<WeeklyDetails> weeklyDetails)
    {
        this.weeklyDetails = weeklyDetails;
    }


   /* @JsonProperty("ServiceDetails")
    public List<ServiceDetails> getServiceDetailsList()
    {
        return serviceDetailsList;
    }

    @JsonProperty("ServiceDetails")
    public void setServiceDetailsList(List<ServiceDetails> serviceDetailsList)
    {
        this.serviceDetailsList = serviceDetailsList;
    }*/

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(final String serviceName)
    {
        this.serviceName = serviceName;
    }


}
