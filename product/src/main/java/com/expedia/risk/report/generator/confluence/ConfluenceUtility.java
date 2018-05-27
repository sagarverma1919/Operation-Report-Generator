package com.expedia.risk.report.generator.confluence;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.expedia.risk.report.generator.model.Field;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.model.Service;
import com.expedia.risk.report.generator.model.WeeklyDetails;

public class ConfluenceUtility
{

    /**
     * render table service name
     */
    public static String renderServiceName(String reportName, String serviceName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "<h1 id='" + reportName + "-" + serviceName + "'><u>" + serviceName
                        + "</u></h1>");

        return sb.toString();
    }

    /**
     * render table name
     */
    public static String renderTableName(WeeklyDetails weeklyDetails)
    {

        StringBuilder sb = new StringBuilder();
        sb.append(
                " <p><strong style=\"color: rgb(0,0,0);font-size: 20.0px;\"><span>"
                        + weeklyDetails.getTableName()
                        + "</span></strong></p>");
        return sb.toString();
    }

    /**
     * render table
     */
    public static String renderTableContent(Report report, WeeklyDetails weeklyDetails)
    {

        StringBuilder sb = new StringBuilder("");
        sb.append("<div  class=\"expand-container\"><div "
                + "class=\"expand-control\"><span "
                + "class=\"expand-control-icon icon\">&nbsp;</span><span "
                + "class=\"expand-control-text\">Statistical Details</span></div><div "
                + "class=\"expand-content expand-hidden\">");


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
        if (!CollectionUtils.isEmpty(weeklyDetails.getExtraColumns()))
        {
            for (Map.Entry<String, Object> map : weeklyDetails.getExtraColumns().entrySet())
            {
                sb.append("<th colspan=\"1\" class=\"confluenceTh\"><div "
                        + "class=\"tablesorter-header-inner\">");

                sb.append(map.getKey());
                sb.append("</div></th>");
            }
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
                if(report.getNoOfWeeks()==2){
                    sb.append("<td colspan=\"1\" class=\"confluenceTd\"><strong>Last Week</strong></td>" +
                            "<td colspan=\"1\" class=\"confluenceTd\"><strong>Current Week</strong></td>");
                }
                else if(report.getNoOfWeeks()==3){
                    sb.append("<td colspan=\"1\" class=\"confluenceTd\"><strong>Previous to Last Week</strong></td>" +
                            "<td colspan=\"1\" class=\"confluenceTd\"><strong>Last Week</strong></td>" +
                            "<td colspan=\"1\" class=\"confluenceTd\"><strong>Current Week</strong></td>");
                }

            }
        }
        if (!CollectionUtils.isEmpty(weeklyDetails.getExtraColumns()))
        {
            for (Map.Entry<String, Object> map : weeklyDetails.getExtraColumns().entrySet())
            {
                sb.append("<td colspan=\"1\" class=\"confluenceTd\"> </td>");
            }
        }
        sb.append("</tr>");
        sb.append("</thead><tbody>");

        List<String> dataList = weeklyDetails.getResults();
        for (String s : dataList)
        {
            sb.append("<tr>");
            String arr[] = s.split(":");
            for (String inner : arr)
            {
                if (inner.contains("#"))
                {
                    String[] inner_inner = inner.split("#");
                    for (String in : inner_inner)
                    {
                        sb.append("<td colspan=\"1\" class=\"confluenceTd\">" + in + "</td>");
                    }
                }
                else
                {
                    sb.append("<td colspan=\"1\" class=\"confluenceTd\"><p>" + inner + "</p></td>");
                }
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div></div></div>");
        /*  sb.append("<div class=\"code panel pdl conf-macro output-block\" data-hasbody=\"true\" "
                + "data-macro-name=\"code\" style=\"border-width: 1px;\">\n"
                + " <div class=\"codeHeader panelHeader pdl hide-border-bottom\" style=\"border-bottom-width: 1px;\">\n"
                + "  <b class=\" code-title\">Splunk query</b>\n"
                + "  <span class=\"collapse-source expand-control\" style=\"display:none;\"><span "
                + "class=\"expand-control-icon icon\"> </span><span class=\"expand-control-text\">Expand "
                + "source</span></span>\n"
                + "  <span class=\"collapse-spinner-wrapper\"> </span>\n"
                + " </div>\n"
                + " <div class=\"codeContent panelContent pdl hide-toolbar\"> \n"
                + "  <pre class=\"syntaxhighlighter-pre\" data-syntaxhighlighter-params=\"brush: java; gutter: false;"
                + " theme: Eclipse; collapse: true\" data-theme=\"Eclipse\">" + weeklyDetails.getQuery() + "</pre> \n"
                + " </div>\n"
                + "</div>");*/
        sb.append(
                "<div  class=\"expand-container\" style=\"border-width: 1.0px;border-bottom-color: #ccc;text-align: \n"
                        + " left;padding: 5px 15px;background: #f5f5f5;overflow: hidden;position: relative;border: "
                        + "1px solid \n"
                        + "#ccc;\"> \n "
                        + " <div \n"
                        + "class=\"expand-control  \"><span \n"
                        + "class=\"expand-control-icon icon\">&nbsp;</span><span \n"
                        + "class=\"expand-control-text\">Splunk Query</span></div><div \n"
                        + "class=\"expand-content expand-hidden\">");
        sb.append("<pre class=\"syntaxhighlighter-pre\" data-syntaxhighlighter-params=\"brush: java; \n"
                + "gutter: false; \n"
                + " theme: Eclipse; collapse: true\" data-theme=\"Eclipse\">\n "
                + weeklyDetails.getQuery() + "</pre> ");
        sb.append("</div></div>");
        return sb.toString();
    }

    /**
     * render service page
     */
    public static String renderServicePage(Report report, Service
            service)
    {
        StringBuilder sb = new StringBuilder("");
        sb.append(renderServiceName(report.getReportName(), service.getServiceName()));
        if (!CollectionUtils.isEmpty(service.getWeeklyDetails()))
        {
            for (WeeklyDetails serviceDetail : service.getWeeklyDetails())
            {
                sb.append(renderTableName(serviceDetail));
                sb.append(renderTableContent(report, serviceDetail));
                sb.append("<br></br>");
            }
        }
        return sb.toString();
    }

    /**
     * render all services
     */
    public static String renderServicesPage(Report report)
    {

        StringBuilder sb = new StringBuilder("");
        for (Service service : report.getServices())
        {
            sb.append(renderServicePage(report, service));
            sb.append("<br></br><br></br>");
        }

        return sb.toString();
    }

    /**
     * render team ops report confluence page
     */
    public static String renderPage(final Report report)
    {
        StringBuilder sb = new StringBuilder("");
        if (!CollectionUtils.isEmpty(report.getServices()))
        {
            sb.append(renderServicesPage(report));
        }
        return sb.toString();
    }


}
