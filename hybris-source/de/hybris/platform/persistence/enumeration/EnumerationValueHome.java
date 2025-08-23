package de.hybris.platform.persistence.enumeration;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface EnumerationValueHome extends ItemHome
{
    EnumerationValueRemote create(PK paramPK, ComposedTypeRemote paramComposedTypeRemote, String paramString, int paramInt) throws YCreateException, ConsistencyCheckException, EJBInvalidParameterException;


    @Deprecated(since = "ages", forRemoval = true)
    EnumerationValueRemote findByTypeAndCode(PK paramPK, String paramString) throws YFinderException;


    EnumerationValueRemote findByTypeAndCodeIgnoreCase(PK paramPK, String paramString) throws YFinderException;


    Collection findSortedValues(PK paramPK) throws YFinderException;
}
