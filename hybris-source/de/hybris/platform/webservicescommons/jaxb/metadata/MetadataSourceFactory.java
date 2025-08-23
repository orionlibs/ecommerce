package de.hybris.platform.webservicescommons.jaxb.metadata;

import java.util.Collection;
import org.eclipse.persistence.jaxb.metadata.MetadataSource;

public interface MetadataSourceFactory
{
    MetadataSource createMetadataSource(Class<?> paramClass, Collection<Class<?>> paramCollection, Boolean paramBoolean);
}
