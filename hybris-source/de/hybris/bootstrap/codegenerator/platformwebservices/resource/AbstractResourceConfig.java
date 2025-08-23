package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.ResourceConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import java.util.Collection;

@Deprecated(since = "1818", forRemoval = true)
public abstract class AbstractResourceConfig implements ResourceConfig
{
    protected WebservicesConfig provider = null;
    private boolean getSupport = true;
    private boolean postSupport = false;
    private boolean putSupport = false;
    private boolean deleteSupport = false;
    private String resourcePackage = null;
    private String resourceClassName = null;
    private String resourceClassSimpleName = null;
    private final DtoConfig dtoConfig;


    public AbstractResourceConfig(DtoConfig dtoConfig, String resourcePackage, WebservicesConfig provider)
    {
        this.dtoConfig = dtoConfig;
        this.provider = provider;
        this.resourcePackage = resourcePackage;
    }


    public boolean isPostSupport()
    {
        return this.postSupport;
    }


    public void setPostSupport(boolean isPostSupport)
    {
        this.postSupport = isPostSupport;
    }


    public boolean isPutSupport()
    {
        return this.putSupport;
    }


    public void setPutSupport(boolean isPutSupport)
    {
        this.putSupport = isPutSupport;
    }


    public boolean isGetSupport()
    {
        return this.getSupport;
    }


    public void setGetSupport(boolean isGetSupport)
    {
        this.getSupport = isGetSupport;
    }


    public boolean isDeleteSupport()
    {
        return this.deleteSupport;
    }


    public void setDeleteSupport(boolean isDeleteSupport)
    {
        this.deleteSupport = isDeleteSupport;
    }


    public String getClassName()
    {
        return this.resourceClassName;
    }


    public void setResourceClassName(String className)
    {
        this.resourceClassName = className;
        this.resourceClassSimpleName = this.resourceClassName.substring(this.resourceClassName.lastIndexOf('.') + 1);
    }


    public String getSimpleClassName()
    {
        return this.resourceClassSimpleName;
    }


    public String getPackageName()
    {
        return this.resourcePackage;
    }


    public void setResourcePackage(String resourcePackage)
    {
        this.resourcePackage = resourcePackage;
    }


    public abstract Collection<ResourceConfig> getSubResources();


    protected WebservicesConfig getResourceConfigProvider()
    {
        return this.provider;
    }


    public DtoConfig getDTOConfig()
    {
        return this.dtoConfig;
    }
}
