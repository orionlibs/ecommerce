package com.hybris.backoffice.daos;

import com.hybris.backoffice.model.CustomThemeModel;
import com.hybris.backoffice.model.ThemeModel;
import java.util.List;
import java.util.Optional;

public interface BackofficeThemeDao
{
    Optional<ThemeModel> findByCode(String paramString);


    List<ThemeModel> findAllThemes(boolean paramBoolean);


    List<CustomThemeModel> findAllCustomThemes();
}
