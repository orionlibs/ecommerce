package de.hybris.platform.webservicescommons.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class YSanitizer
{
    public static String sanitize(String input)
    {
        String output = StringUtils.defaultString(input).trim();
        output = output.replaceAll("(\\r\\n|\\r|\\n)+", " ");
        output = StringEscapeUtils.escapeHtml(output);
        return output;
    }
}
