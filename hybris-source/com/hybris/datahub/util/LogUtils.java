package com.hybris.datahub.util;

import java.util.List;
import java.util.stream.Collectors;

public class LogUtils
{
    private static final String PATTERN_BREAKING = "[\n|\r]";


    public static String logSafe(String message)
    {
        return message.replaceAll("[\n|\r]", "");
    }


    public static List<String> logSafe(List<String> messages)
    {
        return (List<String>)messages.stream().map(message -> message.replaceAll("[\n|\r]", "")).collect(Collectors.toList());
    }
}
