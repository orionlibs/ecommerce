package de.hybris.platform.basecommerce.site.dao;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import java.util.List;

public interface BaseSiteDao
{
    List<BaseSiteModel> findAllBaseSites();


    BaseSiteModel findBaseSiteByUID(String paramString);
}
