package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.reports.jasperreports.JasperReportsRefresh;
import de.hybris.platform.cockpit.services.NewItemService;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.services.search.SearchService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.undo.UndoManager;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.session.PerspectivePluginList;
import de.hybris.platform.cockpit.session.RequestHandler;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.CockpitTestIDGenerator;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.IdGenerator;
import org.zkoss.zk.ui.sys.WebAppCtrl;

public class UISessionImpl implements UISession, ApplicationContextAware
{
    private static final String DEFAULT_USE_TEST_IDS = "default.useTestIds";
    private static final String ON_LATER_COCKPIT_EVENT = "onLaterCockpitEvent";
    private static final Logger LOG = LoggerFactory.getLogger(UISessionImpl.class);
    private SystemService systemService;
    private TypeService cockpitTypeService;
    private NewItemService newItemService;
    private LabelService labelService;
    private SavedQueryService savedQueryService;
    private SessionService sessionService;
    private ObjectValueHandlerRegistry valueHandlerRegistry;
    private boolean sendEventsImmediately = false;
    private UserModel currentUser;
    private UICockpitPerspective currentPerspective;
    private Component perspectiveContainer;
    private List<UISessionListener> listeners;
    private Set<String> supportedLanguageIsos;
    private List<UICockpitPerspective> availablePerspectives;
    private List<UICockpitPerspective> avPerspUnrestricted;
    private String globalDataLanguageIso;
    private SearchService searchService;
    private UndoManager undoManager;
    private ModelService modelService;
    private UIConfigurationService configurationService;
    private UIAccessRightService accessRightService;
    private LocalizationService localizationService;
    private CommonI18NService commonI18NService;
    private JasperReportsRefresh jasperReportsRefresh;
    private Boolean usingTestIDs = null;
    private boolean dragOverPerspEnabled = false;
    private boolean cachePerspectivesEnabled = false;
    private final Set<String> perspectiveUids = new HashSet<>();
    private final Set<CockpitEvent> eventCache = new HashSet<>();
    private final EventListener onLaterListener = (EventListener)new Object(this);
    private List<CatalogVersionModel> selectedCatalogVersion = new ArrayList<>();
    private RequestHandler reqHandler = null;
    private final List<PushCreationContainer> pushContainers = new ArrayList<>();
    private ApplicationContext applicationContext;


    public List<CatalogVersionModel> getSelectedCatalogVersions()
    {
        return this.selectedCatalogVersion;
    }


    public void setSelectedCatalogVersions(List<CatalogVersionModel> catalogVersions)
    {
        this.selectedCatalogVersion = catalogVersions;
    }


    public List<UICockpitPerspective> getAvailablePerspectives()
    {
        return (this.availablePerspectives != null) ? Collections.<UICockpitPerspective>unmodifiableList(this.availablePerspectives) :
                        Collections.EMPTY_LIST;
    }


    @Required
    public void setAvailablePerspectives(List<UICockpitPerspective> availablePerspectives)
    {
        this.avPerspUnrestricted = availablePerspectives;
        updateRestrictedPerspectives();
        initSession();
    }


    public void setSendEventsImmediately(boolean immediately)
    {
        this.sendEventsImmediately = immediately;
    }


    protected void updateRestrictedPerspectives()
    {
        if(this.currentUser != null)
        {
            this.availablePerspectives = new ArrayList<>();
            if(this.avPerspUnrestricted != null && !this.avPerspUnrestricted.isEmpty())
            {
                for(UICockpitPerspective persp : this.avPerspUnrestricted)
                {
                    if(this.accessRightService.canRead(this.currentUser, persp.getUid()))
                    {
                        this.availablePerspectives.add(persp);
                    }
                }
            }
        }
        else
        {
            this.availablePerspectives = this.avPerspUnrestricted;
        }
        this.perspectiveUids.clear();
        if(this.availablePerspectives != null)
        {
            for(UICockpitPerspective perspective : this.availablePerspectives)
            {
                this.perspectiveUids.add(perspective.getUid());
            }
        }
    }


    public void registerPerspectiveContainer(Component comp)
    {
        this.perspectiveContainer = comp;
    }


    public UICockpitPerspective getCurrentPerspective()
    {
        if(this.currentPerspective == null && !getAvailablePerspectives().isEmpty())
        {
            setCurrentPerspective(getAvailablePerspectives().get(0));
        }
        return this.currentPerspective;
    }


    public void setCurrentPerspective(UICockpitPerspective perspective)
    {
        setCurrentPerspective(perspective, Collections.EMPTY_MAP);
    }


    public void setCurrentPerspective(UICockpitPerspective perspective, Map<String, ? extends Object> params)
    {
        if(perspective == null)
        {
            throw new IllegalArgumentException("perspective cannot be null");
        }
        if(this.currentPerspective != perspective && (this.currentPerspective == null || !this.currentPerspective.equals(perspective)))
        {
            UICockpitPerspective old = this.currentPerspective;
            this.currentPerspective = perspective;
            this.currentPerspective.initialize((params == null) ? Collections.EMPTY_MAP : params);
            if(this.perspectiveContainer != null && !UITools.isFromOtherDesktop(this.perspectiveContainer))
            {
                Events.postEvent("onPerspectiveChanged", this.perspectiveContainer, null);
                if(isDragOverPerspectivesEnabled())
                {
                    for(Object perspComp : this.perspectiveContainer.getChildren())
                    {
                        if(perspComp instanceof HtmlBasedComponent)
                        {
                            ((HtmlBasedComponent)perspComp).setVisible(false);
                        }
                    }
                }
                else
                {
                    this.perspectiveContainer.getChildren().clear();
                }
            }
            if(old instanceof AbstractUIPerspective)
            {
                ((AbstractUIPerspective)old).onHide();
            }
            if(this.currentPerspective instanceof AbstractUIPerspective)
            {
                ((AbstractUIPerspective)this.currentPerspective).onShow();
            }
            firePerspectiveChanged(old);
        }
    }


    protected void firePerspectiveChanged(UICockpitPerspective old)
    {
        for(UISessionListener l : (UISessionListener[])getListeners(false).<UISessionListener>toArray(new UISessionListener[getListeners(false).size()]))
        {
            l.perspectiveChanged(old, this.currentPerspective);
        }
    }


    protected List<UISessionListener> getListeners(boolean create)
    {
        return (this.listeners != null) ? this.listeners : (create ? (this.listeners = new ArrayList<>()) : Collections.EMPTY_LIST);
    }


    @Required
    public void setInitialSessionListeners(Collection<UISessionListener> listeners)
    {
        getListeners(true).addAll(listeners);
    }


    public void addSessionListener(UISessionListener listener)
    {
        getListeners(false).add(listener);
    }


    public void removeSessionListener(UISessionListener listener)
    {
        getListeners(false).remove(listener);
    }


    public void clearSessionListeners()
    {
        getListeners(false).clear();
    }


    protected void fireAfterLogin()
    {
        for(UISessionListener l : (UISessionListener[])getListeners(false).<UISessionListener>toArray(new UISessionListener[getListeners(false).size()]))
        {
            l.afterLogin(getUser());
        }
    }


    protected void fireBeforeLogout()
    {
        for(UISessionListener l : (UISessionListener[])getListeners(false).<UISessionListener>toArray(new UISessionListener[getListeners(false).size()]))
        {
            l.beforeLogout(getUser());
        }
    }


    protected void fireDataLanguageChanged()
    {
        for(UISessionListener l : (UISessionListener[])getListeners(false).<UISessionListener>toArray(new UISessionListener[getListeners(false).size()]))
        {
            l.globalDataLanguageChanged();
        }
    }


    public String getLanguageIso()
    {
        String languageCode = (String)Executions.getCurrent().getAttribute("languageIso");
        if(languageCode == null)
        {
            LanguageModel lang = this.sessionService.hasCurrentSession() ? this.commonI18NService.getCurrentLanguage() : null;
            languageCode = (lang != null) ? lang.getIsocode() : Locales.getCurrent().toString();
            Executions.getCurrent().setAttribute("languageIso", languageCode);
        }
        return languageCode;
    }


    public Set<String> getSupportedLanguageIsoCodes()
    {
        if(this.supportedLanguageIsos == null)
        {
            this.supportedLanguageIsos = new TreeSet<>((Comparator<? super String>)new Object(this));
            for(LanguageModel lang : getSystemService().getAvailableLanguages())
            {
                this.supportedLanguageIsos.add(lang.getIsocode());
            }
        }
        return this.supportedLanguageIsos;
    }


    public UserModel getUser()
    {
        return this.currentUser;
    }


    public void setUser(UserModel currentUser)
    {
        if(this.currentUser != currentUser && (this.currentUser == null || !this.currentUser.equals(currentUser)))
        {
            this.currentUser = currentUser;
            updateRestrictedPerspectives();
        }
    }


    public void setUserByUID(String uid)
    {
        setUser(getSystemService().getUserByUID(uid));
    }


    public void login(UserModel user)
    {
        setUser(user);
    }


    public void logout()
    {
        clearSessionListeners();
        setUser(null);
        this.currentPerspective = null;
    }


    public SystemService getSystemService()
    {
        return this.systemService;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    @Required
    public void setNewItemService(NewItemService newItemService)
    {
        this.newItemService = newItemService;
    }


    public NewItemService getNewItemService()
    {
        return this.newItemService;
    }


    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }


    @Required
    public void setValueHandlerRegistry(ObjectValueHandlerRegistry objectValueRegistry)
    {
        this.valueHandlerRegistry = objectValueRegistry;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public ObjectValueHandlerRegistry getValueHandlerRegistry()
    {
        return this.valueHandlerRegistry;
    }


    public void setGlobalDataLanguageIso(String globalDataLanguageIso)
    {
        if(this.globalDataLanguageIso == null || !this.globalDataLanguageIso.equals(globalDataLanguageIso))
        {
            this.globalDataLanguageIso = globalDataLanguageIso;
            fireDataLanguageChanged();
        }
    }


    public String getGlobalDataLanguageIso()
    {
        String languageCode = (String)Executions.getCurrent().getAttribute("globalDataLanguageIso");
        if(languageCode == null)
        {
            languageCode = (this.globalDataLanguageIso != null) ? this.globalDataLanguageIso : getLanguageIso();
            Set<String> readableLanguages = UISessionUtils.getCurrentSession().getSystemService().getAllReadableLanguageIsos();
            if(readableLanguages != null && !readableLanguages.isEmpty() && !readableLanguages.contains(languageCode))
            {
                languageCode = readableLanguages.iterator().next();
            }
            Executions.getCurrent().setAttribute("globalDataLanguageIso", languageCode);
        }
        return languageCode;
    }


    public SearchService getSearchService()
    {
        return this.searchService;
    }


    @Required
    public void setSearchService(SearchService searchService)
    {
        this.searchService = searchService;
    }


    public SavedQueryService getSavedQueryService()
    {
        return this.savedQueryService;
    }


    @Required
    public void setSavedQueryService(SavedQueryService savedQueryService)
    {
        this.savedQueryService = savedQueryService;
    }


    public UndoManager getUndoManager()
    {
        return this.undoManager;
    }


    @Required
    public void setUndoManager(UndoManager undoManager)
    {
        this.undoManager = undoManager;
    }


    public void sendGlobalEvent(CockpitEvent event, boolean immediate)
    {
        Desktop desktop = Executions.getCurrent().getDesktop();
        Component eventComponent = null;
        if(this.perspectiveContainer != null && this.perspectiveContainer.getDesktop() != null && this.perspectiveContainer
                        .getDesktop().equals(desktop))
        {
            eventComponent = this.perspectiveContainer;
        }
        if(immediate || this.sendEventsImmediately || eventComponent == null)
        {
            for(UICockpitPerspective perspective : getAvailablePerspectives())
            {
                try
                {
                    perspective.onCockpitEvent(event);
                }
                catch(Exception e)
                {
                    LOG.error("Could not dispatch Cockpit event '" + event + "'.", e);
                }
            }
        }
        else
        {
            this.eventCache.add(event);
            eventComponent.addEventListener("onLaterCockpitEvent", this.onLaterListener);
            Events.echoEvent("onLaterCockpitEvent", eventComponent, null);
        }
    }


    public void sendGlobalEvent(CockpitEvent event)
    {
        sendGlobalEvent(event, false);
    }


    public void initSession()
    {
        if(Executions.getCurrent() == null)
        {
            LOG.error("Can not set ID generator. Reason: ZK has not been properly initialized.");
        }
        else
        {
            Desktop desktop = Executions.getCurrent().getDesktop();
            if(desktop != null && isTestIdPropertyTrue())
            {
                ((WebAppCtrl)desktop.getWebApp()).setIdGenerator(createIdGenerator());
            }
        }
    }


    public void registerAdditionalPerspectives()
    {
        Map<String, PerspectivePluginList> beans = this.applicationContext.getBeansOfType(PerspectivePluginList.class);
        for(PerspectivePluginList bean : beans.values())
        {
            this.avPerspUnrestricted.addAll(bean.getAdditionalPerspectives());
        }
    }


    protected IdGenerator createIdGenerator()
    {
        return (IdGenerator)new CockpitTestIDGenerator();
    }


    public boolean isUsingTestIDs()
    {
        if(this.usingTestIDs == null)
        {
            this.usingTestIDs = Boolean.valueOf(isTestIdPropertyTrue());
        }
        return this.usingTestIDs.booleanValue();
    }


    private boolean isTestIdPropertyTrue()
    {
        boolean ret = false;
        try
        {
            ret = BooleanUtils.toBoolean(UITools.getCockpitParameter("default.useTestIds", Executions.getCurrent()));
        }
        catch(Exception e)
        {
            LOG.error("Could not read test id property, reason was ", e);
        }
        return ret;
    }


    @Deprecated
    public void setUsingTestIDs(boolean value)
    {
        try
        {
            if(UITools.getCockpitParameter("default.useTestIds", Executions.getCurrent()) == null)
            {
                this.usingTestIDs = Boolean.valueOf(value);
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("Setting 'usingTestIDs' to '" + value + "' will be ignored since it is overridden in a properties file.");
            }
        }
        catch(Exception e)
        {
            LOG.warn("Property 'usingTestIDs' could not be set.");
        }
    }


    public UICockpitPerspective getPerspective(String uid) throws IllegalArgumentException
    {
        UICockpitPerspective ret = null;
        if(isPerspectiveAvailable(uid))
        {
            for(UICockpitPerspective perspective : getAvailablePerspectives())
            {
                if(perspective.getUid().equals(uid))
                {
                    ret = perspective;
                    break;
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("No perspective available with the specified uid '" + uid + "'.");
        }
        if(ret == null)
        {
            throw new IllegalStateException("Perspective available but can not be retrieved.");
        }
        return ret;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public boolean isPerspectiveAvailable(String uid)
    {
        return this.perspectiveUids.contains(uid);
    }


    public RequestHandler getRequestHandler()
    {
        if(this.reqHandler == null)
        {
            DefaultRequestHandler defaultRequestHandler = new DefaultRequestHandler();
            setRequestHandler((RequestHandler)defaultRequestHandler);
        }
        return this.reqHandler;
    }


    public void setRequestHandler(RequestHandler reqHandler)
    {
        this.reqHandler = reqHandler;
    }


    public List<PushCreationContainer> getPushContainers()
    {
        return Collections.unmodifiableList(this.pushContainers);
    }


    public void setPushContainers(List<PushCreationContainer> pushContainers)
    {
        if(!this.pushContainers.equals(pushContainers))
        {
            this.pushContainers.clear();
            this.pushContainers.addAll(pushContainers);
        }
    }


    public void setDragOverPerspectivesEnabled(boolean dragOverPerspEnabled)
    {
        this.dragOverPerspEnabled = dragOverPerspEnabled;
    }


    public boolean isDragOverPerspectivesEnabled()
    {
        return this.dragOverPerspEnabled;
    }


    public void setCachePerspectivesEnabled(boolean cachePerspectivesEnabled)
    {
        this.cachePerspectivesEnabled = cachePerspectivesEnabled;
    }


    public boolean isCachePerspectivesEnabled()
    {
        return this.cachePerspectivesEnabled;
    }


    @Required
    public void setUiConfigurationService(UIConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public UIConfigurationService getUiConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setUiAccessRightService(UIAccessRightService accessRightService)
    {
        this.accessRightService = accessRightService;
    }


    public UIAccessRightService getUiAccessRightService()
    {
        return this.accessRightService;
    }


    public boolean setSessionAttribute(String key, Object value)
    {
        if(JaloSession.hasCurrentSession())
        {
            this.sessionService.getCurrentSession().setAttribute(key, value);
            return true;
        }
        return false;
    }


    public Locale getGlobalDataLocale()
    {
        Locale locale = (Locale)Executions.getCurrent().getAttribute("globalDataLocale");
        if(locale == null)
        {
            String lang = (this.globalDataLanguageIso != null) ? this.globalDataLanguageIso : getLanguageIso();
            Set<String> readableLanguages = UISessionUtils.getCurrentSession().getSystemService().getAllReadableLanguageIsos();
            if(readableLanguages != null && !readableLanguages.isEmpty() && !readableLanguages.contains(lang))
            {
                lang = readableLanguages.iterator().next();
            }
            locale = getLocalizationService().getLocaleByString(lang);
            Executions.getCurrent().setAttribute("globalDataLocale", locale);
        }
        return locale;
    }


    public Locale getLocale()
    {
        LanguageModel lang = JaloSession.hasCurrentSession() ? this.commonI18NService.getCurrentLanguage() : null;
        return (lang != null) ? this.commonI18NService.getLocaleForLanguage(lang) : Locales.getCurrent();
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setLocalizationService(LocalizationService localizationService)
    {
        this.localizationService = localizationService;
    }


    public LocalizationService getLocalizationService()
    {
        return this.localizationService;
    }


    @Required
    public void setJasperReportsRefresh(JasperReportsRefresh jasperReportsRefresh)
    {
        this.jasperReportsRefresh = jasperReportsRefresh;
    }


    public JasperReportsRefresh getJasperReportsRefresh()
    {
        return this.jasperReportsRefresh;
    }


    public TypeService getTypeService()
    {
        return this.cockpitTypeService;
    }


    public void setApplicationContext(ApplicationContext appCtx) throws BeansException
    {
        this.applicationContext = appCtx;
    }
}
