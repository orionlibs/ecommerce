package com.hybris.backoffice.daos;

import de.hybris.platform.processengine.model.BackofficeThemeConfigModel;
import java.util.List;

public interface BackofficeThemeConfigDao
{
    List<BackofficeThemeConfigModel> findByCode(String paramString);


    List<BackofficeThemeConfigModel> findByCodeAndActive(String paramString, boolean paramBoolean);
}
