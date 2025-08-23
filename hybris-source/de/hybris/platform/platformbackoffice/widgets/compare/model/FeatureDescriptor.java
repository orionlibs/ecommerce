package de.hybris.platform.platformbackoffice.widgets.compare.model;

import java.util.Objects;

public class FeatureDescriptor
{
    private final String code;
    private String classificationAttributeCode;
    private final String name;
    private String classificationCode;
    private boolean canRead;
    private boolean canWrite;


    public FeatureDescriptor(String code, String classificationCode, String name, String classificationAttributeCode)
    {
        this(code, classificationCode, name);
        this.classificationAttributeCode = classificationAttributeCode;
    }


    @Deprecated(since = "1905", forRemoval = true)
    public FeatureDescriptor(String code, String classificationCode, String name)
    {
        this.code = code;
        this.classificationCode = classificationCode;
        this.name = name;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getClassificationCode()
    {
        return this.classificationCode;
    }


    public String getName()
    {
        return this.name;
    }


    public String getClassificationAttributeCode()
    {
        return this.classificationAttributeCode;
    }


    public void setClassificationCode(String classificationCode)
    {
        this.classificationCode = classificationCode;
    }


    public boolean canRead()
    {
        return this.canRead;
    }


    public void setCanRead(boolean canRead)
    {
        this.canRead = canRead;
    }


    public boolean canWrite()
    {
        return this.canWrite;
    }


    public void setCanWrite(boolean canWrite)
    {
        this.canWrite = canWrite;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        FeatureDescriptor that = (FeatureDescriptor)o;
        return (Objects.equals(this.code, that.code) && Objects.equals(this.name, that.name) && Objects.equals(this.classificationCode, that.classificationCode) && Objects.equals(this.classificationAttributeCode, that.classificationAttributeCode));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.code, this.name, this.classificationCode, this.classificationAttributeCode});
    }
}
