package de.hybris.platform.cockpit.services.meta;

import java.util.Collection;

public interface ObjectTypeFilter<TYPE extends de.hybris.platform.cockpit.model.meta.ObjectType, TARGET>
{
    Collection<TYPE> filterObjectTypes(Collection<TYPE> paramCollection, TARGET paramTARGET);
}
