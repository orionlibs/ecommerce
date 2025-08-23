package de.hybris.platform.persistence.link;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemHome;
import de.hybris.platform.util.jeeapi.YCreateException;

public interface LinkHome extends ItemHome
{
    LinkRemote create(String paramString, PK paramPK1, PK paramPK2, int paramInt1, int paramInt2) throws YCreateException, EJBInvalidParameterException;
}
