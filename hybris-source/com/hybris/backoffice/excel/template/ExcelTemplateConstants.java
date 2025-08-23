package com.hybris.backoffice.excel.template;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExcelTemplateConstants
{
    public static final int HEADER_ROW_INDEX = 0;
    public static final int REFERENCE_PATTERN_ROW_INDEX = 1;
    public static final int DEFAULT_VALUES_ROW_INDEX = 2;
    public static final int FIRST_DATA_ROW = 3;
    public static final int TYPE_SYSTEM_FIRST_ROW_INDEX = 1;
    public static final String MULTI_VALUE_DELIMITER = ",";
    public static final String REFERENCE_PATTERN_SEPARATOR = ":";
    private static final String TYPE_SYSTEM_SHEET_NAME = "TypeSystem";
    private static final String TYPE_TEMPLATE_SHEET_NAME = "TypeTemplate";
    @Deprecated(since = "1808", forRemoval = true)
    public static final String TYPE_SYSTEM = "TypeSystem";
    @Deprecated(since = "1808", forRemoval = true)
    public static final String TYPE_TEMPLATE = "TypeTemplate";
    @Deprecated(since = "1808", forRemoval = true)
    protected static final Set<String> UTILITY_SHEETS = new HashSet<>(
                    Arrays.asList(new String[] {UtilitySheet.TYPE_SYSTEM.getSheetName(), UtilitySheet.TYPE_TEMPLATE.getSheetName()}));


    @Deprecated(since = "1808", forRemoval = true)
    public static boolean isUtilitySheet(String sheetName)
    {
        return UTILITY_SHEETS.contains(sheetName);
    }
}
