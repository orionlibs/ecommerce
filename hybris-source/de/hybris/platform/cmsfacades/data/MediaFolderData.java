package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class MediaFolderData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String qualifier;


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }
}
