package com.expedia.risk.report.generator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ReportName",
        "EmailId",
        "NoOfWeeks",
        "Services"
})
public class Report {

    @JsonProperty("ReportName")
    private String reportName;
    @JsonProperty("EmailId")
    private String emailId;
    @JsonProperty("NoOfWeeks")
    private String noOfWeeks;
    @JsonProperty("Services")
    private List<Service> services = null;

    @JsonProperty("ReportName")
    public String getReportName() {
        return reportName;
    }

    @JsonProperty("ReportName")
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @JsonProperty("EmailId")
    public String getEmailId() {
        return emailId;
    }

    @JsonProperty("EmailId")
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @JsonProperty("NoOfWeeks")
    public String getNoOfWeeks() {
        return noOfWeeks;
    }

    @JsonProperty("NoOfWeeks")
    public void setNoOfWeeks(String noOfWeeks) {
        this.noOfWeeks = noOfWeeks;
    }

    @JsonProperty("Services")
    public List<Service> getServices() {
        return services;
    }

    @JsonProperty("Services")
    public void setServices(List<Service> services) {
        this.services = services;
    }


}