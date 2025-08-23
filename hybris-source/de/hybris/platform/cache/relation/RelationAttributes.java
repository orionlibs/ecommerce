package de.hybris.platform.cache.relation;

import de.hybris.platform.core.PK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class RelationAttributes
{
    private final String owningTypeCode;


    RelationAttributes(String owningTypeCode)
    {
        this.owningTypeCode = Objects.<String>requireNonNull(owningTypeCode);
    }


    static RelationAttributes empty()
    {
        return (RelationAttributes)EmptyRelationAttributes.INSTANCE;
    }


    String getOwningTypeCode()
    {
        return this.owningTypeCode;
    }


    Collection<Object[]> getRelationInvalidations(Function<String, Collection<PK>> fkValuesProvider)
    {
        Objects.requireNonNull(fkValuesProvider);
        if(!containsAnyAttribute())
        {
            return List.of();
        }
        Collection<Object[]> manySideInvalidations = new ArrayList();
        Collection<Object[]> typeInvalidations = new ArrayList();
        forEachAttribute((fkName, manySideName) -> {
            Collection<PK> fkValues = fkValuesProvider.apply(fkName);
            if(typeInvalidations.isEmpty())
            {
                if(fkValues.isEmpty())
                {
                    typeInvalidations.add(RelationCacheKey.createInvalidationKeyForRelation(getOwningTypeCode()));
                }
                else
                {
                    fkValues.forEach(());
                }
            }
        });
        return typeInvalidations.isEmpty() ? manySideInvalidations : typeInvalidations;
    }


    public String toString()
    {
        String attributes = "{}";
        if(containsAnyAttribute())
        {
            StringBuilder sb = new StringBuilder("{");
            forEachAttribute((fk, many) -> {
                if(sb.length() > 1)
                {
                    sb.append(", ");
                }
                sb.append(fk).append(":").append(many);
            });
            attributes = sb.append("}").toString();
        }
        return "[" + getOwningTypeCode() + "]" + attributes;
    }


    public abstract boolean containsAnyAttribute();


    abstract void forEachAttribute(BiConsumer<? super String, ? super String> paramBiConsumer);
}
