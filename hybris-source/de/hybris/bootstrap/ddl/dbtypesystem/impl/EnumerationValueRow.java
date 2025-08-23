package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.Timestamp;
import java.util.Objects;

public class EnumerationValueRow implements Row
{
    private Long hjmpts;
    private Long typepkstring;
    private Long pk;
    private Timestamp createdts;
    private Timestamp modifiedts;
    private Long ownerpkstring;
    private Long aclts;
    private Long propts;
    private String code;
    private Boolean editable;
    private Integer sequencenumber;
    private String codelowercase;
    private Long icon;
    private String extensionname;


    public Long getHjmpts()
    {
        return this.hjmpts;
    }


    public void setHjmpts(Long hjmpts)
    {
        this.hjmpts = hjmpts;
    }


    public Long getTypepkstring()
    {
        return this.typepkstring;
    }


    public void setTypepkstring(Long typepkstring)
    {
        this.typepkstring = typepkstring;
    }


    public Long getPk()
    {
        return this.pk;
    }


    public void setPk(Long pk)
    {
        this.pk = pk;
    }


    public Timestamp getCreatedts()
    {
        return this.createdts;
    }


    public void setCreatedts(Timestamp createdts)
    {
        this.createdts = createdts;
    }


    public Timestamp getModifiedts()
    {
        return this.modifiedts;
    }


    public void setModifiedts(Timestamp modifiedts)
    {
        this.modifiedts = modifiedts;
    }


    public Long getOwnerpkstring()
    {
        return this.ownerpkstring;
    }


    public void setOwnerpkstring(Long ownerpkstring)
    {
        this.ownerpkstring = ownerpkstring;
    }


    public Long getAclts()
    {
        return this.aclts;
    }


    public void setAclts(Long aclts)
    {
        this.aclts = aclts;
    }


    public Long getPropts()
    {
        return this.propts;
    }


    public void setPropts(Long propts)
    {
        this.propts = propts;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public Boolean getEditable()
    {
        return this.editable;
    }


    public void setEditable(Boolean editable)
    {
        this.editable = editable;
    }


    public Integer getSequencenumber()
    {
        return this.sequencenumber;
    }


    public void setSequencenumber(Integer sequencenumber)
    {
        this.sequencenumber = sequencenumber;
    }


    public String getCodelowercase()
    {
        return this.codelowercase;
    }


    public void setCodelowercase(String codelowercase)
    {
        this.codelowercase = codelowercase;
    }


    public Long getIcon()
    {
        return this.icon;
    }


    public void setIcon(Long icon)
    {
        this.icon = icon;
    }


    public String getExtensionname()
    {
        return this.extensionname;
    }


    public void setExtensionname(String extensionname)
    {
        this.extensionname = extensionname;
    }


    public Object getValue(String columnName)
    {
        Objects.requireNonNull(columnName);
        switch(columnName.toLowerCase(LocaleHelper.getPersistenceLocale()))
        {
            case "hjmpts":
                return getHjmpts();
            case "typepkstring":
                return getTypepkstring();
            case "pk":
                return getPk();
            case "createdts":
                return getCreatedts();
            case "modifiedts":
                return getModifiedts();
            case "ownerpkstring":
                return getOwnerpkstring();
            case "aclts":
                return getAclts();
            case "propts":
                return getPropts();
            case "code":
                return getCode();
            case "editable":
                return getEditable();
            case "sequencenumber":
                return getSequencenumber();
            case "codelowercase":
                return getCodelowercase();
            case "p_icon":
                return getIcon();
            case "p_extensionname":
                return getExtensionname();
        }
        throw new IllegalArgumentException("columnName");
    }
}
