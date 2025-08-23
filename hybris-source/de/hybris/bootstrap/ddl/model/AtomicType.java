package de.hybris.bootstrap.ddl.model;

public class AtomicType
{
    private long pk;
    private long typePkString;
    private String internalCode;
    private String internalCodeLowerCase;
    private String inheritancePathString;
    private long superTypePk;
    private String javaClassName;


    public long getPk()
    {
        return this.pk;
    }


    public void setPk(long pk)
    {
        this.pk = pk;
    }


    public long getTypePkString()
    {
        return this.typePkString;
    }


    public void setTypePkString(long typePkString)
    {
        this.typePkString = typePkString;
    }


    public String getInternalCode()
    {
        return this.internalCode;
    }


    public void setInternalCode(String internalCode)
    {
        this.internalCode = internalCode;
    }


    public String getInternalCodeLowerCase()
    {
        return this.internalCodeLowerCase;
    }


    public void setInternalCodeLowerCase(String internalCodeLowerCase)
    {
        this.internalCodeLowerCase = internalCodeLowerCase;
    }


    public String getInheritancePathString()
    {
        return this.inheritancePathString;
    }


    public void setInheritancePathString(String inheritancePathString)
    {
        this.inheritancePathString = inheritancePathString;
    }


    public long getSuperTypePk()
    {
        return this.superTypePk;
    }


    public void setSuperTypePk(long superTypePk)
    {
        this.superTypePk = superTypePk;
    }


    public String getJavaClassName()
    {
        return this.javaClassName;
    }


    public void setJavaClassName(String javaClassName)
    {
        this.javaClassName = javaClassName;
    }
}
