package com.hybris.backoffice.theme;

import com.hybris.backoffice.daos.BackofficeThemeConfigDao;
import com.hybris.backoffice.daos.BackofficeThemeDao;
import com.hybris.backoffice.model.ThemeModel;
import com.hybris.backoffice.theme.impl.DefaultBackofficeThemeService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.model.BackofficeThemeConfigModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeThemeServiceTest
{
    private static final String BACKOFFICE_THEME_LEVEL_KEY = "backoffice.theme.level";
    private static final String BACKOFFICE_SYSTEM_LEVEL_THEME_KEY = "backoffice.theme.system";
    private static final String BACKOFFICE_USER_LEVEL_THEME_DEFAULT_KEY = "backoffice.theme.user.default";
    @Mock
    private ModelService modelService;
    @Mock
    private UserService userService;
    @Mock
    private BackofficeThemeDao backofficeThemeDao;
    @Mock
    private BackofficeThemeConfigDao backofficeThemeConfigDao;
    @Mock
    private BackofficeThemeConfigModel themeLevelConfig;
    @Mock
    private BackofficeThemeConfigModel systemThemeConfig;
    @Mock
    private BackofficeThemeConfigModel userLevelDefaultThemeConfig;
    @Mock
    private ThemeModel systemTheme;
    @Mock
    private ThemeModel userTheme;
    @Mock
    private UserModel user;
    @Mock
    private ThemeModel defaultTheme;
    @Mock
    private MediaModel defaultStyle;
    private String defaultThemeCode = "defaultThemeCode";
    private String defaultStyleURL = "defaultStyleURL";
    @Spy
    @InjectMocks
    private DefaultBackofficeThemeService defaultBackofficeThemeService;


    @Before
    public void setup()
    {
        Mockito.when(this.modelService.create(ThemeModel.class)).thenReturn(this.defaultTheme);
        Mockito.when(this.modelService.create(MediaModel.class)).thenReturn(this.defaultStyle);
        this.defaultBackofficeThemeService.setDefaultThemeCode(this.defaultThemeCode);
    }


    @Test
    public void getSystemThemeThemeShouldGet()
    {
        String systemThemeCode = "system_theme";
        Mockito.when(this.systemThemeConfig.getContent()).thenReturn("system_theme");
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.system", true)).thenReturn(Arrays.asList(new BackofficeThemeConfigModel[] {this.systemThemeConfig}));
        Mockito.when(this.backofficeThemeDao.findByCode("system_theme")).thenReturn(Optional.of(this.systemTheme));
        Assertions.assertThat(this.defaultBackofficeThemeService.getSystemTheme()).isEqualTo(this.systemTheme);
    }


    @Test
    public void getSystemThemeShouldGetDefaultWhenNoConfig()
    {
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.system", true)).thenReturn(Collections.emptyList());
        Mockito.when(this.backofficeThemeDao.findByCode(this.defaultThemeCode)).thenReturn(Optional.of(this.systemTheme));
        Assertions.assertThat(this.defaultBackofficeThemeService.getSystemTheme()).isEqualTo(this.systemTheme);
    }


    @Test
    public void getSystemThemeShouldGetDefaultWhenNoTheme()
    {
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.system", true)).thenReturn(Collections.emptyList());
        Mockito.when(this.backofficeThemeDao.findByCode(this.defaultThemeCode)).thenReturn(Optional.empty());
        Assertions.assertThat(this.defaultBackofficeThemeService.getSystemTheme()).isEqualTo(this.defaultTheme);
    }


    @Test
    public void getCurrentUserThemeShouldGetUserThemeWhenThemeSetByUser()
    {
        Mockito.when(this.userService.getCurrentUser()).thenReturn(this.user);
        Mockito.when(this.user.getThemeForBackoffice()).thenReturn(this.userTheme);
        Assertions.assertThat(this.defaultBackofficeThemeService.getCurrentUserTheme()).isEqualTo(this.userTheme);
    }


    @Test
    public void getCurrentUserThemeShouldGetDefaultThemeWhenNoThemeSetByUser()
    {
        Mockito.when(this.userService.getCurrentUser()).thenReturn(this.user);
        Mockito.when(this.user.getThemeForBackoffice()).thenReturn(null);
        Assertions.assertThat(this.defaultBackofficeThemeService.getCurrentUserTheme()).isEqualTo(this.defaultTheme);
    }


    @Test
    public void getAvailableThemesShouldGetAllThemes()
    {
        List<ThemeModel> themes = new ArrayList<>();
        Mockito.when(this.backofficeThemeDao.findAllThemes(false)).thenReturn(themes);
        Assertions.assertThat(this.defaultBackofficeThemeService.getAvailableThemes()).isEqualTo(themes);
    }


    @Test
    public void getCurrentUserThemeShouldGetSystemTheme()
    {
        ((DefaultBackofficeThemeService)Mockito.doReturn(BackofficeThemeLevel.SYSTEM).when(this.defaultBackofficeThemeService)).getThemeLevel();
        ((DefaultBackofficeThemeService)Mockito.doReturn(this.systemTheme).when(this.defaultBackofficeThemeService)).getSystemTheme();
        Assertions.assertThat(this.defaultBackofficeThemeService.getCurrentTheme()).isEqualTo(this.systemTheme);
    }


    @Test
    public void getCurrentUserThemeShouldGetUserTheme()
    {
        ((DefaultBackofficeThemeService)Mockito.doReturn(BackofficeThemeLevel.USER).when(this.defaultBackofficeThemeService)).getThemeLevel();
        ((DefaultBackofficeThemeService)Mockito.doReturn(this.userTheme).when(this.defaultBackofficeThemeService)).getCurrentUserTheme();
        Assertions.assertThat(this.defaultBackofficeThemeService.getCurrentTheme()).isEqualTo(this.userTheme);
    }


    @Test
    public void getThemeLevelShouldGetUserLevel()
    {
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.level", true)).thenReturn(Arrays.asList(new BackofficeThemeConfigModel[] {this.themeLevelConfig}));
        Mockito.when(this.themeLevelConfig.getContent()).thenReturn(BackofficeThemeLevel.USER.name());
        Assertions.assertThat((Comparable)this.defaultBackofficeThemeService.getThemeLevel()).isEqualTo(BackofficeThemeLevel.USER);
    }


    @Test
    public void getThemeLevelShouldGetDefaultLevelWhenNoConfig()
    {
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.level", true)).thenReturn(Collections.emptyList());
        Assertions.assertThat((Comparable)this.defaultBackofficeThemeService.getThemeLevel()).isEqualTo(BackofficeThemeLevel.SYSTEM);
    }


    @Test
    public void setThemeLevelShouldSuccWhenHasConfig()
    {
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.level", true)).thenReturn(Arrays.asList(new BackofficeThemeConfigModel[] {this.themeLevelConfig}));
        Mockito.when(this.themeLevelConfig.getContent()).thenReturn(BackofficeThemeLevel.USER.name());
        this.defaultBackofficeThemeService.setThemeLevel(BackofficeThemeLevel.SYSTEM);
        ((ModelService)Mockito.verify(this.modelService)).save(Mockito.any(BackofficeThemeConfigModel.class));
    }


    @Test
    public void setThemeLevelShouldSuccWhenNoConfig()
    {
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.level", true)).thenReturn(Collections.emptyList());
        Mockito.when(this.modelService.create(BackofficeThemeConfigModel.class)).thenReturn(this.themeLevelConfig);
        this.defaultBackofficeThemeService.setThemeLevel(BackofficeThemeLevel.SYSTEM);
        ((ModelService)Mockito.verify(this.modelService)).save(Mockito.any(BackofficeThemeConfigModel.class));
    }


    @Test
    public void setCurrentUserThemeShouldSucc() throws ThemeNotFound
    {
        String code = "valid_code";
        Mockito.when(this.userService.getCurrentUser()).thenReturn(this.user);
        Mockito.when(this.backofficeThemeDao.findByCode("valid_code")).thenReturn(Optional.of(this.userTheme));
        this.defaultBackofficeThemeService.setCurrentUserTheme("valid_code");
        ((ModelService)Mockito.verify(this.modelService)).save(Mockito.any(UserModel.class));
    }


    @Test(expected = ThemeNotFound.class)
    public void setCurrentUserThemeShouldThrowExceptionWhenNoTheme() throws ThemeNotFound
    {
        String code = "invalid_code";
        Mockito.when(this.userService.getCurrentUser()).thenReturn(this.user);
        Mockito.when(this.backofficeThemeDao.findByCode("invalid_code")).thenReturn(Optional.empty());
        this.defaultBackofficeThemeService.setCurrentUserTheme("invalid_code");
    }


    @Test
    public void setSystemThemeShouldSuccWhenHasConfig() throws ThemeNotFound
    {
        String code = "valid_code";
        Mockito.when(this.backofficeThemeDao.findByCode("valid_code")).thenReturn(Optional.of(this.userTheme));
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.system", true)).thenReturn(Arrays.asList(new BackofficeThemeConfigModel[] {this.systemThemeConfig}));
        Mockito.when(this.systemThemeConfig.getContent()).thenReturn("old");
        this.defaultBackofficeThemeService.setSystemTheme("valid_code");
        ((ModelService)Mockito.verify(this.modelService)).save(this.systemThemeConfig);
    }


    @Test
    public void setSystemThemeShouldSuccWhenNoConfig() throws ThemeNotFound
    {
        String code = "valid_code";
        Mockito.when(this.backofficeThemeDao.findByCode("valid_code")).thenReturn(Optional.of(this.userTheme));
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.system", true)).thenReturn(Collections.emptyList());
        Mockito.when(this.modelService.create(BackofficeThemeConfigModel.class)).thenReturn(this.systemThemeConfig);
        this.defaultBackofficeThemeService.setSystemTheme("valid_code");
        ((ModelService)Mockito.verify(this.modelService)).save(this.systemThemeConfig);
    }


    @Test(expected = ThemeNotFound.class)
    public void setSystemThemeShouldThrowExceptionWhenNoTheme() throws ThemeNotFound
    {
        String code = "invalid_code";
        Mockito.when(this.backofficeThemeDao.findByCode("invalid_code")).thenReturn(Optional.empty());
        this.defaultBackofficeThemeService.setSystemTheme("invalid_code");
    }


    @Test
    public void setUserLevelDefaultThemeShouldSuccWhenHasConfig() throws ThemeNotFound
    {
        String code = "valid_code";
        Mockito.when(this.backofficeThemeDao.findByCode("valid_code")).thenReturn(Optional.of(this.userTheme));
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.user.default", true)).thenReturn(Arrays.asList(new BackofficeThemeConfigModel[] {this.userLevelDefaultThemeConfig}));
        Mockito.when(this.userLevelDefaultThemeConfig.getContent()).thenReturn("old");
        this.defaultBackofficeThemeService.setUserLevelDefaultTheme("valid_code");
        ((ModelService)Mockito.verify(this.modelService)).save(this.userLevelDefaultThemeConfig);
    }


    @Test
    public void setUserLevelDefaultThemeShouldSuccWhenNoConfig() throws ThemeNotFound
    {
        String code = "valid_code";
        Mockito.when(this.backofficeThemeDao.findByCode("valid_code")).thenReturn(Optional.of(this.userTheme));
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.user.default", true)).thenReturn(Collections.emptyList());
        Mockito.when(this.modelService.create(BackofficeThemeConfigModel.class)).thenReturn(this.userLevelDefaultThemeConfig);
        this.defaultBackofficeThemeService.setUserLevelDefaultTheme("valid_code");
        ((ModelService)Mockito.verify(this.modelService)).save(this.userLevelDefaultThemeConfig);
    }


    @Test
    public void setUserLevelDefaultThemeShouldSuccWhenNoActiveConfigHasInactiveConfig() throws ThemeNotFound
    {
        String code = "valid_code";
        Mockito.when(this.backofficeThemeDao.findByCode("valid_code")).thenReturn(Optional.of(this.userTheme));
        Mockito.when(this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.user.default", true)).thenReturn(Collections.emptyList());
        List<BackofficeThemeConfigModel> inactiveConfigs = Arrays.asList(new BackofficeThemeConfigModel[] {this.userLevelDefaultThemeConfig});
        Mockito.when(this.backofficeThemeConfigDao.findByCode("backoffice.theme.user.default")).thenReturn(inactiveConfigs);
        ((ModelService)Mockito.doNothing().when(this.modelService)).removeAll(inactiveConfigs);
        Mockito.when(this.modelService.create(BackofficeThemeConfigModel.class)).thenReturn(this.userLevelDefaultThemeConfig);
        this.defaultBackofficeThemeService.setUserLevelDefaultTheme("valid_code");
        ((ModelService)Mockito.verify(this.modelService)).removeAll(inactiveConfigs);
        ((ModelService)Mockito.verify(this.modelService)).save(this.userLevelDefaultThemeConfig);
    }


    @Test(expected = ThemeNotFound.class)
    public void setUserLevelDefaultThemeShouldThrowExceptionWhenNoTheme() throws ThemeNotFound
    {
        String code = "invalid_code";
        Mockito.when(this.backofficeThemeDao.findByCode("invalid_code")).thenReturn(Optional.empty());
        this.defaultBackofficeThemeService.setUserLevelDefaultTheme("invalid_code");
    }


    @Test
    public void getDefaultThemeShouldReturnWhenNoThemeFound()
    {
        Mockito.when(this.backofficeThemeDao.findByCode(this.defaultThemeCode)).thenReturn(Optional.empty());
        ThemeModel theme = this.defaultBackofficeThemeService.getDefaultTheme();
        Assertions.assertThat(theme).isNotNull();
    }
}
