package de.hybris.platform.persistence.property.loader.internal.dto;

import de.hybris.platform.core.ItemDeployment;

public class ComposedTypeDTO
{
    private long pk;
    private long superTypePk;
    private String typePkString;
    private Boolean jaloOnly;
    private String jaloClassName;
    private String typeCode;
    private int itemTypeCode;
    private boolean abstractTypeCodeFlag = false;
    private ItemDeployment deployment;
    private ItemDeployment superDeployment;
    private Boolean isAbstract;
    private Boolean isRelation;
    private Boolean isViewType;
    private String propsTable;
    private String itemTableName;
    private String auditTableName;
    private int modifiers;
    private Boolean propertyTableStatus;
    private boolean isAbstractWithoutConcreteSubTypes;


    public Boolean getAbstract()
    {
        return this.isAbstract;
    }


    public void setAbstract(Boolean anAbstract)
    {
        this.isAbstract = anAbstract;
    }


    public long getPk()
    {
        return this.pk;
    }


    public void setPk(long pk)
    {
        this.pk = pk;
    }


    public long getSuperTypePk()
    {
        return this.superTypePk;
    }


    public void setSuperTypePk(long superTypePk)
    {
        this.superTypePk = superTypePk;
    }


    public String getTypePkString()
    {
        return this.typePkString;
    }


    public void setTypePkString(String typePkString)
    {
        this.typePkString = typePkString;
    }


    public Boolean getJaloOnly()
    {
        return this.jaloOnly;
    }


    public void setJaloOnly(Boolean jaloOnly)
    {
        this.jaloOnly = jaloOnly;
    }


    public String getJaloClassName()
    {
        return this.jaloClassName;
    }


    public void setJaloClassName(String jaloClassName)
    {
        this.jaloClassName = jaloClassName;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public ItemDeployment getDeployment()
    {
        return this.deployment;
    }


    public void setDeployment(ItemDeployment deployment)
    {
        this.deployment = deployment;
    }


    public ItemDeployment getSuperDeployment()
    {
        return this.superDeployment;
    }


    public void setSuperDeployment(ItemDeployment superDeployment)
    {
        this.superDeployment = superDeployment;
    }


    public Boolean getRelation()
    {
        return this.isRelation;
    }


    public void setRelation(Boolean relation)
    {
        this.isRelation = relation;
    }


    public String getPropsTable()
    {
        return this.propsTable;
    }


    public void setPropsTable(String propsTable)
    {
        this.propsTable = propsTable;
    }


    public String getItemTableName()
    {
        return this.itemTableName;
    }


    public void setItemTableName(String itemTableName)
    {
        this.itemTableName = itemTableName;
    }


    public String getAuditTableName()
    {
        return this.auditTableName;
    }


    public void setAuditTableName(String auditTableName)
    {
        this.auditTableName = auditTableName;
    }


    public Boolean getViewType()
    {
        return this.isViewType;
    }


    public void setViewType(Boolean viewType)
    {
        this.isViewType = viewType;
    }


    public int getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(int modifiers)
    {
        this.modifiers = modifiers;
    }


    public int getItemTypeCode()
    {
        return this.itemTypeCode;
    }


    public void setItemTypeCode(int itemTypeCode)
    {
        this.itemTypeCode = itemTypeCode;
    }


    public Boolean isPropertyTableStatus()
    {
        return this.propertyTableStatus;
    }


    public void setPropertyTableStatus(Boolean propertyTableStatus)
    {
        this.propertyTableStatus = propertyTableStatus;
    }


    public boolean isAbstractWithoutConcreteSubTypes()
    {
        return this.isAbstractWithoutConcreteSubTypes;
    }


    public void setAbstractWithConcreteSubTypes()
    {
        this.isAbstractWithoutConcreteSubTypes = false;
    }


    public void setAbstractWithoutConcreteSubTypes()
    {
        this.isAbstractWithoutConcreteSubTypes = true;
    }


    public boolean isAbstractTypeCodeFlag()
    {
        return this.abstractTypeCodeFlag;
    }


    public void setAbstractTypeCodeFlag()
    {
        this.abstractTypeCodeFlag = true;
    }
}
