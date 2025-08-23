package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

@Deprecated(since = "1905", forRemoval = true)
public class CatalogVersionFacetChartFiltersRenderer implements FacetChartFiltersRenderer
{
    private static final String CATALOG_VERSION_SELECTED_VALUE = "catalogVersionSelectedValue";
    private static final String LABEL_CATALOG_VERSION = "solrchart.catalogversionfilterrenderer.catalog_version";
    private static final String FACET_NAME = "catalogVersion";
    private static final String SCLASS_BUTTON_REMOVE_FILTER = "ye-text-button ye-delete-btn";
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private LabelService labelService;
    private PermissionFacade permissionFacade;
    private SessionService sessionService;
    private WidgetInstanceManager widgetInstanceManager;
    private BiConsumer<String, Set<String>> facetSelectionChange;
    private int order = 100;


    public void renderFilters(WidgetInstanceManager widgetInstanceManager, Div filterContainer, BiConsumer<String, Set<String>> facetSelectionChange)
    {
        this.widgetInstanceManager = widgetInstanceManager;
        this.facetSelectionChange = facetSelectionChange;
        appendCatalogVersionLabel(filterContainer);
        appendRemoveFilterButton(filterContainer);
        appendChosenBox(filterContainer);
    }


    protected void appendRemoveFilterButton(Div filterContainer)
    {
        Button removeFilterButton = new Button();
        removeFilterButton.setClass("ye-text-button ye-delete-btn");
        removeFilterButton.addEventListener("onClick", event -> removeFilters());
        removeFilterButton.setParent((Component)filterContainer);
    }


    protected void appendCatalogVersionLabel(Div filterContainer)
    {
        Label catalogVersionLabel = new Label(Labels.getLabel("solrchart.catalogversionfilterrenderer.catalog_version"));
        catalogVersionLabel.setParent((Component)filterContainer);
    }


    protected void appendChosenBox(Div filterContainer)
    {
        Chosenbox chosenbox = new Chosenbox();
        chosenbox.setModel((ListModel)getCatalogVersions());
        chosenbox.addEventListener("onSelect", event -> onSelectCatalogVersion(((SelectEvent)event).getSelectedObjects()));
        Collection<String> selectedValues = readSelectedValues();
        Set<String> selectedCatalogVersions = getSelectedCatalogVersions((ListModelList<String>)chosenbox.getModel(), selectedValues);
        if(selectedValues.size() != selectedCatalogVersions.size())
        {
            storeSelectedValues(selectedCatalogVersions);
            if(selectedCatalogVersions.isEmpty())
            {
                this.facetSelectionChange.accept("catalogVersion", selectedCatalogVersions);
                return;
            }
        }
        chosenbox.setSelectedObjects(selectedCatalogVersions);
        chosenbox.setParent((Component)filterContainer);
    }


    protected ListModelList<String> getCatalogVersions()
    {
        ListModelList<String> listModel = new ListModelList();
        listModel.setMultiple(true);
        Objects.requireNonNull(this.labelService);
        listModel.addAll((Collection)getAllReadableCatalogVersionsForCurrentUser().stream().map(this.labelService::getObjectLabel).collect(Collectors.toSet()));
        return listModel;
    }


    private Collection<CatalogVersionModel> getAllReadableCatalogVersionsForCurrentUser()
    {
        UserModel currentUser = getUserService().getCurrentUser();
        if(getUserService().isAdmin(currentUser))
        {
            return (Collection<CatalogVersionModel>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this), (UserModel)
                            getUserService().getAdminUser());
        }
        return getCatalogVersionService().getAllReadableCatalogVersions((PrincipalModel)currentUser);
    }


    protected Set<String> getSelectedCatalogVersions(ListModelList<String> listModel, Collection<String> selectedValues)
    {
        Objects.requireNonNull(listModel);
        return (Set<String>)selectedValues.stream().filter(listModel::contains)
                        .collect(Collectors.toSet());
    }


    protected void onSelectCatalogVersion(Set<String> selectedCatalogVersions)
    {
        storeSelectedValues(selectedCatalogVersions);
        this.facetSelectionChange.accept("catalogVersion", selectedCatalogVersions);
    }


    protected Collection<String> readSelectedValues()
    {
        Collection<String> selectedValues = (Collection<String>)this.widgetInstanceManager.getModel().getValue("catalogVersionSelectedValue", Collection.class);
        return (selectedValues == null) ? Collections.<String>emptySet() : selectedValues;
    }


    protected void removeFilters()
    {
        storeSelectedValues(Collections.emptySet());
        this.facetSelectionChange.accept("catalogVersion", Collections.emptySet());
    }


    protected void storeSelectedValues(Collection<String> selectedItems)
    {
        this.widgetInstanceManager.getModel().setValue("catalogVersionSelectedValue", selectedItems);
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return this.permissionFacade;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
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


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }
}
