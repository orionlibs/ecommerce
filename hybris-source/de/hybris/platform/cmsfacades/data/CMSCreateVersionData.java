package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public abstract class CMSCreateVersionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Boolean createVersion;
    private String versionLabel;


    public void setCreateVersion(Boolean createVersion)
    {
        this.createVersion = createVersion;
    }


    public Boolean getCreateVersion()
    {
        return this.createVersion;
    }


    public void setVersionLabel(String versionLabel)
    {
        this.versionLabel = versionLabel;
    }


    public String getVersionLabel()
    {
        return this.versionLabel;
    }
}
