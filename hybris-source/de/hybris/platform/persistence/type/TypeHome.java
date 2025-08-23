package de.hybris.platform.persistence.type;

import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.util.Collection;

public interface TypeHome extends ItemHome
{
    Collection findByCodeExact(String paramString) throws YFinderException;
}
