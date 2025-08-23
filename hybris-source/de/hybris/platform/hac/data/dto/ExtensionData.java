package de.hybris.platform.hac.data.dto;

import java.util.List;

public class ExtensionData
{
    private String creatorName;
    private List<CreatorParameterData> creatorParameters;


    public ExtensionData(String creatorName, List<CreatorParameterData> creatorParameters)
    {
        this.creatorName = creatorName;
        this.creatorParameters = creatorParameters;
    }


    public String getCreatorName()
    {
        return this.creatorName;
    }


    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
    }


    public List<CreatorParameterData> getCreatorParameters()
    {
        return this.creatorParameters;
    }


    public void setCreatorParameters(List<CreatorParameterData> creatorParameters)
    {
        this.creatorParameters = creatorParameters;
    }
}
