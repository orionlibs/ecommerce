package de.hybris.platform.util;

import java.nio.charset.Charset;
import org.apache.log4j.Logger;

public final class CSVConstants
{
    private static final Logger log = Logger.getLogger(CSVConstants.class);
    public static final String HYBRIS_ENCODING = "UTF-8";
    public static final String DEFAULT_ENCODING = readAndCheckEncodingFromProperty();
    public static final String HYBRIS_LINE_SEPARATOR = "\n";
    public static final String DEFAULT_LINE_SEPARATOR = Config.getString("csv.lineseparator", readFromProperties("line.separator", "\n"));
    public static final char HYBRIS_FIELD_SEPARATOR = ';';
    public static final char DEFAULT_FIELD_SEPARATOR = readFromConfig("csv.fieldseparator", ';');
    public static final char HYBRIS_QUOTE_CHARACTER = '"';
    public static final char DEFAULT_QUOTE_CHARACTER = readFromConfig("csv.quotecharacter", '"');
    public static final char DEFAULT_COMMENT_CHAR = '#';
    public static final String DEFAULT_MULTI_LINE_SEPARATOR = "\\";
    public static final long HYBRIS_MAX_MULTI_LINES = 10000L;
    public static final long DEFAULT_MAX_MULTI_LINES = readFromConfig("csv.maxmultilines", 10000L);
    public static final String[] LINE_SEPARATORS = new String[] {"\n", "\r\n", "\r", "", " ", " "};


    private static char readFromConfig(String key, char def)
    {
        char ret = def;
        try
        {
            ret = Config.getChar(key, def);
        }
        catch(IndexOutOfBoundsException e)
        {
            log.warn("Property '" + key + "' configured at project.properties is not of type char (length is greater than 0), will use default value '" + def + "'");
        }
        return ret;
    }


    private static String readAndCheckEncodingFromProperty()
    {
        String encoding = Config.getString("csv.encoding", "UTF-8");
        if(!Charset.isSupported(encoding))
        {
            log.warn("The given encoding \"" + encoding + "\" is not supported by this VM. Please change in your property file the value for the key csv.encoding to a valid one. As fallback UTF-8 is used for now.");
            return "UTF-8";
        }
        return encoding;
    }


    private static long readFromConfig(String key, long def)
    {
        long ret = def;
        try
        {
            ret = Config.getLong(key, def);
        }
        catch(IndexOutOfBoundsException e)
        {
            log.warn("Property '" + key + "' configured at project.properties is an invalid long, will use default value '" + def + "'");
        }
        return ret;
    }


    private static String readFromProperties(String key, String def)
    {
        String ret = def;
        try
        {
            ret = System.getProperty(key, def);
        }
        catch(SecurityException e)
        {
            log.warn("Property '" + key + "' is not accessible at system properties, will use default value '" + def + "', reason: " + e
                            .getMessage());
        }
        return ret;
    }
}
