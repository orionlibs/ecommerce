package de.hybris.bootstrap.codegenerator.platformwebservices.dto;

import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YComposedType;

@Deprecated(since = "1818", forRemoval = true)
public abstract class AbstractDtoConfig implements DtoConfig
{
    protected WebservicesConfig provider = null;
    private YComposedType type = null;
    private String modelClassName = null;
    private String modelClassSimpleName = null;
    private String dtoClassName = null;
    private String dtoClassSimpleName = null;
    private String dtoPackage = null;
    private String singular = null;
    private String plural = null;


    public AbstractDtoConfig(YComposedType type, String modelClassName, String dtoClassName, String plural, WebservicesConfig provider)
    {
        this.type = type;
        setModelClassName(modelClassName);
        setDtoClassName(dtoClassName);
        this.singular = type.getCode();
        this.plural = plural;
        this.provider = provider;
    }


    public WebservicesConfig getConfigProvider()
    {
        return this.provider;
    }


    public void setDtoPackage(String dtoPackage)
    {
        this.dtoPackage = dtoPackage;
    }


    public String getDtoPackage()
    {
        return this.dtoPackage;
    }


    public String getModelClassName()
    {
        return this.modelClassName;
    }


    public final void setModelClassName(String modelClassName)
    {
        this.modelClassName = modelClassName;
        this.modelClassSimpleName = modelClassName.substring(modelClassName.lastIndexOf('.') + 1);
    }


    public String getModelClassSimpleName()
    {
        return this.modelClassSimpleName;
    }


    public String getDtoClassName()
    {
        return this.dtoClassName;
    }


    public final void setDtoClassName(String dtoClassName)
    {
        this.dtoClassName = dtoClassName;
        this.dtoClassSimpleName = dtoClassName.substring(dtoClassName.lastIndexOf('.') + 1);
        this.dtoPackage = dtoClassName.substring(0, dtoClassName.lastIndexOf('.'));
    }


    public String getDtoClassSimpleName()
    {
        return this.dtoClassSimpleName;
    }


    public void setType(YComposedType type)
    {
        this.type = type;
    }


    public YComposedType getType()
    {
        return this.type;
    }


    public String getSingular()
    {
        return this.singular;
    }


    public void setSingular(String singular)
    {
        this.singular = singular;
    }


    public String getPlural()
    {
        return this.plural;
    }


    public void setPlural(String plural)
    {
        this.plural = plural;
    }
}
