package de.hybris.platform.persistence.meta;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.util.jeeapi.YCreateException;
import de.hybris.platform.util.jeeapi.YFinderException;

public interface MetaInformationHome extends ItemHome
{
    @Deprecated(since = "18.08", forRemoval = true)
    MetaInformationRemote create(PK paramPK, String paramString) throws YCreateException;


    MetaInformationRemote findBySystemID(String paramString) throws YFinderException;
}
