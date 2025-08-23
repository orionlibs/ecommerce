package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.hmc.jalo.SavedValues;
import de.hybris.platform.hmc.jalo.UserProfile;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSessionListener;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.servicelayer.event.events.AfterInitializationStartEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemCreationEvent;
import de.hybris.platform.servicelayer.event.events.AfterItemRemovalEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionAttributeChangeEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionCreationEvent;
import de.hybris.platform.servicelayer.event.events.AfterSessionUserChangeEvent;
import de.hybris.platform.servicelayer.event.events.AfterTenantInitializationClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AfterTenantRestartClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.BeforeSessionCloseEvent;
import de.hybris.platform.servicelayer.event.events.InvalidateModelConverterRegistryEvent;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.SingletonCreator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ServicelayerManager extends GeneratedServicelayerManager implements JaloSessionListener
{
    static final Logger LOG = Logger.getLogger(ServicelayerManager.class.getName());
    private final EventService eventService;
    private final EnumerationService enumerationService;
    protected static final OneToManyHandler<SavedValues> ITEMSAVEDVALUESRELATIONSAVEDVALUESHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.SAVEDVALUES, false, "modifiedItem"
                    .intern(), "modifiedItemPOS".intern(), true, true, 1);
    private final List<String> sessionArtNamesFilter;
    private final InvalidationListener invalidationListener;
    private final SingletonCreator.Creator<Set<Language>> WRITEABLE_LANGUAGES_CREATOR;
    private final SingletonCreator.Creator<Set<Language>> READABLE_LANGUAGES_CREATOR;
    private final SingletonCreator.Creator<Set<Language>> LANGUAGES_CREATOR;


    public ServicelayerManager()
    {
        this.sessionArtNamesFilter = Arrays.asList(Config.getString("servicelayer.sessionatrchangeevents", "user,currency,language")
                        .split(","));
        this.invalidationListener = (InvalidationListener)new Object(this);
        this.WRITEABLE_LANGUAGES_CREATOR = (SingletonCreator.Creator<Set<Language>>)new Object(this);
        this.READABLE_LANGUAGES_CREATOR = (SingletonCreator.Creator<Set<Language>>)new Object(this);
        this.LANGUAGES_CREATOR = (SingletonCreator.Creator<Set<Language>>)new Object(this);
        this.eventService = (EventService)Registry.getCoreApplicationContext().getBean("eventService", EventService.class);
        this.enumerationService = (EnumerationService)Registry.getCoreApplicationContext().getBean("enumerationService", EnumerationService.class);
        JaloConnection.getInstance().registerJaloSessionListener(this);
    }


    public void init()
    {
        registerInvalidationListener();
    }


    private void registerInvalidationListener()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener(this.invalidationListener);
    }


    public String getName()
    {
        return "core";
    }


    public static ServicelayerManager getInstance()
    {
        return (ServicelayerManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("core");
    }


    protected void notifyAttributesChanged()
    {
        if(eventsActive())
        {
            InvalidateModelConverterRegistryEvent evt = new InvalidateModelConverterRegistryEvent();
            evt.setRefresh(true);
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        if(eventsActive())
        {
            if(item instanceof ComposedType)
            {
                InvalidateModelConverterRegistryEvent evt = new InvalidateModelConverterRegistryEvent();
                ComposedType encType = (ComposedType)item;
                evt.setComposedTypeCode(encType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
                evt.setComposedClass(encType.getJaloClass());
                this.eventService.publishEvent((AbstractEvent)evt);
            }
            else if(item instanceof AttributeDescriptor)
            {
                InvalidateModelConverterRegistryEvent evt = new InvalidateModelConverterRegistryEvent();
                ComposedType encType = ((AttributeDescriptor)item).getEnclosingType();
                evt.setComposedTypeCode(encType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
                evt.setComposedClass(encType.getJaloClass());
                evt.setRefresh(true);
                this.eventService.publishEvent((AbstractEvent)evt);
            }
            this.eventService.publishEvent((AbstractEvent)new AfterItemRemovalEvent((Serializable)item.getPK()));
        }
    }


    public void afterItemCreation(SessionContext ctx, ComposedType type, Item createdItem, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
        if(eventsActive())
        {
            if(createdItem instanceof ComposedType)
            {
                InvalidateModelConverterRegistryEvent invalidateModelConverterRegistryEvent = new InvalidateModelConverterRegistryEvent();
                ComposedType encType = (ComposedType)createdItem;
                invalidateModelConverterRegistryEvent.setComposedTypeCode(encType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
                invalidateModelConverterRegistryEvent.setComposedClass(encType.getJaloClass());
                this.eventService.publishEvent((AbstractEvent)invalidateModelConverterRegistryEvent);
            }
            else if(createdItem instanceof AttributeDescriptor)
            {
                InvalidateModelConverterRegistryEvent invalidateModelConverterRegistryEvent = new InvalidateModelConverterRegistryEvent();
                ComposedType encType = ((AttributeDescriptor)createdItem).getEnclosingType();
                invalidateModelConverterRegistryEvent.setComposedTypeCode(encType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
                invalidateModelConverterRegistryEvent.setComposedClass(encType.getJaloClass());
                invalidateModelConverterRegistryEvent.setRefresh(true);
                this.eventService.publishEvent((AbstractEvent)invalidateModelConverterRegistryEvent);
            }
            AfterItemCreationEvent evt = new AfterItemCreationEvent((Serializable)createdItem.getPK());
            evt.setTypeCode(type.getCode());
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void afterSessionCreation(JaloSession session)
    {
        if(eventsActive())
        {
            this.eventService.publishEvent((AbstractEvent)new AfterSessionCreationEvent((Serializable)session));
        }
    }


    public void afterSessionUserChange(JaloSession session, User previous)
    {
        if(eventsActive())
        {
            AfterSessionUserChangeEvent evt = new AfterSessionUserChangeEvent((Serializable)session);
            evt.setPreviousUserUID((previous == null) ? null : previous.getUid());
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    public void afterSessionAttributeChange(JaloSession session, String attributeName, Object value)
    {
        if(eventsActive() && isEventActiveAttribute(attributeName) && session != null)
        {
            AfterSessionAttributeChangeEvent evt = new AfterSessionAttributeChangeEvent((Serializable)session);
            evt.setAttributeName(attributeName);
            evt.setValue(value);
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    protected boolean isEventActiveAttribute(String attributeName)
    {
        return this.sessionArtNamesFilter.contains(attributeName);
    }


    public void beforeSessionClose(JaloSession session)
    {
        if(eventsActive())
        {
            this.eventService.publishEvent((AbstractEvent)new BeforeSessionCloseEvent((Serializable)session));
        }
    }


    public void notifyTenantRestart(Tenant tenant)
    {
        if(eventsActive())
        {
            this.eventService.publishEvent((AbstractEvent)new AfterTenantRestartClusterAwareEvent(tenant));
        }
    }


    public void notifyInitializationEnd(Map<String, String> params, JspContext ctx) throws Exception
    {
        if(eventsActive())
        {
            AfterInitializationEndEvent evt = new AfterInitializationEndEvent();
            evt.setCtx(ctx);
            evt.setParams(params);
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    public void notifyInitializationStart(Map<String, String> params, JspContext ctx) throws Exception
    {
        if(eventsActive())
        {
            AfterInitializationStartEvent evt = new AfterInitializationStartEvent();
            evt.setParams(params);
            evt.setCtx(ctx);
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    private boolean eventsActive()
    {
        return !RedeployUtilities.isShutdownInProgress();
    }


    public List<String> getSessionArtNamesFilter()
    {
        return this.sessionArtNamesFilter;
    }


    public Set<SavedValues> getSavedValues(Item item)
    {
        return (Set<SavedValues>)ITEMSAVEDVALUESRELATIONSAVEDVALUESHANDLER.getValues(getSession().getSessionContext(), item);
    }


    public UserProfile getOrCreateUserProfile()
    {
        User user = JaloSession.getCurrentSession().getUser();
        UserProfile profile = getUserprofile(user);
        if(profile == null)
        {
            profile = createUserProfile(Collections.singletonMap(UserProfile.OWNER, user));
            setUserprofile(user, profile);
        }
        return profile;
    }


    public Set<Language> getAllWritableLanguages()
    {
        return (Set<Language>)getTenant().getCache().getRequestCacheContent(this.WRITEABLE_LANGUAGES_CREATOR);
    }


    public Set<Language> getAllReadableLanguages()
    {
        return (Set<Language>)getTenant().getCache().getRequestCacheContent(this.READABLE_LANGUAGES_CREATOR);
    }


    public Set<Language> getAllLanguages()
    {
        return (Set<Language>)getTenant().getCache().getRequestCacheContent(this.LANGUAGES_CREATOR);
    }


    public UserProfile getUserprofile(User item)
    {
        return getUserprofile(getSession().getSessionContext(), item);
    }


    public UserProfile getUserprofile(SessionContext ctx, User item)
    {
        return (UserProfile)item.getProperty(ctx, GeneratedCoreConstants.TC.USERPROFILE);
    }


    public void setUserprofile(SessionContext ctx, User item, UserProfile value)
    {
        (new Object(this, item))
                        .setValue(ctx, value);
    }


    public void setUserprofile(User item, UserProfile value)
    {
        setUserprofile(getSession().getSessionContext(), item, value);
    }


    public void notifyClusterAboutTenantInitialization(Tenant tenant)
    {
        Objects.requireNonNull(tenant, "Tenant must not be null");
        if(eventsActive())
        {
            AfterTenantInitializationClusterAwareEvent evt = new AfterTenantInitializationClusterAwareEvent(tenant);
            this.eventService.publishEvent((AbstractEvent)evt);
        }
    }


    public void publishEvent(AbstractEvent event)
    {
        Objects.requireNonNull(event, "event must not be null");
        if(eventsActive())
        {
            this.eventService.publishEvent(event);
        }
    }


    public <T extends de.hybris.platform.core.HybrisEnumValue> T getServiceLayerEnumerationValue(String enumerationCode, String valueCode)
    {
        return (T)this.enumerationService.getEnumerationValue(enumerationCode, valueCode);
    }


    public void checkBeforeInitialization(JspContext ctx, boolean forceInit) throws Exception
    {
        super.checkBeforeInitialization(ctx, forceInit);
        checkMediaLocationHashSalt(forceInit);
    }


    private void checkMediaLocationHashSalt(boolean isInit) throws ConsistencyCheckException
    {
        if(!isInit)
        {
            return;
        }
        String salt = Config.getString("media.default.storage.location.hash.salt", null);
        if(StringUtils.isBlank(salt))
        {
            handleEmptySalt();
        }
        if("35b5cd0da3121fc53b4bc84d0c8af2e81".equals(StringUtils.trim(salt)))
        {
            handleDefaultSalt();
        }
    }


    private void handleEmptySalt() throws ConsistencyCheckException
    {
        String msg = "There is no value provided for property: media.default.storage.location.hash.salt.";
        logError("There is no value provided for property: media.default.storage.location.hash.salt.");
    }


    private void logError(String msg) throws ConsistencyCheckException
    {
        String illegalValueBehavior = StringUtils.trim(StringUtils.lowerCase(
                        Config.getString("media.default.storage.location.hash.illegalValueBehavior", "warn"), Locale.ROOT));
        if("error".equals(illegalValueBehavior))
        {
            LOG.error(msg);
            throw new ConsistencyCheckException(msg, 0);
        }
        if("warn".equals(illegalValueBehavior))
        {
            LOG.warn(msg);
        }
    }


    private void handleDefaultSalt() throws ConsistencyCheckException
    {
        logError("The value of media.default.storage.location.hash.salt property has been left unchanged. This could reduce the security of medias' urls");
    }
}
