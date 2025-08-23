package com.hybris.backoffice.theme;

import com.hybris.backoffice.model.CustomThemeModel;
import com.hybris.backoffice.model.ThemeModel;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface BackofficeThemeService
{
    @NotNull
    ThemeModel getCurrentTheme();


    @NotNull
    ThemeModel getCurrentUserTheme();


    void setCurrentUserTheme(@NotNull String paramString) throws ThemeNotFound;


    @NotNull
    ThemeModel getSystemTheme();


    void setSystemTheme(@NotNull String paramString) throws ThemeNotFound;


    @NotNull
    List<ThemeModel> getAvailableThemes();


    @NotNull
    List<ThemeModel> getBaseThemes();


    @NotNull
    List<CustomThemeModel> getCustomThemes();


    @NotNull
    ThemeModel getDefaultTheme();


    @NotNull
    BackofficeThemeLevel getThemeLevel();


    void setThemeLevel(@NotNull BackofficeThemeLevel paramBackofficeThemeLevel);


    void setUserLevelDefaultTheme(String paramString) throws ThemeNotFound;


    @NotNull
    ThemeModel getUserLevelDefaultTheme();


    @NotNull
    int getMaximumOfCustomTheme();
}
