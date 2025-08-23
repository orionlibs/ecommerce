package de.hybris.bootstrap.ddl.dbtypesystem;

import de.hybris.platform.core.PK;

final class ObjectToNormalizedStringTransformer
{
    private static final String nullValue = "[|NULL|]";


    public String getStringFrom(Object object)
    {
        if(object == null)
        {
            return "[|NULL|]";
        }
        if(object instanceof Boolean)
        {
            return ((Boolean)object).booleanValue() ? "1" : "[|NULL|]";
        }
        if(object instanceof Number && ((Number)object).longValue() == 0L)
        {
            return "[|NULL|]";
        }
        if(object instanceof PK && ((PK)object).getLongValue() == 0L)
        {
            return "[|NULL|]";
        }
        return toJsonString(object.toString());
    }


    private String toJsonString(String rawString)
    {
        StringBuilder jsonStringBuilder = new StringBuilder();
        for(int i = 0; i < rawString.length(); i++)
        {
            if(rawString.charAt(i) == '\n')
            {
                jsonStringBuilder.append('\\');
                jsonStringBuilder.append('n');
            }
            else if(rawString.charAt(i) == '\r')
            {
                jsonStringBuilder.append('\\');
                jsonStringBuilder.append('r');
            }
            else if(rawString.charAt(i) == '\t')
            {
                jsonStringBuilder.append('\\');
                jsonStringBuilder.append('t');
            }
            else if(rawString.charAt(i) == '\b')
            {
                jsonStringBuilder.append('\\');
                jsonStringBuilder.append('b');
            }
            else if(rawString.charAt(i) == '\f')
            {
                jsonStringBuilder.append('\\');
                jsonStringBuilder.append('f');
            }
            else if(rawString.charAt(i) == '"')
            {
                jsonStringBuilder.append('\\');
                jsonStringBuilder.append('"');
            }
            else if(rawString.charAt(i) == '\\')
            {
                jsonStringBuilder.append('\\');
                jsonStringBuilder.append('\\');
            }
            else
            {
                jsonStringBuilder.append(rawString.charAt(i));
            }
        }
        return jsonStringBuilder.toString();
    }
}
