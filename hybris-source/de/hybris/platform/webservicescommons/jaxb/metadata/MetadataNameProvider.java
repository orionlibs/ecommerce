package de.hybris.platform.webservicescommons.jaxb.metadata;

import java.lang.reflect.Field;

public interface MetadataNameProvider
{
    String createNodeNameFromClass(Class<?> paramClass);


    String createCollectionItemNameFromField(Field paramField);
}
