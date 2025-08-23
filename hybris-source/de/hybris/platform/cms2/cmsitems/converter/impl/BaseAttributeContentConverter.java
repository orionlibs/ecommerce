package de.hybris.platform.cms2.cmsitems.converter.impl;

import de.hybris.platform.cms2.cmsitems.converter.AttributeContentConverter;
import de.hybris.platform.cms2.common.functions.Converter;
import java.util.function.Predicate;

public class BaseAttributeContentConverter<T> implements AttributeContentConverter<T>
{
    private Predicate<T> constrainedBy;
    private Converter<Object, Object> modelToDataConverter;
    private Converter<Object, Object> dataToModelConverter;


    public Predicate<T> getConstrainedBy()
    {
        return this.constrainedBy;
    }


    public void setConstrainedBy(Predicate<T> constrainedBy)
    {
        this.constrainedBy = constrainedBy;
    }


    public Object convertModelToData(T attribute, Object source)
    {
        return getModelToDataConverter().convert(source);
    }


    public Object convertDataToModel(T attribute, Object source)
    {
        return getDataToModelConverter().convert(source);
    }


    protected Converter<Object, Object> getModelToDataConverter()
    {
        return this.modelToDataConverter;
    }


    public void setModelToDataConverter(Converter<Object, Object> modelToDataConverter)
    {
        this.modelToDataConverter = modelToDataConverter;
    }


    protected Converter<Object, Object> getDataToModelConverter()
    {
        return this.dataToModelConverter;
    }


    public void setDataToModelConverter(Converter<Object, Object> dataToModelConverter)
    {
        this.dataToModelConverter = dataToModelConverter;
    }
}
