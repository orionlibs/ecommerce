package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;
import java.util.List;

public interface CMSSiteDao extends Dao
{
    Collection<CMSSiteModel> findAllCMSSites();


    List<CMSSiteModel> findCMSSitesById(String paramString);
}
