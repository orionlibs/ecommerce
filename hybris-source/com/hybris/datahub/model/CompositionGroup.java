package com.hybris.datahub.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.hybris.datahub.domain.CanonicalAttributeDefinition;
import com.hybris.datahub.domain.CanonicalItemMetadata;
import com.hybris.datahub.runtime.domain.DataHubPool;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class CompositionGroup<T extends RawItem>
{
    private final DataHubPool pool;
    private final ImmutableList<T> items;
    private final ImmutableList<CanonicalAttributeDefinition> attributes;
    private ImmutableList<CanonicalAttributeDefinition> keyAttributes;
    private String batchId;
    private String traceId;


    public CompositionGroup(List<T> items, Collection<CanonicalAttributeDefinition> attributes, DataHubPool pool)
    {
        this.items = (items != null) ? ImmutableList.copyOf(items) : ImmutableList.of();
        this.attributes = (attributes != null) ? ImmutableList.copyOf(attributes) : ImmutableList.of();
        this.pool = pool;
        lastRawItem().ifPresent(item -> {
            this.batchId = item.getBatchId();
            this.traceId = item.getTraceId();
        });
    }


    private Optional<RawItem> lastRawItem()
    {
        return this.items.isEmpty() ? Optional.<RawItem>empty() : Optional.<RawItem>of((RawItem)this.items.get(this.items.size() - 1));
    }


    public DataHubPool getPool()
    {
        return this.pool;
    }


    public ImmutableList<T> getItems()
    {
        return this.items;
    }


    public ImmutableList<CanonicalAttributeDefinition> getAttributes()
    {
        return this.attributes;
    }


    public ImmutableList<CanonicalAttributeDefinition> getKeyTransformations()
    {
        if(this.keyAttributes == null)
        {
            this.keyAttributes = ImmutableList.copyOf(deriveKeyAttributeTransformations());
        }
        return this.keyAttributes;
    }


    private List<CanonicalAttributeDefinition> deriveKeyAttributeTransformations()
    {
        List<CanonicalAttributeDefinition> attrList = new ArrayList<>();
        for(UnmodifiableIterator<CanonicalAttributeDefinition> unmodifiableIterator = this.attributes.iterator(); unmodifiableIterator.hasNext(); )
        {
            CanonicalAttributeDefinition attrDef = unmodifiableIterator.next();
            if(isKeyAttributeTransformation(attrDef))
            {
                attrList.add(attrDef);
            }
        }
        return attrList;
    }


    private static boolean isKeyAttributeTransformation(CanonicalAttributeDefinition attrDesc)
    {
        return attrDesc.getCanonicalAttributeModelDefinition().isPrimaryKey();
    }


    public boolean isEmpty()
    {
        assert this.items != null : "constructor ensures list creation";
        return this.items.isEmpty();
    }


    public boolean isDeleteGroup()
    {
        assert this.items != null : "constructor ensures list creation";
        return this.items.stream().anyMatch(RawItem::isDelete);
    }


    public int getNumberOfItems()
    {
        return this.items.size();
    }


    public String getBatchId()
    {
        return this.batchId;
    }


    public String getTraceId()
    {
        return this.traceId;
    }


    public CanonicalItemMetadata getCanonicalItemMetadata()
    {
        assert this.attributes != null : "attributes are never null";
        if(!this.attributes.isEmpty())
        {
            CanonicalAttributeDefinition def = (CanonicalAttributeDefinition)this.attributes.iterator().next();
            return def.getCanonicalAttributeModelDefinition().getCanonicalItemMetadata();
        }
        return null;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        CompositionGroup<?> that = (CompositionGroup)o;
        return (new EqualsBuilder())
                        .append(this.pool, that.pool)
                        .append(this.items, that.items)
                        .append(this.attributes, that.attributes)
                        .append(this.keyAttributes, that.keyAttributes)
                        .append(this.batchId, that.batchId)
                        .append(this.traceId, that.traceId)
                        .isEquals();
    }


    public int hashCode()
    {
        return (new HashCodeBuilder(17, 37))
                        .append(this.pool)
                        .append(this.items)
                        .append(this.attributes)
                        .append(this.keyAttributes)
                        .append(this.batchId)
                        .append(this.traceId)
                        .toHashCode();
    }


    public String toString()
    {
        return "CompositionGroup{itemList=" + this.items + ", attributeList=" + this.attributes + "}";
    }
}
