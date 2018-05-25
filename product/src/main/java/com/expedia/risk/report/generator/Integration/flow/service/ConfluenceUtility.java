package com.expedia.risk.report.generator.Integration.flow.service;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.expedia.risk.report.generator.model.Field;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.model.Service;
import com.expedia.risk.report.generator.model.WeeklyDetails;

public class ConfluenceUtility
{
    public static String renderServiceName(String reportName, String serviceName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "<h1 id='" + reportName + "-" + serviceName + "'><u>" + serviceName
                        + "</u></h1>");

        return sb.toString();
    }

    public static String renderTableName(WeeklyDetails weeklyDetails)
    {

        StringBuilder sb = new StringBuilder();
        sb.append(
                " <p><strong style=\"color: rgb(0,0,0);font-size: 20.0px;\"><span>"
                        + weeklyDetails.getTableName()
                        + "</span></strong></p>");
        return sb.toString();
    }


    public static String renderTableContent(Report report, WeeklyDetails weeklyDetails)
    {

        StringBuilder sb = new StringBuilder("");
        sb.append("<div id=\"expander-774317728\" class=\"expand-container\"><div "
                + "id=\"expander-control-774317728\" class=\"expand-control\"><span "
                + "class=\"expand-control-icon icon\">&nbsp;</span><span "
                + "class=\"expand-control-text\">Statistical Details</span></div><div "
                + "id=\"expander-content-774317728\" class=\"expand-content expand-hidden\">");


        sb.append("\n"
                + "<div class=\"table-wrap\">\n"
                + "\n");

        sb.append("<table class=\"relative-table wrapped confluenceTable\" style=\"width: 100.0%;"
                + "\">\n"
                + "\n");

        sb.append("<thead>");
        sb.append("<tr>");
        for (Field field : weeklyDetails.getFields())
        {
            if (!field.isPartitioned())
            {
                sb.append("<th colspan=\"1\" class=\"confluenceTh\"><div "
                        + "class=\"tablesorter-header-inner\">");

                sb.append(field.getName());
                sb.append("</div></th>");
            }
            else
            {
                sb.append("<th colspan='" + report.getNoOfWeeks()
                        + "' class=\"confluenceTh\"><div class=\"tablesorter-header-inner\">");

                sb.append(field.getName());
                sb.append("</div></th>");
            }
        }
        for(Map.Entry<String,Object>map: weeklyDetails.getExtraColumns().entrySet()){
            sb.append("<th colspan=\"1\" class=\"confluenceTh\"><div "
                              + "class=\"tablesorter-header-inner\">");

            sb.append(map.getKey());
            sb.append("</div></th>");
        }
        sb.append("</tr><tr>");
        for (Field field : weeklyDetails.getFields())
        {
            if (!field.isPartitioned())
            {
                sb.append("<td colspan=\"1\" class=\"confluenceTd\"> </td>");


            }
            else
            {
                sb.append("<td colspan=\"1\" class=\"confluenceTd\"><strong>Previous to Last Week</strong></td>" +
                        "<td colspan=\"1\" class=\"confluenceTd\"><strong>Last Week</strong></td>" +
                        "<td colspan=\"1\" class=\"confluenceTd\"><strong>Current Week</strong></td>" );
            }
        }
        for(Map.Entry<String,Object>map: weeklyDetails.getExtraColumns().entrySet()){
            sb.append("<td colspan=\"1\" class=\"confluenceTd\"> </td>");
        }
        sb.append("</tr>");
        sb.append("</thead><tbody>");
        //loop
        List<String>dataList=weeklyDetails.getResults();
        for(String s:dataList){
            sb.append("<tr>");
            String arr[]=s.split(":");
            for(String inner:arr){
                if(inner.contains(",")){
                    String[] inner_inner=inner.split(",");
                    for(String in:inner_inner){
                        sb.append("<td colspan=\"1\" class=\"confluenceTd\">"+in+"</td>");
                    }
                }
                else
                {
                    sb.append("<td colspan=\"1\" class=\"confluenceTd\"><p>" + inner + "</p></td>");
                }
            }
            sb.append("</tr>");
        }

//loop
/*
        sb.append("<tr><td colspan=\"1\" class=\"confluenceTd\"><p>lookup</p></td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\"><span>51,240,056</span></td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\"><span>50,430,028</span></td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\"><span class=\"Apple-tab-span\"> 45,382,576</span></td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\">6</td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\">5</td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\">5</td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\"><span>32</span></td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\"><span>33</span></td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\">42</td>" +
                          "<td colspan=\"1\" class=\"confluenceTd\">250</td" +
                          "><td colspan=\"1\" class=\"confluenceTd\"> </td></tr>");*/

        sb.append("</tbody>");


        sb.append("</table>");
        sb.append("</div></div></div>");


        return sb.toString();


    }

    public static String renderServicePage(Report report, Service
            service)
    {

        StringBuilder sb = new StringBuilder("");
        /*sb.append("<h1 id=\"FraudOpsReport2018-02-19-2018-02-25_Mapper-ListService\"><u>List Service</u></h1>\n" +
                          "<p><strong style=\"color: rgb(0,0,0);font-size: 20.0px;\"><span>Operational Metrics</span></strong></p>\n" +
                          "<div id=\"expander-774317728\" class=\"expand-container\">\n" +
                          "<div id=\"expander-control-774317728\" class=\"expand-control\"><span class=\"expand-control-icon icon\">&nbsp;</span><span class=\"expand-control-text\">Statistical Details</span>\n" +
                          "</div>\n" +
                          "<div id=\"expander-content-774317728\" class=\"expand-content expand-hidden\">\n" +
                          "<div class=\"table-wrap\">\n" +
                          "<table class=\"relative-table wrapped confluenceTable\" style=\"width: 100.0%;\">\n" +
                          "<colgroup><col style=\"width: 8.56833%;\"/><col style=\"width: 9.97831%;\"/><col style=\"width: 9.97831%;\"/><col style=\"width: 7.80911%;\"/><col style=\"width: 6.29067%;\"/><col style=\"width: 6.29067%;\"/><col style=\"width: 7.80911%;\"/><col style=\"width: 6.29067%;\"/><col style=\"width: 6.29067%;\"/><col style=\"width: 7.80911%;\"/><col style=\"width: 6.18221%;\"/><col style=\"width: 16.7028%;\"/></colgroup>\n" +
                          "<thead><tr><th colspan=\"1\" class=\"confluenceTh\"><div class=\"tablesorter-header-inner\">API name</div></th><th colspan=\"3\" class=\"confluenceTh\"><div class=\"tablesorter-header-inner\">Total Transactions Count</div></th><th colspan=\"3\" class=\"confluenceTh\"><div class=\"tablesorter-header-inner\">TP99 in ms</div></th><th colspan=\"3\" class=\"confluenceTh\"><div class=\"tablesorter-header-inner\">TP99.9 in ms</div></th><th colspan=\"1\" class=\"confluenceTh\">SLA in ms</th><th colspan=\"1\" class=\"confluenceTh\">Comments</th></tr><tr><td colspan=\"1\" class=\"confluenceTd\"> </td><td colspan=\"1\" class=\"confluenceTd\"><strong>Previous to Last Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"><strong>Last Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"><strong>Current Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"><strong>Previous to Last Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"><p><strong>Last </strong><strong>Week</strong></p></td><td colspan=\"1\" class=\"confluenceTd\"><strong>Current Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"><strong>Previous to Last Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"><strong><strong>Last </strong>Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"><strong>Current Week</strong></td><td colspan=\"1\" class=\"confluenceTd\"> </td><td colspan=\"1\" class=\"confluenceTd\"> </td></tr></thead>\n" +
                          "<tbody>\n" +
                          "</tbody>\n" +
                          "</table></div></div></div>");*/
        sb.append(renderServiceName(report.getReportName(), service.getServiceName()));
        for (WeeklyDetails serviceDetail : service.getWeeklyDetails())
        {
            sb.append(renderTableName(serviceDetail));
            sb.append(renderTableContent(report,serviceDetail));
        }
        return sb.toString();
    }

    public static String renderServicesPage(Report report)
    {

        StringBuilder sb = new StringBuilder("");
        for (Service service : report.getServices())
        {
            sb.append(renderServicePage(report, service));
        }

        return sb.toString();
    }

    public static String renderPage(final Report report)
    {
        StringBuilder sb = new StringBuilder("");
        if (!CollectionUtils.isEmpty(report.getServices()))
        {
            sb.append(renderServicesPage(report));
        }
        return sb.toString();
    }

    //public static String
}
