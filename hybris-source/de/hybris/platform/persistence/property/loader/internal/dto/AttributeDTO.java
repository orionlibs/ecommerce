package de.hybris.platform.persistence.property.loader.internal.dto;

public class AttributeDTO
{
    private Long pk;
    private Long ownerPk;
    private String qualifier;
    private String columnName;
    private int modifiers;
    private Long persistenceTypePk;
    private boolean property;
    private boolean localized;
    private boolean encrypted;
    private boolean notForOptimization;


    public Long getPk()
    {
        return this.pk;
    }


    public void setPk(Long pk)
    {
        this.pk = pk;
    }


    public Long getOwnerPk()
    {
        return this.ownerPk;
    }


    public void setOwnerPk(Long ownerPk)
    {
        this.ownerPk = ownerPk;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getColumnName()
    {
        return this.columnName;
    }


    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }


    public int getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(int modifiers)
    {
        this.modifiers = modifiers;
    }


    public Long getPersistenceTypePk()
    {
        return this.persistenceTypePk;
    }


    public void setPersistenceTypePk(Long persistenceTypePk)
    {
        this.persistenceTypePk = persistenceTypePk;
    }


    public boolean isProperty()
    {
        return this.property;
    }


    public void setProperty(boolean property)
    {
        this.property = property;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public void setLocalized(boolean localized)
    {
        this.localized = localized;
    }


    public boolean isCore()
    {
        return (!isProperty() && !isLocalized());
    }


    public boolean isEncrypted()
    {
        return this.encrypted;
    }


    public void setEncrypted(boolean encrypted)
    {
        this.encrypted = encrypted;
    }


    public boolean isNotForOptimization()
    {
        return this.notForOptimization;
    }


    public void setNotForOptimization(boolean notForOptimization)
    {
        this.notForOptimization = notForOptimization;
    }
}
