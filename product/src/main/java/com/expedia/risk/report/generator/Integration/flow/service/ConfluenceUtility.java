package com.expedia.risk.report.generator.Integration.flow.service;

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
                " <p ><strong style=\\\"color: rgb(0,0,0);font-size: 20.0px;\\\" > <span>"
                        + weeklyDetails.getTableName()
                        + "</span></strong></p>");
        return sb.toString();
    }


    public static String renderTableContent(Report report, WeeklyDetails weeklyDetails)
    {

        StringBuilder sb = new StringBuilder("");
        sb.append("<div id=\\\"expander-774317728\\\" class=\\\"expand-container\\\"><div "
                + "id=\\\"expander-control-774317728\\\" class=\\\"expand-control\\\"><span "
                + "class=\\\"expand-control-icon icon\\\">&nbsp;</span><span "
                + "class=\\\"expand-control-text\\\">Statistical Details</span></div><div "
                + "id=\\\"expander-content-774317728\\\" class=\\\"expand-content expand-hidden\\\">");


        sb.append("\n"
                + "<div class=\\\"table-wrap\\\">\n"
                + "\n");

        sb.append("<table class=\\\"relative-table wrapped confluenceTable\\\" style=\\\"width: 100.0%;"
                + "\\\"><colgroup><col style=\\\"width: 8.56833%;\\\"/><col style=\\\"width: 9.97831%;\\\"/><col "
                + "style=\\\"width: 9.97831%;\\\"/><col style=\\\"width: 7.80911%;\\\"/><col style=\\\"width: "
                + "6.29067%;\\\"/><col style=\\\"width: 6.29067%;\\\"/><col style=\\\"width: 7.80911%;\\\"/><col "
                + "style=\\\"width: 6.29067%;\\\"/><col style=\\\"width: 6.29067%;\\\"/><col style=\\\"width: "
                + "7.80911%;\\\"/><col style=\\\"width: 6.18221%;\\\"/><col style=\\\"width: 16.7028%;"
                + "\\\"/></colgroup>\n"
                + "\n");

        sb.append("<thead>");
        sb.append("<tr>");
        for (Field field : weeklyDetails.getFields())
        {
            if (!field.isPartitioned())
            {
                sb.append("<th colspan=\\\"1\\\" class=\\\"confluenceTh\\\"><div "
                        + "class=\\\"tablesorter-header-inner\\\">");

                sb.append(field.getName());
                sb.append("</div></th>");
            }
            else
            {
                sb.append("<th colspan='" + report.getNoOfWeeks()
                        + "' class=\\\"confluenceTh\\\"><div class=\\\"tablesorter-header-inner\\\">");

                sb.append(field.getName());
                sb.append("</div></th>");
            }
        }
        sb.append("</tr>");
        sb.append("</thead>");


        sb.append("</table>");
        sb.append("</div></div></div>");


        return sb.toString();


    }

    public static String renderServicePage(Report report, Service
            service)
    {

        StringBuilder sb = new StringBuilder("");
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
            renderServicesPage(report);
        }
        return sb.toString();
    }

    //public static String
}
