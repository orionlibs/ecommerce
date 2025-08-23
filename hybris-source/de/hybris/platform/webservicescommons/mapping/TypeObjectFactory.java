package de.hybris.platform.webservicescommons.mapping;

import ma.glasnost.orika.ObjectFactory;

public interface TypeObjectFactory<D> extends ObjectFactory<D>
{
    Class<D> getType();
}
