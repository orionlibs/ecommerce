package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.framework.HomeProxy;
import de.hybris.platform.util.jeeapi.YFinderException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Date;

public interface ItemHome extends HomeProxy
{
    Collection findAll() throws YFinderException;


    Collection findByPKList(Collection paramCollection) throws YFinderException;


    Collection findChangedAfter(Date paramDate) throws YFinderException;


    Collection findByType(PK paramPK) throws YFinderException;


    void loadItemData(ResultSet paramResultSet);


    String getOwnJNDIName();
}
