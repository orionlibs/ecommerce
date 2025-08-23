package de.hybris.bootstrap.typesystem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class YExtension extends YNamespace
{
    private final String extensionName;
    private final Set<String> required;


    public YExtension(YTypeSystem rootSystem, String extensionName, Set<String> requiredExtensionNames)
    {
        super(rootSystem);
        this.extensionName = extensionName;
        if(requiredExtensionNames != null)
        {
            this.required = new HashSet<>();
            for(String extName : requiredExtensionNames)
            {
                this.required.add(extName.toLowerCase(Locale.ENGLISH));
            }
        }
        else
        {
            this.required = Collections.EMPTY_SET;
        }
    }


    public String toString()
    {
        return "((" + getExtensionName() + "))";
    }


    public Set<String> getRequiredExtensionNames()
    {
        return this.required;
    }


    public boolean requires(String extName)
    {
        return this.required.contains(extName.toLowerCase(Locale.ENGLISH));
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }
}
