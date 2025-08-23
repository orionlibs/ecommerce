package de.hybris.platform.cms2.version.converter.customattribute;

public abstract class CMSVersionCustomAttribute
{
    public static final String DELIMITER = "__::__";


    public abstract String toData();


    public abstract void init(String paramString);


    public String getDelimiter()
    {
        return "__::__";
    }


    public String toString()
    {
        return toData();
    }
}
