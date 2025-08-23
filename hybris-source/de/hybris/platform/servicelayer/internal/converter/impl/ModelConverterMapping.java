package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import org.springframework.beans.factory.annotation.Required;

public class ModelConverterMapping
{
    private String typeCode;
    private Class modelClass;
    private ModelConverter converter;


    public ModelConverter getConverter()
    {
        return this.converter;
    }


    public void setConverter(ModelConverter converter)
    {
        this.converter = converter;
    }


    @Required
    public void setModelClass(Class modelClass)
    {
        this.modelClass = modelClass;
    }


    public Class getModelClass()
    {
        return this.modelClass;
    }


    @Required
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }
}
