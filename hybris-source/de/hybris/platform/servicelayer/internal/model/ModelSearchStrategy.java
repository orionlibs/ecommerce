package de.hybris.platform.servicelayer.internal.model;

import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import java.util.List;

public interface ModelSearchStrategy
{
    <T> T getModelByExample(ModelConverter paramModelConverter, T paramT);


    <T> List<T> getModelsByExample(ModelConverter paramModelConverter, T paramT);
}
