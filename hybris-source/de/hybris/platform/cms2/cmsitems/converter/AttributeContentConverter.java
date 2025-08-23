package de.hybris.platform.cms2.cmsitems.converter;

import java.util.function.Predicate;

public interface AttributeContentConverter<T>
{
    Predicate<T> getConstrainedBy();


    Object convertModelToData(T paramT, Object paramObject);


    Object convertDataToModel(T paramT, Object paramObject);
}
