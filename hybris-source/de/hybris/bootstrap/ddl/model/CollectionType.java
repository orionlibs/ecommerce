package de.hybris.bootstrap.ddl.model;

public class CollectionType
{
    private long pk;
    private long elementTypePk;
    private String internalCode;
    private String internalCodeLowerCase;
    private long typePkString;


    public long getPk()
    {
        return this.pk;
    }


    public void setPk(long pk)
    {
        this.pk = pk;
    }


    public long getElementTypePk()
    {
        return this.elementTypePk;
    }


    public void setElementTypePk(long elementTypePk)
    {
        this.elementTypePk = elementTypePk;
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


    public long getTypePkString()
    {
        return this.typePkString;
    }


    public void setTypePkString(long typePkString)
    {
        this.typePkString = typePkString;
    }
}
