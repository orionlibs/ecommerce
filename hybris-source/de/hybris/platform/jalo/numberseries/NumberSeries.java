package de.hybris.platform.jalo.numberseries;

import de.hybris.platform.core.MasterTenant;
import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class NumberSeries implements Serializable
{
    public static Logger LOG = Logger.getLogger(NumberSeries.class);
    public static final int TYPE_ALPHANUMERIC = 0;
    public static final int TYPE_NUMERIC = 1;
    private final String key;
    private final long number;
    private final int type;
    private final String template;


    @Deprecated(since = "5.0", forRemoval = false)
    public NumberSeries(String key, String formattedValue, int type)
    {
        this(key, formattedValue, type, null);
    }


    public NumberSeries(String key, String formattedValue, int type, String template)
    {
        this.key = key;
        try
        {
            this.number = (type == 0) ? Long.parseLong(formattedValue, 36) : Long.parseLong(formattedValue);
        }
        catch(NullPointerException e)
        {
            throw new IllegalArgumentException("value '" + formattedValue + "' must not be NULL");
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("value '" + formattedValue + "' is not legal: " + e.getMessage());
        }
        this.type = type;
        this.template = validateTemplate(template);
    }


    private String validateTemplate(String template)
    {
        if(template == null)
        {
            return null;
        }
        if(template.indexOf('$') == -1)
        {
            LOG.warn("Invalid template: \"" + template + "\" for number series: " + this.key + ". Creating number series: " + this.key + " without template.");
            return null;
        }
        return template;
    }


    @Deprecated(since = "5.0", forRemoval = false)
    public NumberSeries(String key, long value, int type)
    {
        this(key.intern(), value, type, null);
    }


    public NumberSeries(String key, long value, int type, String template)
    {
        this.key = key.intern();
        this.number = value;
        this.type = type;
        this.template = validateTemplate(template);
    }


    public long getCurrentNumber()
    {
        return this.number;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getCurrentValue()
    {
        return getFormatted(-1);
    }


    public String getFormatted(int digits)
    {
        String ret = null;
        switch(getType())
        {
            case 0:
                ret = Long.toString(getCurrentNumber(), 36).toUpperCase();
                s = ret.length();
                if(s < digits)
                {
                    char[] chars = new char[digits];
                    ret.getChars(0, s, chars, digits - s);
                    Arrays.fill(chars, 0, digits - s, '0');
                    ret = String.valueOf(chars);
                }
                return applyPatternIfExists(ret);
        }
        ret = Long.toString(getCurrentNumber());
        int s = ret.length();
        if(s < digits)
        {
            char[] chars = new char[digits];
            ret.getChars(0, s, chars, digits - s);
            Arrays.fill(chars, 0, digits - s, '0');
            ret = String.valueOf(chars);
        }
        return applyPatternIfExists(ret);
    }


    protected String applyPatternIfExists(String formattedNumber)
    {
        if(this.template != null)
        {
            if(this.template.indexOf('\\') == -1)
            {
                return replaceSpecialCharacters(this.template, formattedNumber);
            }
            StringBuffer result = new StringBuffer();
            Pattern pattern = Pattern.compile("\\\\.");
            Matcher matcher = pattern.matcher(this.template);
            int start = 0;
            int end = 0;
            while(matcher.find())
            {
                result.append(replaceSpecialCharacters(this.template.substring(start, matcher.start()), formattedNumber));
                if(matcher.group().equals("\\\\"))
                {
                    result.append("\\");
                }
                else
                {
                    result.append(matcher.group());
                }
                start = matcher.start() + matcher.group().length();
                end = matcher.end();
            }
            result.append(replaceSpecialCharacters(this.template.substring(end), formattedNumber));
            return result.toString();
        }
        return formattedNumber;
    }


    protected String replaceSpecialCharacters(String template, String formattedNumber)
    {
        return template.replaceAll("\\$", formattedNumber).replaceAll("@",
                        String.valueOf(
                                        MasterTenant.getInstance().getClusterIslandPK()));
    }


    public String getTemplate()
    {
        return this.template;
    }


    public String getKey()
    {
        return this.key;
    }


    public int getType()
    {
        return this.type;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTemplate(String template)
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCurrentValue(String value)
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setKey(String key)
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setType(int type)
    {
    }
}
