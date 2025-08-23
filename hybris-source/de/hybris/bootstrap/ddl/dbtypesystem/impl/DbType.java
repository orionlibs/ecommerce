package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import de.hybris.bootstrap.ddl.dbtypesystem.Attribute;
import de.hybris.bootstrap.ddl.dbtypesystem.EnumerationValue;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.Type;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

class DbType implements Type
{
    private final String rowsHash;
    private final long pk;
    private final String internalCodeLowerCase;
    private final Long superTypePk;
    private final int itemTypeCode;
    private DbType superType;
    private ImmutableMap<String, Attribute> attributes;
    private ImmutableMap<String, EnumerationValue> enumerationValues;
    private DbDeployment deployment;
    private final String itemJndiName;


    public DbType(TypeRow row)
    {
        Preconditions.checkNotNull(row);
        Preconditions.checkNotNull(row.getPk());
        this.rowsHash = HashGenerationStrategy.getForItemType().getHashFor((Row)row);
        this.pk = row.getPk().longValue();
        this.internalCodeLowerCase = row.getInternalcodelowercase();
        this.superTypePk = row.getSupertypepk();
        this.itemTypeCode = row.getItemtypecode().intValue();
        this.itemJndiName = row.getItemjndiname();
    }


    public long getPK()
    {
        return this.pk;
    }


    public String getInternalCodeLowerCase()
    {
        return this.internalCodeLowerCase;
    }


    public Long getSuperTypePk()
    {
        return this.superTypePk;
    }


    public int getItemTypeCode()
    {
        return this.itemTypeCode;
    }


    public DbType getSuperType()
    {
        return this.superType;
    }


    void setSuperType(DbType superType)
    {
        this.superType = superType;
    }


    public String toString()
    {
        return "DbType{attributes=" + this.attributes + ", pk=" + this.pk + ", internalCodeLowerCase='" + this.internalCodeLowerCase + "', superTypePk=" + this.superTypePk + ", itemTypeCode=" + this.itemTypeCode + ", superType=" + (
                        (this.superType == null) ? "<empty>" :
                                        this.superType.getInternalCodeLowerCase()) + ", enumerationValues=" + this.enumerationValues + ", deployment=" + this.deployment + ", itemJndiName='" + this.itemJndiName + "'}";
    }


    public Collection<Attribute> getAttributes()
    {
        return (Collection<Attribute>)this.attributes.values();
    }


    public Attribute getAttribute(String attributeName)
    {
        return (Attribute)this.attributes.get(attributeName.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    void setAttributes(Set<? extends Attribute> attributes)
    {
        ImmutableMap immutableMap = Maps.uniqueIndex(attributes, (Function)new Object(this));
        this.attributes = ImmutableMap.copyOf((Map)immutableMap);
    }


    public Collection<EnumerationValue> getEnumerationValues()
    {
        return (Collection<EnumerationValue>)this.enumerationValues.values();
    }


    public EnumerationValue getEnumerationValue(String enumerationValueName)
    {
        return (EnumerationValue)this.enumerationValues.get(enumerationValueName.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    void setEnumerationValues(Set<? extends EnumerationValue> enumerationValues)
    {
        ImmutableMap immutableMap = Maps.uniqueIndex(enumerationValues, (Function)new Object(this));
        this.enumerationValues = ImmutableMap.copyOf((Map)immutableMap);
    }


    public DbDeployment getDeployment()
    {
        return this.deployment;
    }


    void setDeployment(DbDeployment deployment)
    {
        this.deployment = deployment;
    }


    public String getItemJndiName()
    {
        return this.itemJndiName;
    }


    public UniqueIdentifier getUniqueIdentifier()
    {
        return UniqueIdentifier.createFrom(this.pk);
    }


    public String getHash()
    {
        return this.rowsHash;
    }
}
