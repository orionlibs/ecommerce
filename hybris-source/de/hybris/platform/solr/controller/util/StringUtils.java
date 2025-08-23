package de.hybris.platform.solr.controller.util;

public final class StringUtils
{
    public static final String EMPTY = "";


    public static final boolean isBlank(String str)
    {
        return (str == null || str.isEmpty());
    }


    public static final boolean isNotBlank(String str)
    {
        return !isBlank(str);
    }


    public static final boolean startsWith(String str, String prefix)
    {
        if(str == null && prefix == null)
        {
            return true;
        }
        if(str == null || prefix == null)
        {
            return false;
        }
        return str.startsWith(prefix);
    }


    public static final boolean endsWith(String str, String suffix)
    {
        if(str == null && suffix == null)
        {
            return true;
        }
        if(str == null || suffix == null)
        {
            return false;
        }
        return str.endsWith(suffix);
    }


    public static final String removeStart(String str, String remove)
    {
        if(str == null)
        {
            return null;
        }
        if(str.isEmpty() || remove == null || remove.isEmpty())
        {
            return str;
        }
        if(str.startsWith(remove))
        {
            return str.substring(remove.length());
        }
        return str;
    }


    public static final String removeEnd(String str, String remove)
    {
        if(str == null)
        {
            return null;
        }
        if(str.isEmpty() || remove == null || remove.isEmpty())
        {
            return str;
        }
        if(str.endsWith(remove))
        {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }
}
