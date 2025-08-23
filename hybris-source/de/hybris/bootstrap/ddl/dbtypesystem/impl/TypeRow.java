package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.Timestamp;
import java.util.Objects;

public class TypeRow implements Row
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
    private String inheritancepathstring;
    private Long supertypepk;
    private String itemjndiname;
    private String jaloclassname;
    private Boolean removable;
    private Integer itemtypecode;
    private Boolean singleton;
    private Boolean propertytablestatus;
    private Long sourcetype;
    private Long sourceattribute;
    private Boolean jaloonly;
    private Long orderingattribute;
    private Boolean dynamic;
    private Long hmcicon;
    private Long targetattribute;
    private Long localizationattribute;
    private Integer loghmcchanges;
    private Boolean autocreate;
    private String extensionname;
    private String catalogversionattributequali;
    private Boolean systemtype;
    private Boolean localized;
    private Boolean generate;
    private Long comparationattribute;
    private String uniquekeyattributequalifier;
    private Long targettype;
    private Boolean catalogitemtype;
    private byte[] defaultvalue;
    private Boolean legacyPersistence;


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


    public String getInheritancepathstring()
    {
        return this.inheritancepathstring;
    }


    public void setInheritancepathstring(String inheritancepathstring)
    {
        this.inheritancepathstring = inheritancepathstring;
    }


    public Long getSupertypepk()
    {
        return this.supertypepk;
    }


    public void setSupertypepk(Long supertypepk)
    {
        this.supertypepk = supertypepk;
    }


    public String getItemjndiname()
    {
        return this.itemjndiname;
    }


    public void setItemjndiname(String itemjndiname)
    {
        this.itemjndiname = itemjndiname;
    }


    public String getJaloclassname()
    {
        return this.jaloclassname;
    }


    public void setJaloclassname(String jaloclassname)
    {
        this.jaloclassname = jaloclassname;
    }


    public Boolean getRemovable()
    {
        return this.removable;
    }


    public void setRemovable(Boolean removable)
    {
        this.removable = removable;
    }


    public Integer getItemtypecode()
    {
        return this.itemtypecode;
    }


    public void setItemtypecode(Integer itemtypecode)
    {
        this.itemtypecode = itemtypecode;
    }


    public Boolean getSingleton()
    {
        return this.singleton;
    }


    public void setSingleton(Boolean singleton)
    {
        this.singleton = singleton;
    }


    public Boolean getPropertytablestatus()
    {
        return this.propertytablestatus;
    }


    public void setPropertytablestatus(Boolean propertytablestatus)
    {
        this.propertytablestatus = propertytablestatus;
    }


    public Long getSourcetype()
    {
        return this.sourcetype;
    }


    public void setSourcetype(Long sourcetype)
    {
        this.sourcetype = sourcetype;
    }


    public Long getSourceattribute()
    {
        return this.sourceattribute;
    }


    public void setSourceattribute(Long sourceattribute)
    {
        this.sourceattribute = sourceattribute;
    }


    public Boolean getJaloonly()
    {
        return this.jaloonly;
    }


    public void setJaloonly(Boolean jaloonly)
    {
        this.jaloonly = jaloonly;
    }


    public Long getOrderingattribute()
    {
        return this.orderingattribute;
    }


    public void setOrderingattribute(Long orderingattribute)
    {
        this.orderingattribute = orderingattribute;
    }


    public Boolean getDynamic()
    {
        return this.dynamic;
    }


    public void setDynamic(Boolean dynamic)
    {
        this.dynamic = dynamic;
    }


    public Long getHmcicon()
    {
        return this.hmcicon;
    }


    public void setHmcicon(Long hmcicon)
    {
        this.hmcicon = hmcicon;
    }


    public Long getTargetattribute()
    {
        return this.targetattribute;
    }


    public void setTargetattribute(Long targetattribute)
    {
        this.targetattribute = targetattribute;
    }


    public Long getLocalizationattribute()
    {
        return this.localizationattribute;
    }


    public void setLocalizationattribute(Long localizationattribute)
    {
        this.localizationattribute = localizationattribute;
    }


    public Integer getLoghmcchanges()
    {
        return this.loghmcchanges;
    }


    public void setLoghmcchanges(Integer loghmcchanges)
    {
        this.loghmcchanges = loghmcchanges;
    }


    public Boolean getAutocreate()
    {
        return this.autocreate;
    }


    public void setAutocreate(Boolean autocreate)
    {
        this.autocreate = autocreate;
    }


    public String getExtensionname()
    {
        return this.extensionname;
    }


    public void setExtensionname(String extensionname)
    {
        this.extensionname = extensionname;
    }


    public String getCatalogversionattributequali()
    {
        return this.catalogversionattributequali;
    }


    public void setCatalogversionattributequali(String catalogversionattributequali)
    {
        this.catalogversionattributequali = catalogversionattributequali;
    }


    public Boolean getSystemtype()
    {
        return this.systemtype;
    }


    public void setSystemtype(Boolean systemtype)
    {
        this.systemtype = systemtype;
    }


    public Boolean getLocalized()
    {
        return this.localized;
    }


    public void setLocalized(Boolean localized)
    {
        this.localized = localized;
    }


    public Boolean getGenerate()
    {
        return this.generate;
    }


    public void setGenerate(Boolean generate)
    {
        this.generate = generate;
    }


    public Long getComparationattribute()
    {
        return this.comparationattribute;
    }


    public void setComparationattribute(Long comparationattribute)
    {
        this.comparationattribute = comparationattribute;
    }


    public String getUniquekeyattributequalifier()
    {
        return this.uniquekeyattributequalifier;
    }


    public void setUniquekeyattributequalifier(String uniquekeyattributequalifier)
    {
        this.uniquekeyattributequalifier = uniquekeyattributequalifier;
    }


    public Long getTargettype()
    {
        return this.targettype;
    }


    public void setTargettype(Long targettype)
    {
        this.targettype = targettype;
    }


    public Boolean getCatalogitemtype()
    {
        return this.catalogitemtype;
    }


    public void setCatalogitemtype(Boolean catalogitemtype)
    {
        this.catalogitemtype = catalogitemtype;
    }


    public byte[] getDefaultvalue()
    {
        return this.defaultvalue;
    }


    public void setDefaultvalue(byte[] defaultvalue)
    {
        this.defaultvalue = defaultvalue;
    }


    public Boolean getLegacyPersistence()
    {
        return this.legacyPersistence;
    }


    public void setLegacyPersistence(Boolean legacyPersistence)
    {
        this.legacyPersistence = legacyPersistence;
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
            case "inheritancepathstring":
                return getInheritancepathstring();
            case "supertypepk":
                return getSupertypepk();
            case "itemjndiname":
                return getItemjndiname();
            case "jaloclassname":
                return getJaloclassname();
            case "removable":
                return getRemovable();
            case "itemtypecode":
                return getItemtypecode();
            case "singleton":
                return getSingleton();
            case "propertytablestatus":
                return getPropertytablestatus();
            case "p_sourcetype":
                return getSourcetype();
            case "p_sourceattribute":
                return getSourceattribute();
            case "p_jaloonly":
                return getJaloonly();
            case "p_orderingattribute":
                return getOrderingattribute();
            case "p_dynamic":
                return getDynamic();
            case "p_hmcicon":
                return getHmcicon();
            case "p_targetattribute":
                return getTargetattribute();
            case "p_localizationattribute":
                return getLocalizationattribute();
            case "p_loghmcchanges":
                return getLoghmcchanges();
            case "p_autocreate":
                return getAutocreate();
            case "p_extensionname":
                return getExtensionname();
            case "p_catalogversionattributequali":
                return getCatalogversionattributequali();
            case "p_systemtype":
                return getSystemtype();
            case "p_localized":
                return getLocalized();
            case "p_generate":
                return getGenerate();
            case "p_comparationattribute":
                return getComparationattribute();
            case "p_uniquekeyattributequalifier":
                return getUniquekeyattributequalifier();
            case "p_targettype":
                return getTargettype();
            case "p_catalogitemtype":
                return getCatalogitemtype();
            case "p_defaultvalue":
                return getDefaultvalue();
            case "p_legacypersistence":
                return getLegacyPersistence();
        }
        throw new IllegalArgumentException("Invalid columnName: " + columnName);
    }
}
