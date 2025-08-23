package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.util.LocaleHelper;

public class PropsRow implements Row
{
    private Long hjmpts;
    private String value1;
    private Long itempk;
    private Long itemtypepk;
    private String realname;
    private Integer type1;
    private String valuestring1;
    private Long langpk;
    private String name;


    public Object getHjmpts()
    {
        return this.hjmpts;
    }


    public Object getValue1()
    {
        return this.value1;
    }


    public Long getItempk()
    {
        return this.itempk;
    }


    public Long getItemtypepk()
    {
        return this.itemtypepk;
    }


    public String getRealname()
    {
        return this.realname;
    }


    public Integer getType1()
    {
        return this.type1;
    }


    public String getValuestring1()
    {
        return this.valuestring1;
    }


    public Long getLangpk()
    {
        return this.langpk;
    }


    public String getName()
    {
        return this.name;
    }


    public void setHjmpts(Long hjmpts)
    {
        this.hjmpts = hjmpts;
    }


    public void setValue1(String value1)
    {
        this.value1 = value1;
    }


    public void setItempk(Long itempk)
    {
        this.itempk = itempk;
    }


    public void setItemtypepk(Long itemtypepk)
    {
        this.itemtypepk = itemtypepk;
    }


    public void setRealname(String realname)
    {
        this.realname = realname;
    }


    public void setType1(Integer type1)
    {
        this.type1 = type1;
    }


    public void setValuestring1(String valuestring1)
    {
        this.valuestring1 = valuestring1;
    }


    public void setLangpk(Long langpk)
    {
        this.langpk = langpk;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public Object getValue(String columnName)
    {
        switch(columnName.toLowerCase(LocaleHelper.getPersistenceLocale()))
        {
            case "hjmpts":
                return getHjmpts();
            case "value1":
                return getValue1();
            case "itempk":
                return getItempk();
            case "itemtypepk":
                return getItemtypepk();
            case "realname":
                return getRealname();
            case "type1":
                return getType1();
            case "valuestring1":
                return getValuestring1();
            case "langpk":
                return getLangpk();
            case "name":
                return getName();
        }
        throw new IllegalArgumentException("columnName");
    }
}
