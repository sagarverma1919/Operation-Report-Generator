package com.expedia.risk.report.generator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.expedia.risk.report.generator.model.Report;

public class CommonUtil
{



    /**
     *
     * @param input
     * @param fieldsOrderMap
     * @param report
     */
    public static List<String> getConsidatedSplunkResult(Map<String,String>input, Map<String,Integer> fieldsOrderMap){

        // to be removed start
        input=new LinkedHashMap<>();
        input.put("0_A,2_GET","1_22#20,3_2#5");
        input.put("0_B,2_POST","1_22#20,3_2#5");
        input.put("0_C,2_GET","1_0#1,3_0#4");
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
                        sb.append(s.split("_")[1]).append(":");
                        i++;
                        break;
                    }
                }
            }
            result.add(sb.substring(0,sb.length()-1));
        }
        return result;

    }

}
