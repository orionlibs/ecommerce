package com.hybris.backoffice.excel.data;

import java.io.Serializable;

public class ImpexHeaderValue implements Serializable
{
    private final String name;
    private String qualifier;
    private final boolean unique;
    private boolean mandatory;
    private final String lang;
    private final String dateFormat;
    private final String translator;


    private ImpexHeaderValue(String name, String qualifier, boolean unique, boolean mandatory, String lang, String dateFormat, String translator)
    {
        this.name = name;
        this.qualifier = qualifier;
        this.unique = unique;
        this.lang = lang;
        this.dateFormat = dateFormat;
        this.translator = translator;
        this.mandatory = mandatory;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ImpexHeaderValue(String name, boolean unique, String lang, String dateFormat, String translator)
    {
        this.name = name;
        this.unique = unique;
        this.lang = lang;
        this.dateFormat = dateFormat;
        this.translator = translator;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ImpexHeaderValue(String name, boolean unique, String lang, String dateFormat)
    {
        this(name, unique, lang, dateFormat, null);
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ImpexHeaderValue(String name, boolean unique, String lang)
    {
        this(name, unique, lang, null, null);
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ImpexHeaderValue(String name, boolean unique)
    {
        this(name, unique, null, null, null);
    }


    public String getName()
    {
        return this.name;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public boolean isMandatory()
    {
        return this.mandatory;
    }


    public String getLang()
    {
        return this.lang;
    }


    public String getDateFormat()
    {
        return this.dateFormat;
    }


    public String getTranslator()
    {
        return this.translator;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ImpexHeaderValue that = (ImpexHeaderValue)o;
        if(this.unique != that.unique)
        {
            return false;
        }
        if(this.mandatory != that.mandatory)
        {
            return false;
        }
        if((this.name != null) ? !this.name.equals(that.name) : (that.name != null))
        {
            return false;
        }
        if((this.qualifier != null) ? !this.qualifier.equals(that.qualifier) : (that.qualifier != null))
        {
            return false;
        }
        if((this.lang != null) ? !this.lang.equals(that.lang) : (that.lang != null))
        {
            return false;
        }
        if((this.dateFormat != null) ? !this.dateFormat.equals(that.dateFormat) : (that.dateFormat != null))
        {
            return false;
        }
        return (this.translator != null) ? this.translator.equals(that.translator) : ((that.translator == null));
    }


    public int hashCode()
    {
        int result = (this.name != null) ? this.name.hashCode() : 0;
        result = 31 * result + ((this.qualifier != null) ? this.qualifier.hashCode() : 0);
        result = 31 * result + (this.unique ? 1 : 0);
        result = 31 * result + (this.mandatory ? 1 : 0);
        result = 31 * result + ((this.lang != null) ? this.lang.hashCode() : 0);
        result = 31 * result + ((this.dateFormat != null) ? this.dateFormat.hashCode() : 0);
        result = 31 * result + ((this.translator != null) ? this.translator.hashCode() : 0);
        return result;
    }
}
