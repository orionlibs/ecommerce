package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.advancedsearch.impl.DefaultAdvancedSearchModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.session.BrowserFilter;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Separator;

public class SearchToolbarBrowserComponent extends PagerToolbarBrowserComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(SearchToolbarBrowserComponent.class);
    protected transient Combobox sortCombobox = null;
    protected transient Combobox browserFilters = null;
    protected transient Checkbox sortDirectionCheckbox = null;
    protected EventListener sortController = null;


    public SearchToolbarBrowserComponent(SearchBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((PageableBrowserModel)model, contentBrowser);
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            this.sortController = (EventListener)new MySortContoller(this);
            if(super.initialize())
            {
                this.initialized = true;
            }
        }
        return this.initialized;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized && super.update())
        {
            updateBrowserFilters();
            updateSortFields();
            success = true;
        }
        else if(!this.initialized)
        {
            success = initialize();
        }
        return success;
    }


    public void setModel(BrowserModel model)
    {
        if(getModel() == null || getModel() != model)
        {
            super.setModel(model);
            if(model instanceof SearchBrowserModel)
            {
                if(getModel() != null && getModel().getAdvancedSearchModel().getSortedByProperty() == null &&
                                !getModel().getAdvancedSearchModel().getSortableProperties().isEmpty())
                {
                    if(getModel().getAdvancedSearchModel() instanceof DefaultAdvancedSearchModel)
                    {
                        ((DefaultAdvancedSearchModel)getModel().getAdvancedSearchModel()).setSortedByProperty(getModel()
                                        .getAdvancedSearchModel().getSortableProperties().iterator().next());
                    }
                }
                initialize();
            }
        }
    }


    protected void updateSortFields()
    {
        if(this.sortCombobox != null)
        {
            this.sortCombobox.getItems().clear();
            if(getModel() != null)
            {
                if(getModel().getAdvancedSearchModel() != null &&
                                getModel().getAdvancedSearchModel().getSortableProperties() != null)
                {
                    for(PropertyDescriptor sortProp : getModel().getAdvancedSearchModel().getSortableProperties())
                    {
                        Comboitem item = new Comboitem(sortProp.getName());
                        item.setValue(sortProp);
                        this.sortCombobox.appendChild((Component)item);
                    }
                    try
                    {
                        int index = getModel().getAdvancedSearchModel().getSortableProperties().indexOf(getModel().getAdvancedSearchModel().getSortedByProperty());
                        this.sortCombobox.setSelectedIndex((index >= 0) ? index : 0);
                    }
                    catch(Exception e)
                    {
                        if(!this.sortCombobox.getChildren().isEmpty())
                        {
                            this.sortCombobox.setSelectedIndex(0);
                        }
                    }
                }
            }
        }
        if(this.sortDirectionCheckbox != null && getModel() != null && getModel().getAdvancedSearchModel() != null)
        {
            this.sortDirectionCheckbox.setChecked(getModel().getAdvancedSearchModel().isSortAscending());
        }
        this.sortCombobox.setVisible((!currentViewHasOwnModel() &&
                        !getModel().getAdvancedSearchModel().getSortableProperties().isEmpty()));
    }


    protected void updateBrowserFilters()
    {
        if(this.browserFilters != null)
        {
            this.browserFilters.getItems().clear();
            if(getModel() != null && CollectionUtils.isNotEmpty(getModel().getAvailableBrowserFilters()))
            {
                for(BrowserFilter filter : getModel().getAvailableBrowserFilters())
                {
                    String label = " ";
                    if(filter != null)
                    {
                        label = filter.getLabel();
                    }
                    Comboitem item = new Comboitem(label);
                    if(StringUtils.isBlank(label))
                    {
                        item.setSclass("browser-filters-empty");
                    }
                    item.setValue(filter);
                    this.browserFilters.appendChild((Component)item);
                }
                try
                {
                    List<BrowserFilter> internal = new ArrayList<>(getModel().getAvailableBrowserFilters());
                    int index = internal.indexOf(getModel().getBrowserFilter());
                    this.browserFilters.setSelectedIndex((index >= 0) ? index : 0);
                }
                catch(Exception e)
                {
                    if(!this.browserFilters.getChildren().isEmpty())
                    {
                        this.browserFilters.setSelectedIndex(0);
                    }
                }
            }
            boolean visible = (!currentViewHasOwnModel() && !isAvailableBrowserFilterEmpty(getModel().getAvailableBrowserFilters()) && !isBrowserFilterDisabled());
            this.browserFilters.setVisible(visible);
        }
    }


    private boolean isBrowserFilterDisabled()
    {
        String perpsectiveId = getModel().getArea().getPerspective().getUid();
        String browserFilterDisabledParam = "browser.filters.disabled.for." + perpsectiveId;
        try
        {
            return Boolean.parseBoolean(UITools.getCockpitParameter(browserFilterDisabledParam, Executions.getCurrent()));
        }
        catch(Exception e)
        {
            LOG.warn("Could not retrieve property ' " + browserFilterDisabledParam + " ', returning true.");
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error was", e);
            }
            return true;
        }
    }


    private boolean isAvailableBrowserFilterEmpty(Set<BrowserFilter> browserFilters)
    {
        boolean ret = false;
        if(CollectionUtils.isEmpty(browserFilters) || (browserFilters.size() == 1 && browserFilters.iterator().next() == null))
        {
            ret = true;
        }
        return ret;
    }


    public SearchBrowserModel getModel()
    {
        return (SearchBrowserModel)super.getModel();
    }


    protected HtmlBasedComponent createLeftToolbarContent()
    {
        HtmlBasedComponent hbox = super.createLeftToolbarContent();
        if(getContentBrowser() instanceof DefaultSearchContentBrowser)
        {
            DefaultSearchContentBrowser contentBrowser = (DefaultSearchContentBrowser)getContentBrowser();
            HtmlBasedComponent createActionComponent = contentBrowser.createCreateNewComponent();
            if(createActionComponent != null)
            {
                getCreateNewToolbarSlot().appendChild((Component)createActionComponent);
            }
        }
        return hbox;
    }


    protected Hbox createRightToolbarHbox()
    {
        Hbox hbox = super.createRightToolbarHbox();
        Separator separator = new Separator("horizontal");
        hbox.insertBefore((Component)separator, hbox.getFirstChild());
        this.sortDirectionCheckbox = new Checkbox(Labels.getLabel("general.direction.asc"));
        hbox.insertBefore((Component)this.sortDirectionCheckbox, (Component)separator);
        this.sortDirectionCheckbox.addEventListener("onCheck", this.sortController);
        this.sortDirectionCheckbox.addEventListener("onLater", this.sortController);
        this.sortCombobox = new Combobox();
        this.sortCombobox.setTooltiptext(Labels.getLabel("browserarea.sort"));
        this.sortCombobox.setCols(10);
        hbox.insertBefore((Component)this.sortCombobox, (Component)this.sortDirectionCheckbox);
        this.sortCombobox.addEventListener("onChange", this.sortController);
        this.sortCombobox.addEventListener("onLater", this.sortController);
        this.sortCombobox.setReadonly(true);
        updateSortFields();
        this.browserFilters = new Combobox();
        this.browserFilters.setSclass("browser-filter-combo");
        this.browserFilters.setTooltiptext(Labels.getLabel("browserarea.browserFilters"));
        this.browserFilters.setCols(10);
        hbox.insertBefore((Component)this.browserFilters, (Component)this.sortCombobox);
        UITools.addBusyListener((Component)this.browserFilters, "onChange", (EventListener)new Object(this), null, "general.updating.busy");
        this.browserFilters.setVisible(false);
        return hbox;
    }
}
