/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator.util;

public class NumberUtil {

    public static boolean isNumeric(final String s){

        return s.matches("-?\\d+(\\.\\d+)?((\\#(\\d+)(\\.\\d+)?)?)+");
    }
}
