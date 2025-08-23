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

public class DummyUISession implements UISession
{
    public void addSessionListener(UISessionListener listener)
    {
    }


    public void removeSessionListener(UISessionListener listener)
    {
    }


    public String getLanguageIso()
    {
        return null;
    }


    public String getGlobalDataLanguageIso()
    {
        return null;
    }


    public Locale getLocale()
    {
        return null;
    }


    public Locale getGlobalDataLocale()
    {
        return null;
    }


    public void setGlobalDataLanguageIso(String iso)
    {
    }


    public UserModel getUser()
    {
        return null;
    }


    public void login(UserModel user)
    {
    }


    public void logout()
    {
    }


    public void setUser(UserModel currentUser)
    {
    }


    public void setUserByUID(String uid)
    {
    }


    public UICockpitPerspective getCurrentPerspective()
    {
        return null;
    }


    public void setCurrentPerspective(UICockpitPerspective perspective)
    {
    }


    public void setCurrentPerspective(UICockpitPerspective perspective, Map<String, ? extends Object> params)
    {
    }


    public List<UICockpitPerspective> getAvailablePerspectives()
    {
        return null;
    }


    public boolean isPerspectiveAvailable(String uid)
    {
        return false;
    }


    public UICockpitPerspective getPerspective(String uid) throws IllegalArgumentException
    {
        return null;
    }


    public void setSelectedCatalogVersions(List<CatalogVersionModel> catalogVersions)
    {
    }


    public List<CatalogVersionModel> getSelectedCatalogVersions()
    {
        return null;
    }


    public SystemService getSystemService()
    {
        return null;
    }


    public TypeService getTypeService()
    {
        return null;
    }


    public NewItemService getNewItemService()
    {
        return null;
    }


    public ObjectValueHandlerRegistry getValueHandlerRegistry()
    {
        return null;
    }


    public LabelService getLabelService()
    {
        return null;
    }


    public SearchService getSearchService()
    {
        return null;
    }


    public SavedQueryService getSavedQueryService()
    {
        return null;
    }


    public UIConfigurationService getUiConfigurationService()
    {
        return null;
    }


    public ModelService getModelService()
    {
        return null;
    }


    public UndoManager getUndoManager()
    {
        return null;
    }


    public void sendGlobalEvent(CockpitEvent event)
    {
    }


    public void sendGlobalEvent(CockpitEvent event, boolean immediate)
    {
    }


    public boolean isUsingTestIDs()
    {
        return false;
    }


    public boolean isDragOverPerspectivesEnabled()
    {
        return false;
    }


    public boolean isCachePerspectivesEnabled()
    {
        return false;
    }


    public void setRequestHandler(RequestHandler reqHandler)
    {
    }


    public RequestHandler getRequestHandler()
    {
        return null;
    }


    public void setPushContainers(List<PushCreationContainer> pushContainers)
    {
    }


    public List<PushCreationContainer> getPushContainers()
    {
        return null;
    }


    public boolean setSessionAttribute(String key, Object value)
    {
        return false;
    }


    public UIAccessRightService getUiAccessRightService()
    {
        return null;
    }


    public JasperReportsRefresh getJasperReportsRefresh()
    {
        return null;
    }
}
