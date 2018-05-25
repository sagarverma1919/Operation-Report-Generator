package com.expedia.risk.report.generator.Integration.flow.service;

public class ConfluenceUtility
{
    public static String createTableHeader(String reportName, String serviceName, String tableName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1 id='" + reportName + "-" + serviceName + "'><u>" + serviceName + "</u></h1><p><strong>" + tableName + "</strong></p>");
        return sb.toString();
    }
}
