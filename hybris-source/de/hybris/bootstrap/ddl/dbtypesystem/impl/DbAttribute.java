package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import de.hybris.bootstrap.ddl.dbtypesystem.Attribute;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import org.apache.commons.lang.StringUtils;

class DbAttribute implements Attribute
{
    private final String rowsHash;
    private final long pk;
    private final long enclosingTypePk;
    private final String qualifierLowerCaseInternal;
    private DbType enclosingType;
    private final boolean unique;
    private final boolean property;
    private final Integer modifiers;
    private final String columnName;
    private final Long persistenceTypePk;
    private final String attributeHandler;


    public DbAttribute(AttributeRow row)
    {
        this.rowsHash = HashGenerationStrategy.getForAttributeDescriptor().getHashFor((Row)row);
        this.pk = row.getPk().longValue();
        this.enclosingTypePk = row.getEnclosingtypepk().longValue();
        this.qualifierLowerCaseInternal = row.getQualifierlowercaseinternal();
        this.unique = (row.getUnique() != null && row.getUnique().booleanValue());
        this.modifiers = row.getModifiers();
        this.columnName = row.getColumnname();
        this.persistenceTypePk = row.getPersistencetypepk();
        this.attributeHandler = row.getAttributehandler();
        this.property = (row.getIsproperty() != null && row.getIsproperty().booleanValue());
    }


    public long getPk()
    {
        return this.pk;
    }


    public long getEnclosingTypePk()
    {
        return this.enclosingTypePk;
    }


    public Long getPersistenceTypePk()
    {
        return this.persistenceTypePk;
    }


    public String getQualifierLowerCaseInternal()
    {
        return this.qualifierLowerCaseInternal;
    }


    public String getAttributeHandler()
    {
        return this.attributeHandler;
    }


    public boolean isProperty()
    {
        return this.property;
    }


    public boolean equals(Object obj)
    {
        return (obj != null && obj instanceof DbAttribute && ((DbAttribute)obj).getPk() == getPk());
    }


    public int hashCode()
    {
        return (int)this.pk;
    }


    void setEnclosingType(DbType enclosingType)
    {
        this.enclosingType = enclosingType;
    }


    public DbType getEnclosingType()
    {
        return this.enclosingType;
    }


    public String toString()
    {
        return "DbAttribute{attributeHandler='" + this.attributeHandler + "', pk=" + this.pk + ", enclosingTypePk=" + this.enclosingTypePk + ", qualifierLowerCaseInternal='" + this.qualifierLowerCaseInternal + "', enclosingTypeCode=" + (
                        (this.enclosingType == null) ? "<empty>" : this.enclosingType.getInternalCodeLowerCase()) + ", unique=" + this.unique + ", property=" + this.property + ", modifiers=" + this.modifiers + ", columnName='" + this.columnName + "', persistenceTypePk=" + this.persistenceTypePk
                        + "}";
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public Integer getModifiers()
    {
        return this.modifiers;
    }


    public String getColumnName()
    {
        return this.columnName;
    }


    public UniqueIdentifier getUniqueIdentifier()
    {
        return UniqueIdentifier.createFrom(this.pk);
    }


    public String getHash()
    {
        return this.rowsHash;
    }


    public YAttributeDescriptor.PersistenceType calculatePersistenceType()
    {
        return calculatePersistenceType(getAttributeHandler(), getPersistenceTypePk(), isProperty());
    }


    public static YAttributeDescriptor.PersistenceType calculatePersistenceType(String attributeHandler, Long persistenceTypePk, boolean isProperty)
    {
        if(StringUtils.isNotBlank(attributeHandler) && persistenceTypePk == null)
        {
            return YAttributeDescriptor.PersistenceType.DYNAMIC;
        }
        if(persistenceTypePk != null)
        {
            if(isProperty)
            {
                return YAttributeDescriptor.PersistenceType.PROPERTY;
            }
            return YAttributeDescriptor.PersistenceType.CMP;
        }
        return YAttributeDescriptor.PersistenceType.JALO;
    }
}
