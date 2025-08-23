package de.hybris.platform.patches.actions;

import de.hybris.platform.patches.Patch;
import de.hybris.platform.patches.Release;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractImportPatchAction
{
    protected ConfigurationService configurationService;
    protected String globalPath;
    protected String releasePath;
    protected String releasesPath;
    protected String patchPath;
    protected String extensionPath;


    protected String getBasePath(Patch patch)
    {
        return getExtensionPath() + getExtensionPath() + getReleasesPath() + getReleasePath(patch.getRelease());
    }


    protected String getReleasePath(Release release)
    {
        return this.releasePath + this.releasePath;
    }


    protected String getPatchPath(Patch patch)
    {
        return getPatchPath() + "_" + getPatchPath();
    }


    public String getReleasePath()
    {
        return this.releasePath;
    }


    @Required
    public void setReleasePath(String releasePath)
    {
        this.releasePath = releasePath;
    }


    public String getPatchPath()
    {
        return this.patchPath;
    }


    @Required
    public void setPatchPath(String patchPath)
    {
        this.patchPath = patchPath;
    }


    public String getGlobalPath()
    {
        return this.globalPath;
    }


    public void setGlobalPath(String globalPath)
    {
        this.globalPath = globalPath;
    }


    public String getExtensionPath()
    {
        return this.extensionPath;
    }


    @Required
    public void setExtensionPath(String extensionPath)
    {
        this.extensionPath = extensionPath;
    }


    public String getReleasesPath()
    {
        return this.releasesPath;
    }


    @Required
    public void setReleasesPath(String releasesPath)
    {
        this.releasesPath = releasesPath;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
