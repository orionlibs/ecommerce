package de.hybris.platform.platformbackoffice.services.converters;

import de.hybris.platform.core.model.ItemModel;

public class SavedQueryValue
{
    private String value;
    private ItemModel valueRef;
    private String localeCode;


    public SavedQueryValue(String localeCode, String value, ItemModel valueRef)
    {
        this.localeCode = localeCode;
        this.value = value;
        this.valueRef = valueRef;
    }


    public SavedQueryValue(String localeCode, String value)
    {
        this.localeCode = localeCode;
        this.value = value;
    }


    public SavedQueryValue(ItemModel valueRef)
    {
        this.valueRef = valueRef;
    }


    public SavedQueryValue(String localeCode, ItemModel valueRef)
    {
        this.localeCode = localeCode;
        this.valueRef = valueRef;
    }


    public SavedQueryValue(String value)
    {
        this.value = value;
    }


    public String getLocaleCode()
    {
        return this.localeCode;
    }


    public String getValue()
    {
        return this.value;
    }


    public ItemModel getValueRef()
    {
        return this.valueRef;
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
        SavedQueryValue that = (SavedQueryValue)o;
        if((this.value != null) ? !this.value.equals(that.value) : (that.value != null))
        {
            return false;
        }
        if((this.valueRef != null) ? !this.valueRef.equals(that.valueRef) : (that.valueRef != null))
        {
            return false;
        }
        return (this.localeCode != null) ? this.localeCode.equals(that.localeCode) : ((that.localeCode == null));
    }


    public int hashCode()
    {
        int result = (this.value != null) ? this.value.hashCode() : 0;
        result = 31 * result + ((this.valueRef != null) ? this.valueRef.hashCode() : 0);
        result = 31 * result + ((this.localeCode != null) ? this.localeCode.hashCode() : 0);
        return result;
    }
}
