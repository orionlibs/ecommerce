package de.hybris.platform.persistence.meta;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.ExtensibleItemRemote;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;

public interface MetaInformationRemote extends ExtensibleItemRemote
{
    void setInitializedFlag(boolean paramBoolean);


    boolean getInitializedFlag();


    String getSystemName();


    String getSystemPk();


    @Deprecated(since = "18.08", forRemoval = true)
    void setSystemName(String paramString);


    void setSystemPk(String paramString);


    PK ejbFindByPrimaryKey(PK paramPK) throws YObjectNotFoundException, YFinderException;
}
