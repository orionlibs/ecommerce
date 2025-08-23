package de.hybris.platform.hac.data.dto;

import java.util.List;

public class CreatorParameterData
{
    private String defaultValue;
    private List<String> possibleValues;


    public CreatorParameterData(String defaultValue, List<String> possibleValues)
    {
        this.defaultValue = defaultValue;
        this.possibleValues = possibleValues;
    }


    public String getDefaultValue()
    {
        return this.defaultValue;
    }


    public List<String> getPossibleValues()
    {
        return this.possibleValues;
    }


    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    public void setPossibleValues(List<String> possibleValues)
    {
        this.possibleValues = possibleValues;
    }
}
