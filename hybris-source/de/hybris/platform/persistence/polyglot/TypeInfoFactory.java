package de.hybris.platform.persistence.polyglot;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TypeInfoFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeInfoFactory.class);


    public static TypeInfo getTypeInfo(Criteria criteria)
    {
        return (TypeInfo)new CriteriaTypeInfo(criteria);
    }


    public static TypeInfo getTypeInfo(Item.ItemAttributeMap allAttributes)
    {
        return (TypeInfo)new InitialAttributesTypeInfo(allAttributes);
    }


    public static TypeInfo getTypeInfo(int typeCode)
    {
        return (TypeInfo)new TypeCodeTypeInfo(typeCode);
    }


    public static TypeInfo getTypeInfo(Item jaloItem)
    {
        return (TypeInfo)new JaloItemTypeInfo(jaloItem);
    }


    public static TypeInfo getTypeInfo(ComposedType composedType)
    {
        return (TypeInfo)new ComposedTypeTypeInfo(composedType);
    }


    public static TypeInfo getTypeInfo(PK pk)
    {
        return (TypeInfo)new PKTypeInfo(pk);
    }
}
