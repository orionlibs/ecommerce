package de.hybris.bootstrap.ddl.model;

public class MapType
{
    private long pk;
    private long argumentTypePk;
    private long returnTypePk;
    private String internalCode;
    private String internalCodeLowerCase;


    public long getPk()
    {
        return this.pk;
    }


    public void setPk(long pk)
    {
        this.pk = pk;
    }


    public long getArgumentTypePk()
    {
        return this.argumentTypePk;
    }


    public void setArgumentTypePk(long argumentTypePk)
    {
        this.argumentTypePk = argumentTypePk;
    }


    public long getReturnTypePk()
    {
        return this.returnTypePk;
    }


    public void setReturnTypePk(long returnTypePk)
    {
        this.returnTypePk = returnTypePk;
    }


    public String getInternalCode()
    {
        return this.internalCode;
    }


    public void setInternalCode(String internalCode)
    {
        this.internalCode = internalCode;
    }


    public String getInternalCodeLoweCase()
    {
        return this.internalCodeLowerCase;
    }


    public void setInternalCodeLowerCase(String internalCodeLowerCase)
    {
        this.internalCodeLowerCase = internalCodeLowerCase;
    }
}
