package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.Timestamp;
import java.util.Objects;

public class AttributeRow implements Row
{
    private Long hjmpts;
    private Long typepkstring;
    private Long pk;
    private Timestamp createdts;
    private Timestamp modifiedts;
    private Long ownerpkstring;
    private Long aclts;
    private Long propts;
    private String qualifierlowercaseinternal;
    private String qualifierinternal;
    private Long attributetypepk;
    private Long selectiondescriptorpk;
    private Boolean ishidden;
    private String columnname;
    private Long enclosingtypepk;
    private Long persistencetypepk;
    private String inheritancepathstring;
    private Boolean isproperty;
    private String persistencequalifierinternal;
    private Long superattributedescriptorpk;
    private Integer modifiers;
    private String defaultvalueexpression;
    private String externalqualifier;
    private Boolean dontcopy;
    private String relationname;
    private Integer position;
    private Boolean autocreate;
    private String extensionname;
    private String attributehandler;
    private Boolean param;
    private Boolean needrestart;
    private Boolean storeindatabase;
    private Boolean ordered;
    private Long relationtype;
    private Boolean generate;
    private Boolean issource;
    private Boolean unique;
    private String defaultvaluedefinitionstring;
    private byte[] defaultvalue;


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


    public String getQualifierlowercaseinternal()
    {
        return this.qualifierlowercaseinternal;
    }


    public void setQualifierlowercaseinternal(String qualifierlowercaseinternal)
    {
        this.qualifierlowercaseinternal = qualifierlowercaseinternal;
    }


    public String getQualifierinternal()
    {
        return this.qualifierinternal;
    }


    public void setQualifierinternal(String qualifierinternal)
    {
        this.qualifierinternal = qualifierinternal;
    }


    public Long getAttributetypepk()
    {
        return this.attributetypepk;
    }


    public void setAttributetypepk(Long attributetypepk)
    {
        this.attributetypepk = attributetypepk;
    }


    public Long getSelectiondescriptorpk()
    {
        return this.selectiondescriptorpk;
    }


    public void setSelectiondescriptorpk(Long selectiondescriptorpk)
    {
        this.selectiondescriptorpk = selectiondescriptorpk;
    }


    public Boolean getIshidden()
    {
        return this.ishidden;
    }


    public void setIshidden(Boolean ishidden)
    {
        this.ishidden = ishidden;
    }


    public String getColumnname()
    {
        return this.columnname;
    }


    public void setColumnname(String columnname)
    {
        this.columnname = columnname;
    }


    public Long getEnclosingtypepk()
    {
        return this.enclosingtypepk;
    }


    public void setEnclosingtypepk(Long enclosingtypepk)
    {
        this.enclosingtypepk = enclosingtypepk;
    }


    public Long getPersistencetypepk()
    {
        return this.persistencetypepk;
    }


    public void setPersistencetypepk(Long persistencetypepk)
    {
        this.persistencetypepk = persistencetypepk;
    }


    public String getInheritancepathstring()
    {
        return this.inheritancepathstring;
    }


    public void setInheritancepathstring(String inheritancepathstring)
    {
        this.inheritancepathstring = inheritancepathstring;
    }


    public Boolean getIsproperty()
    {
        return this.isproperty;
    }


    public void setIsproperty(Boolean isproperty)
    {
        this.isproperty = isproperty;
    }


    public String getPersistencequalifierinternal()
    {
        return this.persistencequalifierinternal;
    }


    public void setPersistencequalifierinternal(String persistencequalifierinternal)
    {
        this.persistencequalifierinternal = persistencequalifierinternal;
    }


    public Long getSuperattributedescriptorpk()
    {
        return this.superattributedescriptorpk;
    }


    public void setSuperattributedescriptorpk(Long superattributedescriptorpk)
    {
        this.superattributedescriptorpk = superattributedescriptorpk;
    }


    public Integer getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(Integer modifiers)
    {
        this.modifiers = modifiers;
    }


    public String getDefaultvalueexpression()
    {
        return this.defaultvalueexpression;
    }


    public void setDefaultvalueexpression(String defaultvalueexpression)
    {
        this.defaultvalueexpression = defaultvalueexpression;
    }


    public String getExternalqualifier()
    {
        return this.externalqualifier;
    }


    public void setExternalqualifier(String externalqualifier)
    {
        this.externalqualifier = externalqualifier;
    }


    public Boolean getDontcopy()
    {
        return this.dontcopy;
    }


    public void setDontcopy(Boolean dontcopy)
    {
        this.dontcopy = dontcopy;
    }


    public String getRelationname()
    {
        return this.relationname;
    }


    public void setRelationname(String relationname)
    {
        this.relationname = relationname;
    }


    public Integer getPosition()
    {
        return this.position;
    }


    public void setPosition(Integer position)
    {
        this.position = position;
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


    public String getAttributehandler()
    {
        return this.attributehandler;
    }


    public void setAttributehandler(String attributehandler)
    {
        this.attributehandler = attributehandler;
    }


    public Boolean getParam()
    {
        return this.param;
    }


    public void setParam(Boolean param)
    {
        this.param = param;
    }


    public Boolean getNeedrestart()
    {
        return this.needrestart;
    }


    public void setNeedrestart(Boolean needrestart)
    {
        this.needrestart = needrestart;
    }


    public Boolean getStoreindatabase()
    {
        return this.storeindatabase;
    }


    public void setStoreindatabase(Boolean storeindatabase)
    {
        this.storeindatabase = storeindatabase;
    }


    public Boolean getOrdered()
    {
        return this.ordered;
    }


    public void setOrdered(Boolean ordered)
    {
        this.ordered = ordered;
    }


    public Long getRelationtype()
    {
        return this.relationtype;
    }


    public void setRelationtype(Long relationtype)
    {
        this.relationtype = relationtype;
    }


    public Boolean getGenerate()
    {
        return this.generate;
    }


    public void setGenerate(Boolean generate)
    {
        this.generate = generate;
    }


    public Boolean getIssource()
    {
        return this.issource;
    }


    public void setIssource(Boolean issource)
    {
        this.issource = issource;
    }


    public Boolean getUnique()
    {
        return this.unique;
    }


    public void setUnique(Boolean unique)
    {
        this.unique = unique;
    }


    public String getDefaultvaluedefinitionstring()
    {
        return this.defaultvaluedefinitionstring;
    }


    public void setDefaultvaluedefinitionstring(String defaultvaluedefinitionstring)
    {
        this.defaultvaluedefinitionstring = defaultvaluedefinitionstring;
    }


    public byte[] getDefaultvalue()
    {
        return this.defaultvalue;
    }


    public void setDefaultvalue(byte[] defaultvalue)
    {
        this.defaultvalue = defaultvalue;
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
            case "qualifierlowercaseinternal":
                return getQualifierlowercaseinternal();
            case "qualifierinternal":
                return getQualifierinternal();
            case "attributetypepk":
                return getAttributetypepk();
            case "selectiondescriptorpk":
                return getSelectiondescriptorpk();
            case "ishidden":
                return getIshidden();
            case "columnname":
                return getColumnname();
            case "enclosingtypepk":
                return getEnclosingtypepk();
            case "persistencetypepk":
                return getPersistencetypepk();
            case "inheritancepathstring":
                return getInheritancepathstring();
            case "isproperty":
                return getIsproperty();
            case "persistencequalifierinternal":
                return getPersistencequalifierinternal();
            case "superattributedescriptorpk":
                return getSuperattributedescriptorpk();
            case "modifiers":
                return getModifiers();
            case "p_defaultvalueexpression":
                return getDefaultvalueexpression();
            case "p_externalqualifier":
                return getExternalqualifier();
            case "p_dontcopy":
                return getDontcopy();
            case "p_relationname":
                return getRelationname();
            case "p_position":
                return getPosition();
            case "p_autocreate":
                return getAutocreate();
            case "p_extensionname":
                return getExtensionname();
            case "p_attributehandler":
                return getAttributehandler();
            case "p_param":
                return getParam();
            case "p_needrestart":
                return getNeedrestart();
            case "p_storeindatabase":
                return getStoreindatabase();
            case "p_ordered":
                return getOrdered();
            case "p_relationtype":
                return getRelationtype();
            case "p_generate":
                return getGenerate();
            case "p_issource":
                return getIssource();
            case "p_unique":
                return getUnique();
            case "p_defaultvaluedefinitionstring":
                return getDefaultvaluedefinitionstring();
            case "p_defaultvalue":
                return getDefaultvalue();
        }
        throw new IllegalArgumentException(columnName);
    }
}
