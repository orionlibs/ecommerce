package de.hybris.platform.servicelayer.user.daos;

import de.hybris.platform.core.model.user.TitleModel;
import java.util.Collection;

public interface TitleDao
{
    Collection<TitleModel> findTitles();


    TitleModel findTitleByCode(String paramString);
}
