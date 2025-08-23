package de.hybris.platform.hac.data.dto;

import java.util.List;
import java.util.Map;

public class ExtensionsData
{
    List<ExtensionData> extensions;
    Map<String, Boolean> extensionNames;


    public ExtensionsData(List<ExtensionData> extensions, Map<String, Boolean> extensionNames)
    {
        this.extensions = extensions;
        this.extensionNames = extensionNames;
    }


    public List<ExtensionData> getExtensions()
    {
        return this.extensions;
    }


    public void setExtensions(List<ExtensionData> extensions)
    {
        this.extensions = extensions;
    }


    public Map<String, Boolean> getExtensionNames()
    {
        return this.extensionNames;
    }


    public void setExtensionNames(Map<String, Boolean> extensionNames)
    {
        this.extensionNames = extensionNames;
    }
}
