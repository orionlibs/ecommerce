package de.hybris.platform.util;

import java.text.MessageFormat;
import java.util.Locale;

public class MessageFormatUtils
{
    public static String format(String pattern, Object... arguments)
    {
        MessageFormat messageFormat = new MessageFormat(pattern, Locale.ROOT);
        return messageFormat.format(arguments);
    }
}
