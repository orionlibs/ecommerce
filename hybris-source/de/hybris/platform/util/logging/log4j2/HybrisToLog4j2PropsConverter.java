package de.hybris.platform.util.logging.log4j2;

import com.google.common.collect.ImmutableSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;

public class HybrisToLog4j2PropsConverter
{
    private static final Logger LOG = Logger.getLogger(HybrisToLog4j2PropsConverter.class);
    private static final String LOG4J2_CONFIG_PREFIX = "log4j2.";
    private static final Set ignoredProperties = (Set)ImmutableSet.of("appenders", "loggers", "filters");


    public Properties convertToLog4jProps(Properties platformProperties)
    {
        Properties properties = removeLog4jProps(platformProperties);
        if(isANSIDisable(properties) || isWindowsOSAndDoesntHaveJansiInClassPath())
        {
            disableANSI(properties);
        }
        Properties log4j2Properties = new Properties();
        Definitions definitions = new Definitions();
        for(Map.Entry<Object, Object> entry : properties.entrySet())
        {
            String key = (String)entry.getKey();
            if(isLog4j2Property(key) && isElementDefinition(removeLog4jPrefix(key)))
            {
                String strippedProperty = removeLog4jPrefix(key);
                log4j2Properties.put(strippedProperty, entry.getValue());
                definitions.collectDefinition(strippedProperty);
            }
        }
        log4j2Properties.putAll(definitions.toMap());
        return log4j2Properties;
    }


    protected Properties removeLog4jProps(Properties platformProperties)
    {
        List<String> removeLog4j2Properties = getRemoveProperties(platformProperties);
        return filterRemovedProperties(platformProperties, removeLog4j2Properties);
    }


    private List<String> getRemoveProperties(Properties platformProperties)
    {
        List<String> removeLog4j2List = new LinkedList<>();
        for(Map.Entry<Object, Object> entry : platformProperties.entrySet())
        {
            String key = (String)entry.getKey();
            if(key.startsWith("remove.log4j2.") && "true".equals(entry.getValue()))
            {
                String strippedProperty = key.replace("remove.", "");
                removeLog4j2List.add(strippedProperty);
            }
        }
        return removeLog4j2List;
    }


    private Properties filterRemovedProperties(Properties platformProperties, List<String> removeLog4j2List)
    {
        if(!removeLog4j2List.isEmpty())
        {
            Properties log4j2Properties = new Properties();
            for(Map.Entry<Object, Object> entry : platformProperties.entrySet())
            {
                if(!removeLog4j2List.contains(entry.getKey()))
                {
                    log4j2Properties.put(entry.getKey(), entry.getValue());
                }
            }
            return log4j2Properties;
        }
        return platformProperties;
    }


    private void disableANSI(Properties platformProperties)
    {
        String value = platformProperties.getProperty("log4j2.appender.console.layout.pattern");
        if(value == null)
        {
            return;
        }
        try
        {
            platformProperties.replace("log4j2.appender.console.layout.pattern", removeHighlightSyntax(value));
        }
        catch(IndexOutOfBoundsException ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("'log4j2.appender.console.layout.pattern' - highlight syntax seems to be invalid: " + value);
            }
        }
    }


    public boolean isANSIDisable(Properties platformProperties)
    {
        return !Boolean.parseBoolean(platformProperties.getProperty("ansi.colors", "true"));
    }


    public boolean isWindowsOSAndDoesntHaveJansiInClassPath()
    {
        String osName = System.getProperty("os.name");
        if(osName == null)
        {
            return false;
        }
        return (osName.startsWith("Windows") && !isJansiInClassPath());
    }


    private boolean isJansiInClassPath()
    {
        try
        {
            Class.forName("org.fusesource.jansi.AnsiConsole");
        }
        catch(ClassNotFoundException e)
        {
            return false;
        }
        return true;
    }


    private boolean isElementDefinition(String property)
    {
        return !ignoredProperties.contains(property.toLowerCase());
    }


    private boolean isLog4j2Property(String property)
    {
        return property.startsWith("log4j2.");
    }


    private String removeLog4jPrefix(String hybrisLog4j2Prop)
    {
        return hybrisLog4j2Prop.replace("log4j2.", "");
    }


    private String removeHighlightSyntax(String value) throws IndexOutOfBoundsException
    {
        if(value == null)
        {
            return value;
        }
        for(ColorPattern pattern : ColorPattern.values())
        {
            while(value.contains(pattern.openingPattern))
            {
                value = removeHighlightSyntax(pattern, value);
            }
        }
        return value;
    }


    private String removeHighlightSyntax(ColorPattern pattern, String value) throws IndexOutOfBoundsException
    {
        int nextClosingBracket, firstBracket = value.indexOf("{", value.indexOf(pattern.openingPattern));
        int closingBracket = findClosingParen(value.toCharArray(), firstBracket);
        String convertedString = value.substring(0, value.indexOf(pattern.openingPattern));
        convertedString = convertedString + convertedString;
        switch(null.$SwitchMap$de$hybris$platform$util$logging$log4j2$HybrisToLog4j2PropsConverter$ColorPattern[pattern.ordinal()])
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                convertedString = convertedString + convertedString;
                break;
            case 10:
                nextClosingBracket = value.indexOf("}", closingBracket + 1);
                convertedString = convertedString + convertedString;
                break;
        }
        return convertedString;
    }


    private String removeHighlightStyle(String value) throws ArrayIndexOutOfBoundsException
    {
        if(value == null || (!value.startsWith("{STYLE=") && !value.startsWith("{FATAL=")))
        {
            return value;
        }
        int closingBracket = findClosingParen(value.toCharArray(), 0);
        return value.substring(closingBracket + 1);
    }


    private int findClosingParen(char[] text, int openPos) throws ArrayIndexOutOfBoundsException
    {
        int closePos = openPos;
        int counter = 1;
        while(counter > 0)
        {
            char c = text[++closePos];
            if(c == '{')
            {
                counter++;
                continue;
            }
            if(c == '}')
            {
                counter--;
            }
        }
        return closePos;
    }
}
