package com.expedia.risk.report.generator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.expedia.risk.report.generator.model.Field;
import com.expedia.risk.report.generator.model.Report;
import com.expedia.risk.report.generator.model.WeeklyDetails;

public class CommonUtil
{



    /**
     *
     * @param input
     * @param fieldsOrderMap
     * @param report
     */
    public static List<String> getConsidatedSplunkResult(Map<String,String>input, Map<String,Integer> fieldsOrderMap, Map<String, Object> extraColumns, WeeklyDetails weeklyDetails){

        // to be removed start

        // to be removed ends
        StringBuilder sb=null;
        List<String>result=new ArrayList<>();
        for(Map.Entry<String,String>map:input.entrySet()){
            sb=new StringBuilder("");
            String key=map.getKey();
            String value=map.getValue();
            String keyValueArray[]=key.concat(",").concat(value).split(",");
            int i=0;
            while(i!=fieldsOrderMap.size()){
                for(String s:keyValueArray){
                    if(Integer.parseInt(s.split("_")[0])==i)
                    {

                        Integer index=Integer.parseInt(s.split("_")[0]);
                        String temp=s.split("_")[1];
                        if(temp.contains("#")){
                          List<Field>fields=  weeklyDetails.getFields();
                          int j=0;
                          for(Field field:fields){
                              if(j==index){
                                  field.setPartitioned(true);
                              }
                              j++;
                          }
                        }
                        sb.append(temp).append(":");
                        i++;
                        break;
                    }
                }
            }
            if(!CollectionUtils.isEmpty(extraColumns)){
                for(Map.Entry<String,Object>mapTemp:extraColumns.entrySet()){
                    sb.append(" ").append(":");
                }
            }
            result.add(sb.substring(0,sb.length()-1));
        }
        return result;

    }

}
