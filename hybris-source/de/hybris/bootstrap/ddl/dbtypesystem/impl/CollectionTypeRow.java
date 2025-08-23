package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.Timestamp;
import java.util.Objects;

public class CollectionTypeRow implements Row
{
    private Long hjmpts;
    private Long typepkstring;
    private Long pk;
    private Timestamp createdts;
    private Timestamp modifiedts;
    private Long ownerpkstring;
    private Long aclts;
    private Long propts;
    private String internalcode;
    private String internalcodelowercase;
    private Long elementtypepk;
    private Integer typeofcollection;
    private Boolean generate;
    private byte[] defaultvalue;
    private String extensionname;
    private Boolean autocreate;


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


    public String getInternalcode()
    {
        return this.internalcode;
    }


    public void setInternalcode(String internalcode)
    {
        this.internalcode = internalcode;
    }


    public String getInternalcodelowercase()
    {
        return this.internalcodelowercase;
    }


    public void setInternalcodelowercase(String internalcodelowercase)
    {
        this.internalcodelowercase = internalcodelowercase;
    }


    public Long getElementtypepk()
    {
        return this.elementtypepk;
    }


    public void setElementtypepk(Long elementtypepk)
    {
        this.elementtypepk = elementtypepk;
    }


    public Integer getTypeofcollection()
    {
        return this.typeofcollection;
    }


    public void setTypeofcollection(Integer typeofcollection)
    {
        this.typeofcollection = typeofcollection;
    }


    public Boolean getGenerate()
    {
        return this.generate;
    }


    public void setGenerate(Boolean generate)
    {
        this.generate = generate;
    }


    public byte[] getDefaultvalue()
    {
        return this.defaultvalue;
    }


    public void setDefaultvalue(byte[] defaultvalue)
    {
        this.defaultvalue = defaultvalue;
    }


    public String getExtensionname()
    {
        return this.extensionname;
    }


    public void setExtensionname(String extensionname)
    {
        this.extensionname = extensionname;
    }


    public Boolean getAutocreate()
    {
        return this.autocreate;
    }


    public void setAutocreate(Boolean autocreate)
    {
        this.autocreate = autocreate;
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
            case "internalcode":
                return getInternalcode();
            case "internalcodelowercase":
                return getInternalcodelowercase();
            case "elementtypepk":
                return getElementtypepk();
            case "typeofcollection":
                return getTypeofcollection();
            case "p_generate":
                return getGenerate();
            case "p_defaultvalue":
                return getDefaultvalue();
            case "p_extensionname":
                return getExtensionname();
            case "p_autocreate":
                return getAutocreate();
        }
        throw new IllegalArgumentException("columnName");
    }
}
