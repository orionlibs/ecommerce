package de.hybris.platform.persistence.audit.payload.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalizedValue
{
    private String language;
    private List<String> value;


    private LocalizedValue()
    {
    }


    public LocalizedValue(String language, String value)
    {
        this.language = language;
        this.value = new ArrayList<>();
        this.value.add(value);
    }


    public LocalizedValue(String language, List<String> value)
    {
        this.language = language;
        this.value = value;
    }


    public String getLanguage()
    {
        return this.language;
    }


    public void setLanguage(String language)
    {
        this.language = language;
    }


    public List<String> getValue()
    {
        return this.value;
    }


    public void setValue(List<String> value)
    {
        this.value = value;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof LocalizedValue))
        {
            return false;
        }
        LocalizedValue that = (LocalizedValue)o;
        return (Objects.equals(this.language, that.language) &&
                        Objects.equals(this.value, that.value));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.language, this.value});
    }
}
