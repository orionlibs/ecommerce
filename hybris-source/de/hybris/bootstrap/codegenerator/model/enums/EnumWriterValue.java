package de.hybris.bootstrap.codegenerator.model.enums;

public class EnumWriterValue
{
    private final String code;
    private String javadoc;


    public EnumWriterValue(String code)
    {
        this.code = code;
    }


    public String getJavadoc()
    {
        return this.javadoc;
    }


    public void setJavadoc(String javadoc)
    {
        this.javadoc = javadoc;
    }


    public String getCode()
    {
        return this.code;
    }


    public String toString()
    {
        return this.code;
    }
}
