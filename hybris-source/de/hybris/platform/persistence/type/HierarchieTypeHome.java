package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface HierarchieTypeHome extends TypeHome
{
    Collection findBySuperType(PK paramPK) throws YFinderException;


    Collection findByInheritancePath(String paramString) throws YFinderException;
}
