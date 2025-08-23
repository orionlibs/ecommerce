package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cockpit.CockpitConfigurationService;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.cockpit.model.CockpitUIScriptConfigMediaModel;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.BaseFallbackEnabledUIConfigurationFactory;
import de.hybris.platform.cockpit.services.config.ConfigurationPersistingStrategy;
import de.hybris.platform.cockpit.services.config.DefaultUIRole;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfigurationFactory;
import de.hybris.platform.cockpit.services.config.UIConfigurationException;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.config.UIRole;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.AfterDoneWizardScript;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xml.sax.InputSource;

public class DefaultUIConfigurationService extends AbstractServiceImpl implements UIConfigurationService, ApplicationContextAware
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUIConfigurationService.class);
    private final Map<Class, ConfigurationPersistingStrategy> persistingStrategies = (Map)new HashMap<>();
    private LoginService loginService;
    private ApplicationContext applicationContext;
    private Collection<UIComponentConfigurationFactory> uiComponentFactories;
    private UIRole fallbackRole;
    private final Map<CacheKey, UIComponentConfiguration> defaultComponentCache = new HashMap<>();
    private UIComponentCache componentCache;
    private ConfigurationPersistingStrategy defaultPersistingStrategy;
    private MyEnumerationInvalidationListener myInvalidationListener;
    private CockpitConfigurationService cockpitConfigurationService;
    private UserService userService;
    private SessionService sessionService;
    private MediaService mediaService;
    private static final String SESSION_UI_ROLE = "cockpitUserGroup";


    public UIRole getFallbackRole()
    {
        if(this.fallbackRole == null)
        {
            throw new UIConfigurationException("No default UI role specified");
        }
        return this.fallbackRole;
    }


    public UIRole getSessionRole()
    {
        UIRole userRole = (UIRole)getSessionService().getAttribute("cockpitUserGroup");
        if(userRole == null)
        {
            List<UIRole> roles = getPossibleRoles();
            if(!roles.isEmpty())
            {
                userRole = roles.get(0);
            }
            else
            {
                userRole = getFallbackRole();
            }
            setSessionRole(userRole);
        }
        return userRole;
    }


    public void setSessionRole(UIRole role)
    {
        getSessionService().setAttribute("cockpitUserGroup", role);
    }


    public void setSessionRole(String role)
    {
        if(role != null)
        {
            setSessionRole((UIRole)new DefaultUIRole(role));
        }
        else
        {
            setSessionRole((UIRole)null);
        }
    }


    public <T extends UIComponentConfiguration> T getComponentConfiguration(UIRole role, ObjectTemplate objectTemplate, String code, Class<T> expectedClass)
    {
        UIComponentConfiguration uIComponentConfiguration;
        if(objectTemplate == null)
        {
            throw new IllegalArgumentException("objectTemplate must not be null");
        }
        if(code == null)
        {
            throw new IllegalArgumentException("code must not be null");
        }
        if(expectedClass == null)
        {
            throw new IllegalArgumentException("expectedClass must not be null");
        }
        String roleName = (role != null) ? role.getName() : null;
        T componentConfig = null;
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("*** Trying to load configuration for role [%s], object template [%s], code [%s] and class [%s] ***", new Object[] {roleName, objectTemplate, code, expectedClass}));
        }
        UIComponentCache.CacheKey cacheKey = new UIComponentCache.CacheKey(roleName, objectTemplate, code, expectedClass);
        if(this.componentCache != null)
        {
            uIComponentConfiguration = this.componentCache.getComponentConfiguration(cacheKey);
            if(LOG.isDebugEnabled() && uIComponentConfiguration != null)
            {
                LOG.debug(String.format("Found in cache: configuration for role [%s], object template [%s], code [%s] and class [%s]", new Object[] {roleName, objectTemplate, code, expectedClass}));
            }
        }
        if(uIComponentConfiguration == null)
        {
            for(ObjectType type : getTypePath(objectTemplate))
            {
                String typeCode = type.getCode();
                if(this.loginService.getCurrentSessionSettings().getUser() != null)
                {
                    String userID = this.loginService.getCurrentSessionSettings().getUser().getUid();
                    uIComponentConfiguration = createComponentConfiguration(userID, typeCode, objectTemplate.getCode(), code, expectedClass);
                    if(LOG.isDebugEnabled() && uIComponentConfiguration != null)
                    {
                        LOG.debug(String.format("Found personalized configuration for user [%s], object template [%s], code [%s], type code [%s] and class [%s]", new Object[] {userID, objectTemplate, code, typeCode, expectedClass}));
                    }
                }
                if(uIComponentConfiguration == null)
                {
                    uIComponentConfiguration = createComponentConfiguration(roleName, typeCode, objectTemplate.getCode(), code, expectedClass);
                    if(LOG.isDebugEnabled() && uIComponentConfiguration != null)
                    {
                        LOG.debug(String.format("Found configuration for role [%s], object template [%s], code [%s], type code [%s] and class [%s]", new Object[] {roleName, objectTemplate, code, typeCode, expectedClass}));
                    }
                }
                if(uIComponentConfiguration == null)
                {
                    uIComponentConfiguration = createComponentConfiguration(getFallbackRole().getName(), typeCode, objectTemplate.getCode(), code, expectedClass);
                    if(LOG.isDebugEnabled() && uIComponentConfiguration != null)
                    {
                        LOG.debug(String.format("Found configuration for fallback role [%s], object template [%s], code [%s], type code [%s] and class [%s]", new Object[] {getFallbackRole().getName(), objectTemplate, code, typeCode, expectedClass}));
                    }
                }
                if(uIComponentConfiguration == null)
                {
                    uIComponentConfiguration = createComponentConfiguration(null, typeCode, objectTemplate.getCode(), code, expectedClass);
                    if(LOG.isDebugEnabled() && uIComponentConfiguration != null)
                    {
                        LOG.debug(String.format("Found configuration for no role, object template [%s], code [%s], type code [%s] and class [%s]", new Object[] {objectTemplate, code, typeCode, expectedClass}));
                    }
                }
                if(uIComponentConfiguration != null)
                {
                    if(!type.equals(objectTemplate))
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(String.format("No configuration for role [%s], object template [%s], code [%s], type code [%s] and class [%s]. Trying super type.", new Object[] {roleName, typeCode, code, typeCode, expectedClass}));
                        }
                    }
                    break;
                }
            }
            if(uIComponentConfiguration == null)
            {
                uIComponentConfiguration = getDefaultUIComponentConfiguration(objectTemplate, expectedClass);
                if(LOG.isDebugEnabled() && uIComponentConfiguration != null)
                {
                    LOG.debug(String.format("Using default fallback configuration for role [%s], object template [%s], code [%s] and class [%s]", new Object[] {roleName, objectTemplate, code, expectedClass}));
                }
                if(uIComponentConfiguration == null)
                {
                    LOG.error(String.format("No default fallback configuration for role [%s], object template [%s], code [%s] and class [%s]", new Object[] {roleName, objectTemplate, code, expectedClass}));
                }
            }
            if(this.componentCache != null && uIComponentConfiguration != null)
            {
                this.componentCache.addComponentConfiguration(cacheKey, uIComponentConfiguration);
            }
        }
        return (T)uIComponentConfiguration;
    }


    protected List<ObjectType> getTypePath(ObjectTemplate template)
    {
        List<ObjectType> path = new ArrayList<>();
        path.add(template);
        if(!template.isDefaultTemplate())
        {
            path.add(template.getBaseType());
        }
        BaseType baseType = template.getBaseType();
        List<ObjectType> supertypes = TypeTools.getAllSupertypes((ObjectType)baseType);
        Collections.reverse(supertypes);
        path.addAll(supertypes);
        return path;
    }


    public <T extends UIComponentConfiguration> T getComponentConfiguration(ObjectTemplate template, String code, Class<T> expectedClass)
    {
        return getComponentConfiguration(getSessionRole(), template, code, expectedClass);
    }


    public List<UIRole> getPossibleRoles()
    {
        UserModel user = this.loginService.getCurrentSessionSettings().getUser();
        return getPossibleRoles(user);
    }


    public List<UIRole> getPossibleRoles(UserModel user)
    {
        UIRole fallbackRole = getFallbackRole();
        List<String> roleNames = getCockpitConfigurationService().getRoleNamesForPrincipal((PrincipalModel)user);
        List<UIRole> roles = new ArrayList<>();
        for(String roleName : roleNames)
        {
            if(!roleName.equals(fallbackRole.getName()) && !roleName.equals(user.getUid()))
            {
                roles.add(new DefaultUIRole(roleName));
            }
        }
        return roles;
    }


    private <T extends UIComponentConfiguration> T createComponentConfiguration(String roleName, String templateCode, String originalTemplateCode, String code, Class<T> expectedClass)
    {
        UserGroupModel userGroupModel;
        PrincipalModel principalModel = null;
        if(!StringUtils.isEmpty(roleName))
        {
            try
            {
                UserModel userModel = getUserService().getUserForUID(roleName);
            }
            catch(UnknownIdentifierException uie)
            {
                try
                {
                    userGroupModel = getUserService().getUserGroupForUID(roleName);
                }
                catch(UnknownIdentifierException uie2)
                {
                    LOG.error("No principal found for uid: " + roleName);
                }
            }
        }
        CockpitUIComponentConfigurationModel configItemModel = null;
        try
        {
            configItemModel = getCockpitConfigurationService().getComponentConfiguration((PrincipalModel)userGroupModel, templateCode, code);
        }
        catch(UnknownIdentifierException uie)
        {
            return null;
        }
        MediaModel mediaModel = configItemModel.getMedia();
        if(LOG.isDebugEnabled())
        {
            String mediaFileName = (mediaModel != null) ? mediaModel.getRealFileName() : null;
            LOG.debug(String.format("Configuration item found for role [%s], object template [%s] and code [%s]. PK = [%s], media file = [%s], factory bean name = [%s]", new Object[] {roleName, templateCode, code, configItemModel
                            .getPk(), mediaFileName, configItemModel.getFactoryBean()}));
        }
        if(configItemModel.getFactoryBean() == null)
        {
            LOG.error(String.format("Configuration item for role [%s], object template [%s] and code [%s] with PK = [%s] has no factory bean", new Object[] {roleName, templateCode, code, configItemModel
                            .getPk()}));
            return null;
        }
        if(mediaModel == null)
        {
            LOG.error(String.format("Configuration item for role [%s], object template [%s] and code [%s] with PK = [%s] has no media", new Object[] {roleName, templateCode, code, configItemModel
                            .getPk()}));
            return null;
        }
        InputStream inStream = null;
        T component = null;
        try
        {
            UIComponentConfigurationFactory<UIComponentConfiguration> factory = (UIComponentConfigurationFactory<UIComponentConfiguration>)this.applicationContext.getBean(configItemModel.getFactoryBean());
            ObjectTemplate objectTemplate = this.typeService.getObjectTemplate(configItemModel.getObjectTemplateCode());
            ObjectTemplate originalTemplate = this.typeService.getObjectTemplate(originalTemplateCode);
            inStream = this.mediaService.getStreamFromMedia(mediaModel);
            UIComponentConfiguration uIComponentConfiguration = factory.create(objectTemplate, originalTemplate, new InputSource(inStream));
            if(uIComponentConfiguration == null)
            {
                LOG.error("Factory " + configItemModel.getFactoryBean() + " for " + configItemModel.getCode() + " for principal " + configItemModel
                                .getPrincipal().getUid() + " returned null");
            }
            else if(!expectedClass.isAssignableFrom(expectedClass))
            {
                LOG.error("Unexpected class for ui component configuration with code " + configItemModel.getCode() + " for principal " + configItemModel
                                .getPrincipal().getUid() + ": " + uIComponentConfiguration.getClass().getName());
                uIComponentConfiguration = null;
            }
        }
        catch(Exception e)
        {
            LOG.error("Cannot create ui component configuration with code " + configItemModel.getCode() + ": " + e.getMessage(), e);
            component = null;
        }
        finally
        {
            IOUtils.closeQuietly(inStream);
        }
        if(component instanceof DefaultWizardConfiguration)
        {
            if(!(mediaModel instanceof CockpitUIScriptConfigMediaModel))
            {
                AfterDoneWizardScript wizardScript = ((DefaultWizardConfiguration)component).getWizardScript();
                if(wizardScript != null && StringUtils.isNotBlank(wizardScript.getContent()))
                {
                    LOG.error("Configuration media is not of type 'CockpitUIScriptConfigMedia', wizard script will be removed.");
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Script content was:\n" + wizardScript.getContent());
                    }
                    ((DefaultWizardConfiguration)component).setWizardScript(null);
                }
            }
            else if(!((CockpitUIScriptConfigMediaModel)mediaModel).getAllowScriptEvaluation().booleanValue())
            {
                ((DefaultWizardConfiguration)component).setWizardScript(null);
                LOG.warn("Script evaluation was disabled, wizard script will be removed.");
            }
        }
        return component;
    }


    private <T extends UIComponentConfiguration> T getDefaultUIComponentConfiguration(ObjectTemplate objectTemplate, Class<T> expectedClass)
    {
        if(expectedClass == null)
        {
            throw new NullPointerException("No expectedClass given");
        }
        if(objectTemplate == null)
        {
            throw new NullPointerException("No objectTemplate given");
        }
        CacheKey key = new CacheKey(objectTemplate, expectedClass);
        UIComponentConfiguration uIComponentConfiguration = this.defaultComponentCache.get(key);
        if(uIComponentConfiguration == null)
        {
            for(UIComponentConfigurationFactory factory : getUiComponentFactories())
            {
                if(expectedClass.isAssignableFrom(factory.getComponentClass()))
                {
                    if(factory instanceof BaseFallbackEnabledUIConfigurationFactory)
                    {
                        BaseConfiguration baseConfiguration = getComponentConfiguration(objectTemplate, "base", BaseConfiguration.class);
                        uIComponentConfiguration = ((BaseFallbackEnabledUIConfigurationFactory)factory).createDefault(objectTemplate, baseConfiguration);
                    }
                    else
                    {
                        uIComponentConfiguration = factory.createDefault(objectTemplate);
                    }
                    this.defaultComponentCache.put(key, uIComponentConfiguration);
                    break;
                }
            }
        }
        if(uIComponentConfiguration == null)
        {
            throw new UIConfigurationException(String.format("No default component configuration for class %s and object template with code %s", new Object[] {expectedClass
                            .getName(), objectTemplate
                            .getCode()}));
        }
        return (T)uIComponentConfiguration;
    }


    @Required
    public void setLoginService(LoginService loginService)
    {
        this.loginService = loginService;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public void setFallbackRoleName(String roleName)
    {
        this.fallbackRole = (UIRole)new DefaultUIRole(roleName);
    }


    public void setUiComponentCache(UIComponentCache componentCache)
    {
        this.componentCache = componentCache;
    }


    private Collection<UIComponentConfigurationFactory> getUiComponentFactories()
    {
        if(this.uiComponentFactories == null)
        {
            this.uiComponentFactories = this.applicationContext.getBeansOfType(UIComponentConfigurationFactory.class).values();
            if(this.uiComponentFactories == null)
            {
                LOG.error("Application Context returned null result for getBeansOfType with parameter " + UIComponentConfigurationFactory.class
                                .getName());
                this.uiComponentFactories = Collections.emptyList();
            }
            else if(this.uiComponentFactories.isEmpty())
            {
                LOG.warn("Application Context returned empty result for getBeansOfType with parameter " + UIComponentConfigurationFactory.class
                                .getName());
            }
        }
        return this.uiComponentFactories;
    }


    public <T extends UIComponentConfiguration> void setLocalComponentConfiguration(T configuration, UserModel user, ObjectTemplate objectTemplate, String code, Class<T> expectedClass)
    {
        if(configuration == null)
        {
            throw new IllegalArgumentException("configuration must not be null");
        }
        if(user == null)
        {
            throw new IllegalArgumentException("user must not be null");
        }
        if(objectTemplate == null)
        {
            throw new IllegalArgumentException("objectTemplate must not be null");
        }
        if(code == null)
        {
            throw new IllegalArgumentException("code must not be null");
        }
        if(expectedClass == null)
        {
            throw new IllegalArgumentException("expectedClass must not be null");
        }
        UIRole role = getSessionRole();
        String roleName = (role != null) ? role.getName() : null;
        UIComponentCache.CacheKey cacheKey = new UIComponentCache.CacheKey(roleName, objectTemplate, code, expectedClass);
        if(this.componentCache != null)
        {
            this.componentCache.addComponentConfiguration(cacheKey, (UIComponentConfiguration)configuration);
        }
        ConfigurationPersistingStrategy persistingStrategy = this.persistingStrategies.get(expectedClass);
        if(persistingStrategy == null)
        {
            persistingStrategy = getDefaultPersistingStrategy();
        }
        if(persistingStrategy != null)
        {
            persistingStrategy.persistComponentConfiguration((UIComponentConfiguration)configuration, user, objectTemplate, code);
        }
    }


    public void initPersistingStrategies()
    {
        Map<String, ConfigurationPersistingStrategy> beansOfType = this.applicationContext.getBeansOfType(ConfigurationPersistingStrategy.class);
        for(Map.Entry<String, ConfigurationPersistingStrategy> entry : beansOfType.entrySet())
        {
            ConfigurationPersistingStrategy strategy = entry.getValue();
            this.persistingStrategies.put(strategy.getComponentClass(), strategy);
        }
    }


    public void setDefaultPersistingStrategy(ConfigurationPersistingStrategy defaultPersistingStrategy)
    {
        this.defaultPersistingStrategy = defaultPersistingStrategy;
    }


    public ConfigurationPersistingStrategy getDefaultPersistingStrategy()
    {
        return this.defaultPersistingStrategy;
    }


    public void init()
    {
        initPersistingStrategies();
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        this.myInvalidationListener = new MyEnumerationInvalidationListener(this, topic);
        topic.addInvalidationListener((InvalidationListener)this.myInvalidationListener);
    }


    public void clear()
    {
        if(this.myInvalidationListener != null)
        {
            this.myInvalidationListener.topic.removeInvalidationListener((InvalidationListener)this.myInvalidationListener);
            this.myInvalidationListener = null;
        }
    }


    protected CockpitConfigurationService getCockpitConfigurationService()
    {
        return this.cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
