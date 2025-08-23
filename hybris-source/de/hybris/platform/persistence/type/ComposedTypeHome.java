package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface ComposedTypeHome extends HierarchieTypeHome
{
    ComposedTypeRemote create(PK paramPK, ComposedTypeRemote paramComposedTypeRemote1, String paramString1, String paramString2, ItemDeployment paramItemDeployment, ComposedTypeRemote paramComposedTypeRemote2) throws EJBDuplicateCodeException, EJBInvalidParameterException, YCreateException;


    ComposedTypeRemote findByPrimaryKey(PK paramPK) throws YFinderException;


    Collection findByJaloClassName(String paramString) throws YFinderException;


    Collection findByTypeCode(int paramInt) throws YFinderException;
}
