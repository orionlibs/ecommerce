package com.hybris.backoffice.excel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class ExcelUtils
{
    public static final Pattern PATTERN_CELL_TOKENS = Pattern.compile("([^:\\[\\]]+)|(\\[.*?\\])|()");


    public static String[] extractExcelCellTokens(String cellValue)
    {
        if(StringUtils.isEmpty(cellValue))
        {
            return new String[0];
        }
        List<String> tokens = new ArrayList<>();
        Matcher matcher = PATTERN_CELL_TOKENS.matcher(cellValue);
        int lastGroupEnd = -1;
        while(matcher.find())
        {
            if(lastGroupEnd != matcher.start())
            {
                String group = matcher.group().trim();
                if(group.length() > 1 && group.charAt(0) == '[' && group.charAt(group.length() - 1) == ']')
                {
                    tokens.add(group.substring(1, group.length() - 1).trim());
                }
                else
                {
                    tokens.add(group);
                }
            }
            lastGroupEnd = matcher.end();
        }
        return tokens.<String>toArray(new String[0]);
    }
}
