package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface SearchRestrictionHome extends ItemHome
{
    SearchRestrictionRemote create(PK paramPK1, PK paramPK2, String paramString1, String paramString2, Boolean paramBoolean) throws YCreateException;


    Collection findRestrictions(PK paramPK1, PK paramPK2) throws YFinderException;


    Collection findByPrincipal(PK paramPK) throws YFinderException;


    Collection findByRestrictedType(PK paramPK) throws YFinderException;
}
