package de.hybris.bootstrap.typesystem;

import java.util.HashMap;
import java.util.Map;

public class OverridenItemsXml
{
    private final Map<String, String> extensionToFileName = new HashMap<>();


    public static OverridenItemsXml empty()
    {
        return new OverridenItemsXml();
    }


    public void override(String extensionName, String fileNameToUse)
    {
        this.extensionToFileName.put(extensionName, fileNameToUse);
    }


    public boolean isOverriden(String extensionName)
    {
        return this.extensionToFileName.containsKey(extensionName);
    }


    public String getOverridingFileName(String extensionName)
    {
        return this.extensionToFileName.get(extensionName);
    }


    public static OverridenItemsXml forExtension(String extensionName, String overridingFile)
    {
        OverridenItemsXml result = empty();
        result.override(extensionName, overridingFile);
        return result;
    }
}
