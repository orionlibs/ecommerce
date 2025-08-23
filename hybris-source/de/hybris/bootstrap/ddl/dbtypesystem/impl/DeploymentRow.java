package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import java.util.Locale;
import java.util.Objects;

public class DeploymentRow implements Row
{
    private Long hjmpts;
    private String supername;
    private String propstablename;
    private Integer typecode;
    private Integer modifiers;
    private String tablename;
    private String packagename;
    private String name;
    private String extensionname;
    private String typesystemname;


    public Long getHjmpts()
    {
        return this.hjmpts;
    }


    public void setHjmpts(Long hjmpts)
    {
        this.hjmpts = hjmpts;
    }


    public String getSupername()
    {
        return this.supername;
    }


    public void setSupername(String supername)
    {
        this.supername = supername;
    }


    public String getPropstablename()
    {
        return this.propstablename;
    }


    public void setPropstablename(String propstablename)
    {
        this.propstablename = propstablename;
    }


    public Integer getTypecode()
    {
        return this.typecode;
    }


    public void setTypecode(Integer typecode)
    {
        this.typecode = typecode;
    }


    public Integer getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(Integer modifiers)
    {
        this.modifiers = modifiers;
    }


    public String getTablename()
    {
        return this.tablename;
    }


    public void setTablename(String tablename)
    {
        this.tablename = tablename;
    }


    public String getPackagename()
    {
        return this.packagename;
    }


    public void setPackagename(String packagename)
    {
        this.packagename = packagename;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getExtensionname()
    {
        return this.extensionname;
    }


    public void setExtensionname(String extensionname)
    {
        this.extensionname = extensionname;
    }


    public String getTypesystemname()
    {
        return this.typesystemname;
    }


    public void setTypesystemname(String typesystemname)
    {
        this.typesystemname = typesystemname;
    }


    public Object getValue(String columnName)
    {
        Objects.requireNonNull(columnName);
        switch(columnName.toLowerCase(Locale.ROOT))
        {
            case "hjmpts":
                return getHjmpts();
            case "supername":
                return getSupername();
            case "propstablename":
                return getPropstablename();
            case "typecode":
                return getTypecode();
            case "modifiers":
                return getModifiers();
            case "tablename":
                return getTablename();
            case "packagename":
                return getPackagename();
            case "name":
                return getName();
            case "extensionname":
                return getExtensionname();
            case "typesystemname":
                return getTypesystemname();
        }
        throw new IllegalArgumentException("columnName");
    }
}
