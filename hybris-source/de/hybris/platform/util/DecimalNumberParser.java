package de.hybris.platform.util;

import java.util.regex.Pattern;

public class DecimalNumberParser
{
    private static final Pattern DECIMAL_PART_PATTERN = Pattern.compile("\\.\\d+$");
    private static final Pattern NUMBER_WITH_ZEROS_AS_DECIMAL_PART_PATTERN = Pattern.compile("^-?(0|([1-9]\\d*?))\\.0+$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?(0|([1-9]\\d*?))(\\.[0-9]+)?$");


    public String stripDecimalPart(String input)
    {
        String stripped = DECIMAL_PART_PATTERN.matcher(input).replaceFirst("");
        return "-0".equals(stripped) ? "0" : stripped;
    }


    public boolean hasOnlyZeroesAsDecimalPart(String input)
    {
        return NUMBER_WITH_ZEROS_AS_DECIMAL_PART_PATTERN.matcher(input).matches();
    }


    public boolean isValidNumber(String input)
    {
        return NUMBER_PATTERN.matcher(input).matches();
    }
}
