package de.hybris.platform.webservicescommons.jaxb.metadata.impl;

import de.hybris.platform.webservicescommons.jaxb.metadata.MetadataNameProvider;
import de.hybris.platform.webservicescommons.jaxb.metadata.MetadataSourceFactory;
import java.util.Collection;
import org.eclipse.persistence.jaxb.metadata.MetadataSource;

public class DefaultMetadataSourceFactory implements MetadataSourceFactory
{
    private MetadataNameProvider nameProvider;


    public MetadataSource createMetadataSource(Class<?> clazz, Collection<Class<?>> typeAdapters, Boolean wrapCollections)
    {
        return (MetadataSource)new WsDTOGenericMetadataSourceAdapter(clazz, typeAdapters, wrapCollections, this.nameProvider);
    }


    public void setNameProvider(MetadataNameProvider nameProvider)
    {
        this.nameProvider = nameProvider;
    }


    protected MetadataNameProvider getNameProvider()
    {
        return this.nameProvider;
    }
}
