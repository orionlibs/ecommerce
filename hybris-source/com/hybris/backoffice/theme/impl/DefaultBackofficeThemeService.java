package com.hybris.backoffice.theme.impl;

import com.hybris.backoffice.daos.BackofficeThemeConfigDao;
import com.hybris.backoffice.daos.BackofficeThemeDao;
import com.hybris.backoffice.model.CustomThemeModel;
import com.hybris.backoffice.model.ThemeModel;
import com.hybris.backoffice.theme.BackofficeThemeLevel;
import com.hybris.backoffice.theme.BackofficeThemeService;
import com.hybris.backoffice.theme.ThemeNotFound;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.model.BackofficeThemeConfigModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBackofficeThemeService implements BackofficeThemeService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBackofficeThemeService.class);
    private static final int MAXIMUM_CUSTOM_THEME = 10;
    private static final String BACKOFFICE_DEFAULT_THEME_STYLE_URL_FORMAT = "./cng/css/themes/%s/variables.css";
    private static final String BACKOFFICE_THEME_LEVEL_KEY = "backoffice.theme.level";
    private static final BackofficeThemeLevel BACKOFFICE_THEME_LEVEL_VALUE_DEFAULT = BackofficeThemeLevel.SYSTEM;
    private static final String BACKOFFICE_SYSTEM_LEVEL_THEME_KEY = "backoffice.theme.system";
    private static final String BACKOFFICE_USER_LEVEL_THEME_DEFAULT_KEY = "backoffice.theme.user.default";
    private ModelService modelService;
    private UserService userService;
    private BackofficeThemeConfigDao backofficeThemeConfigDao;
    private BackofficeThemeDao backofficeThemeDao;
    private String defaultThemeCode;


    public ThemeModel getCurrentTheme()
    {
        return (getThemeLevel() == BackofficeThemeLevel.SYSTEM) ? getSystemTheme() : getCurrentUserTheme();
    }


    public ThemeModel getSystemTheme()
    {
        return getThemeByConfig("backoffice.theme.system");
    }


    public ThemeModel getCurrentUserTheme()
    {
        UserModel currentUser = this.userService.getCurrentUser();
        ThemeModel currentUserTheme = currentUser.getThemeForBackoffice();
        if(currentUserTheme == null)
        {
            LOG.debug("No user theme found, use user default theme");
            return getUserLevelDefaultTheme();
        }
        return currentUserTheme;
    }


    public void setCurrentUserTheme(String code) throws ThemeNotFound
    {
        UserModel currentUser = this.userService.getCurrentUser();
        Optional<ThemeModel> themeOpt = this.backofficeThemeDao.findByCode(code);
        if(themeOpt.isEmpty())
        {
            throw new ThemeNotFound();
        }
        currentUser.setThemeForBackoffice(themeOpt.get());
        this.modelService.save(currentUser);
    }


    public void setSystemTheme(String code) throws ThemeNotFound
    {
        Optional<ThemeModel> themeOpt = this.backofficeThemeDao.findByCode(code);
        if(themeOpt.isEmpty())
        {
            throw new ThemeNotFound();
        }
        saveOrCreateConfig("backoffice.theme.system", code);
    }


    private void saveOrCreateConfig(String code, String content)
    {
        List<BackofficeThemeConfigModel> activeConfigs = this.backofficeThemeConfigDao.findByCodeAndActive(code, true);
        if(activeConfigs.isEmpty())
        {
            makeSureNoConfig(code);
            BackofficeThemeConfigModel activeConfig = (BackofficeThemeConfigModel)this.modelService.create(BackofficeThemeConfigModel.class);
            activeConfig.setCode(code);
            activeConfig.setContent(content);
            activeConfig.setActive(Boolean.valueOf(true));
            this.modelService.save(activeConfig);
        }
        else
        {
            BackofficeThemeConfigModel activeConfig = activeConfigs.get(0);
            if(!activeConfig.getContent().equals(content))
            {
                activeConfig.setContent(content);
                this.modelService.save(activeConfig);
            }
        }
    }


    private void makeSureNoConfig(String code)
    {
        List<BackofficeThemeConfigModel> configs = this.backofficeThemeConfigDao.findByCode(code);
        if(!configs.isEmpty())
        {
            this.modelService.removeAll(configs);
        }
    }


    public void setUserLevelDefaultTheme(String code) throws ThemeNotFound
    {
        Optional<ThemeModel> themeOpt = this.backofficeThemeDao.findByCode(code);
        if(themeOpt.isEmpty())
        {
            throw new ThemeNotFound();
        }
        saveOrCreateConfig("backoffice.theme.user.default", code);
    }


    public ThemeModel getUserLevelDefaultTheme()
    {
        return getThemeByConfig("backoffice.theme.user.default");
    }


    public int getMaximumOfCustomTheme()
    {
        return 10;
    }


    private ThemeModel getThemeByConfig(String code)
    {
        List<BackofficeThemeConfigModel> configs = this.backofficeThemeConfigDao.findByCodeAndActive(code, true);
        String themeCode = null;
        if(configs.isEmpty())
        {
            LOG.debug("{} found, use default theme code", code);
            themeCode = this.defaultThemeCode;
        }
        else
        {
            themeCode = ((BackofficeThemeConfigModel)configs.get(0)).getContent();
        }
        Optional<ThemeModel> themeOpt = this.backofficeThemeDao.findByCode(themeCode);
        return themeOpt.isPresent() ? themeOpt.get() : getDefaultTheme();
    }


    public List<ThemeModel> getAvailableThemes()
    {
        return this.backofficeThemeDao.findAllThemes(false);
    }


    public List<ThemeModel> getBaseThemes()
    {
        return this.backofficeThemeDao.findAllThemes(true);
    }


    public List<CustomThemeModel> getCustomThemes()
    {
        return this.backofficeThemeDao.findAllCustomThemes();
    }


    public ThemeModel getDefaultTheme()
    {
        Optional<ThemeModel> themeOpt = this.backofficeThemeDao.findByCode(this.defaultThemeCode);
        if(themeOpt.isPresent())
        {
            ThemeModel theme = themeOpt.get();
            if(isThemeValid(theme))
            {
                return theme;
            }
        }
        return getTransientTheme();
    }


    private boolean isThemeValid(ThemeModel theme)
    {
        if(theme.getStyle() == null)
        {
            LOG.debug("Theme's style does not exist.");
            return false;
        }
        return true;
    }


    private ThemeModel getTransientTheme()
    {
        ThemeModel theme = (ThemeModel)this.modelService.create(ThemeModel.class);
        theme.setCode(this.defaultThemeCode);
        MediaModel style = (MediaModel)this.modelService.create(MediaModel.class);
        style.setURL(String.format("./cng/css/themes/%s/variables.css", new Object[] {this.defaultThemeCode}));
        theme.setStyle(style);
        return theme;
    }


    public BackofficeThemeLevel getThemeLevel()
    {
        List<BackofficeThemeConfigModel> configs = this.backofficeThemeConfigDao.findByCodeAndActive("backoffice.theme.level", true);
        return configs.isEmpty() ? BACKOFFICE_THEME_LEVEL_VALUE_DEFAULT : BackofficeThemeLevel.valueOf(((BackofficeThemeConfigModel)configs.get(0)).getContent());
    }


    public void setThemeLevel(BackofficeThemeLevel themeLevel)
    {
        saveOrCreateConfig("backoffice.theme.level", themeLevel.name());
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setBackofficeThemeConfigDao(BackofficeThemeConfigDao backofficeThemeConfigDao)
    {
        this.backofficeThemeConfigDao = backofficeThemeConfigDao;
    }


    public void setDefaultThemeCode(String defaultThemeCode)
    {
        this.defaultThemeCode = defaultThemeCode;
    }


    public void setBackofficeThemeDao(BackofficeThemeDao backofficeThemeDao)
    {
        this.backofficeThemeDao = backofficeThemeDao;
    }
}
