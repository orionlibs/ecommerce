package com.hybris.backoffice.excel.importing.data;

public class ClassificationTypeSystemRow
{
    private String fullName;
    private String classificationSystem;
    private String classificationVersion;
    private String classificationClass;
    private String classificationAttribute;
    private boolean localized;
    private String isoCode;
    private boolean mandatory;


    public String getFullName()
    {
        return this.fullName;
    }


    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }


    public String getClassificationSystem()
    {
        return this.classificationSystem;
    }


    public void setClassificationSystem(String classificationSystem)
    {
        this.classificationSystem = classificationSystem;
    }


    public String getClassificationVersion()
    {
        return this.classificationVersion;
    }


    public void setClassificationVersion(String classificationVersion)
    {
        this.classificationVersion = classificationVersion;
    }


    public String getClassificationClass()
    {
        return this.classificationClass;
    }


    public void setClassificationClass(String classificationClass)
    {
        this.classificationClass = classificationClass;
    }


    public String getClassificationAttribute()
    {
        return this.classificationAttribute;
    }


    public void setClassificationAttribute(String classificationAttribute)
    {
        this.classificationAttribute = classificationAttribute;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public void setLocalized(boolean localized)
    {
        this.localized = localized;
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }


    public boolean isMandatory()
    {
        return this.mandatory;
    }


    public void setMandatory(boolean mandatory)
    {
        this.mandatory = mandatory;
    }
}
