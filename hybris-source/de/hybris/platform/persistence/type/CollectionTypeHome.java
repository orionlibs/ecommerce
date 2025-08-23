package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface CollectionTypeHome extends TypeHome
{
    CollectionTypeRemote create(PK paramPK, String paramString, TypeRemote paramTypeRemote, int paramInt) throws EJBDuplicateCodeException, EJBInvalidParameterException, YCreateException;


    CollectionTypeRemote findByPrimaryKey(PK paramPK) throws YFinderException;


    Collection findByElementType(PK paramPK) throws YFinderException;
}
