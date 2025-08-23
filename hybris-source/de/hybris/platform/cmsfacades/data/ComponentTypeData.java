package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ComponentTypeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String category;
    private String name;
    private Map<String, String> nameWithLocale;
    private String i18nKey;
    private String type;
    private List<ComponentTypeAttributeData> attributes;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCategory(String category)
    {
        this.category = category;
    }


    public String getCategory()
    {
        return this.category;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setNameWithLocale(Map<String, String> nameWithLocale)
    {
        this.nameWithLocale = nameWithLocale;
    }


    public Map<String, String> getNameWithLocale()
    {
        return this.nameWithLocale;
    }


    public void setI18nKey(String i18nKey)
    {
        this.i18nKey = i18nKey;
    }


    public String getI18nKey()
    {
        return this.i18nKey;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
    {
        return this.type;
    }


    public void setAttributes(List<ComponentTypeAttributeData> attributes)
    {
        this.attributes = attributes;
    }


    public List<ComponentTypeAttributeData> getAttributes()
    {
        return this.attributes;
    }
}
