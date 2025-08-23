package de.hybris.platform.servicelayer.internal.converter;

import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;

public interface ConverterRegistry
{
    ModelService getModelService();


    ModelConverter getModelConverterByModelType(Class<?> paramClass);


    boolean hasModelConverterForModelType(Class<?> paramClass);


    ModelConverter getModelConverterBySourceType(String paramString);


    boolean hasModelConverterForSourceType(String paramString);


    String getMappedType(Class<?> paramClass);


    ModelConverter removeModelConverterBySourceType(String paramString);


    Collection<ModelConverter> getModelConverters();


    void clearModelConverters();


    ModelConverter getModelConverterByModel(Object paramObject);
}
