package de.hybris.bootstrap.beangenerator.definitions.model;

import de.hybris.bootstrap.beangenerator.ClassNameUtil;

public abstract class ClassNameAware extends ExtensionNameAware
{
    protected final ClassNamePrototype className;
    protected String templatePath;


    public ClassNameAware(String extensionName, String className)
    {
        super(extensionName);
        if(className == null)
        {
            throw new IllegalArgumentException("Class name cannot be null");
        }
        this.className = ClassNameUtil.toPrototype(className);
    }


    public ClassNamePrototype getClassName()
    {
        return this.className;
    }


    public void setTemplatePath(String templateName)
    {
        this.templatePath = templateName;
    }


    public String getTemplatePath()
    {
        return this.templatePath;
    }
}
