package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class ItemTypeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String itemType;
    private String i18nKey;


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public void setI18nKey(String i18nKey)
    {
        this.i18nKey = i18nKey;
    }


    public String getI18nKey()
    {
        return this.i18nKey;
    }
}
