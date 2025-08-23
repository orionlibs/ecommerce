package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.jeeapi.YCreateException;

public interface GenericItemHome extends ItemHome
{
    GenericItemRemote create(PK paramPK, ComposedTypeRemote paramComposedTypeRemote, EJBPropertyContainer paramEJBPropertyContainer) throws YCreateException;
}
