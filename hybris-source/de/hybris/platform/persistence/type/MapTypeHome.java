package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface MapTypeHome extends TypeHome
{
    MapTypeRemote create(PK paramPK, String paramString, TypeRemote paramTypeRemote1, TypeRemote paramTypeRemote2) throws EJBDuplicateCodeException, EJBInvalidParameterException, YCreateException;


    MapTypeRemote findByPrimaryKey(PK paramPK) throws YFinderException;


    Collection findByArgumentType(PK paramPK) throws YFinderException;


    Collection findByReturnType(PK paramPK) throws YFinderException;


    Collection findByTypes(PK paramPK1, PK paramPK2) throws YFinderException;
}
