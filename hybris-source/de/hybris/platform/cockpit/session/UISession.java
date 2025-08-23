package de.hybris.platform.cockpit.session;

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
import de.hybris.platform.cockpit.session.impl.PushCreationContainer;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface UISession
{
    public static final String PERSPECTIVE_ATTRIBUTE = "perspective";


    void addSessionListener(UISessionListener paramUISessionListener);


    void removeSessionListener(UISessionListener paramUISessionListener);


    String getLanguageIso();


    String getGlobalDataLanguageIso();


    Locale getLocale();


    Locale getGlobalDataLocale();


    void setGlobalDataLanguageIso(String paramString);


    UserModel getUser();


    void login(UserModel paramUserModel);


    void logout();


    void setUser(UserModel paramUserModel);


    void setUserByUID(String paramString);


    UICockpitPerspective getCurrentPerspective();


    void setCurrentPerspective(UICockpitPerspective paramUICockpitPerspective);


    void setCurrentPerspective(UICockpitPerspective paramUICockpitPerspective, Map<String, ? extends Object> paramMap);


    List<UICockpitPerspective> getAvailablePerspectives();


    boolean isPerspectiveAvailable(String paramString);


    UICockpitPerspective getPerspective(String paramString) throws IllegalArgumentException;


    void setSelectedCatalogVersions(List<CatalogVersionModel> paramList);


    List<CatalogVersionModel> getSelectedCatalogVersions();


    SystemService getSystemService();


    TypeService getTypeService();


    NewItemService getNewItemService();


    ObjectValueHandlerRegistry getValueHandlerRegistry();


    LabelService getLabelService();


    SearchService getSearchService();


    SavedQueryService getSavedQueryService();


    UIConfigurationService getUiConfigurationService();


    ModelService getModelService();


    UndoManager getUndoManager();


    void sendGlobalEvent(CockpitEvent paramCockpitEvent);


    void sendGlobalEvent(CockpitEvent paramCockpitEvent, boolean paramBoolean);


    boolean isUsingTestIDs();


    boolean isDragOverPerspectivesEnabled();


    boolean isCachePerspectivesEnabled();


    void setRequestHandler(RequestHandler paramRequestHandler);


    RequestHandler getRequestHandler();


    void setPushContainers(List<PushCreationContainer> paramList);


    List<PushCreationContainer> getPushContainers();


    boolean setSessionAttribute(String paramString, Object paramObject);


    UIAccessRightService getUiAccessRightService();


    JasperReportsRefresh getJasperReportsRefresh();
}
