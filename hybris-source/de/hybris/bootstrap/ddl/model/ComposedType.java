package de.hybris.bootstrap.ddl.model;

import java.util.ArrayList;
import java.util.List;

public class ComposedType
{
    private int itemTypeCode;
    private String code;
    private String inheritancePathString;
    private List<ComposedType> superTypes = new ArrayList<>();
    private String extensoinName;
    private String jndiName;
    private long pk;


    public int getItemTypeCode()
    {
        return this.itemTypeCode;
    }


    public void setItemTypeCode(int itemTypeCode)
    {
        this.itemTypeCode = itemTypeCode;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getInheritancePathString()
    {
        return this.inheritancePathString;
    }


    public void setInheritancePathString(String inheritancePathString)
    {
        this.inheritancePathString = inheritancePathString;
    }


    public List<ComposedType> getSuperTypes()
    {
        return this.superTypes;
    }


    public void setSuperTypes(List<ComposedType> superTypes)
    {
        this.superTypes = superTypes;
    }


    public void addSuperType(ComposedType composedType)
    {
        this.superTypes.add(composedType);
    }


    public String getExtensoinName()
    {
        return this.extensoinName;
    }


    public void setExtensoinName(String extensoinName)
    {
        this.extensoinName = extensoinName;
    }


    public String getJndiName()
    {
        return this.jndiName;
    }


    public void setJndiName(String jndiName)
    {
        this.jndiName = jndiName;
    }


    public long getPk()
    {
        return this.pk;
    }


    public void setPk(long pk)
    {
        this.pk = pk;
    }
}
