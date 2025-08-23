package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.ListViewHelper;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDragAndDropContext;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.CockpitListComponent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class MainAreaListviewBrowserComponent extends AbstractMainAreaBrowserComponent
{
    private static final Logger log = LoggerFactory.getLogger(MainAreaListviewBrowserComponent.class);
    protected transient UIListView listView = null;
    protected ComponentController tableController = null;
    private TableModelListener tableModelListener = null;
    protected ColumnModelListener columnModelListener;
    protected static final String MULTITYPE_LIST_VIEW_CONFIG_CODE = "multiTypeListView";
    private static final String CONTENT_BROWSER_LIST_VIEW_CONFIG_CODE = "listViewContentBrowser";


    public MainAreaListviewBrowserComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    protected UIItemView getCurrentItemView()
    {
        return (UIItemView)this.listView;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            loadListModel();
            resetSelection();
            this.listComponentModel.setListModel((ListModel)getResultModel());
            this.listView = loadListView();
            if(this.listView == null)
            {
                this.mainArea.getChildren().clear();
                this.mainArea.appendChild((Component)new Label(Labels.getLabel("browser.emptymessage")));
            }
            else
            {
                if(this.mainArea.getChildren().isEmpty() || !this.mainArea.getFirstChild().equals(this.listView))
                {
                    this.mainArea.getChildren().clear();
                    this.mainArea.appendChild((Component)this.listView);
                }
                if(this.listComponentModel != null)
                {
                    this.listComponentModel.setSelectedIndexesDirectly(getModel().getSelectedIndexes());
                    this.listComponentModel.setActiveItem(getModel().getActiveItem());
                }
                success = true;
            }
            getContentBrowser().updateStatusBar();
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    protected Div createMainArea()
    {
        Div marginHelper = new Div();
        marginHelper.setStyle("margin: 0px; background: white;");
        UITools.maximize((HtmlBasedComponent)marginHelper);
        Div mainContainer = new Div();
        mainContainer.setWidth("99%");
        mainContainer.setHeight("99%");
        loadListModel();
        this.listView = loadListView();
        if(getResultModel() == null || getResultModel().isEmpty())
        {
            mainContainer.appendChild((Component)new Label(Labels.getLabel("browser.emptymessage")));
        }
        else
        {
            mainContainer.appendChild((Component)this.listView);
        }
        this.mainArea = mainContainer;
        marginHelper.appendChild((Component)mainContainer);
        return marginHelper;
    }


    protected UIListView loadListView()
    {
        UIListView listView = null;
        if(getModel() == null)
        {
            this.lastResultType = null;
        }
        else
        {
            if(getTableModel() == null)
            {
                setTableModel((MutableTableModel)new DefaultTableModel(this.listComponentModel, null));
            }
            else
            {
                getTableModel().setListComponentModel(this.listComponentModel);
            }
            ListViewHelper.ListViewInfo lvInfo = ListViewHelper.loadListView(this.listView, getListViewConfigurationCode(), this.lastResultType,
                            getRootType(), getTableModel(), getResultModel(), (CockpitListComponent)getModel(), (ListViewHelper.ListenerHandler)new MyListenerHandler(this), (DragAndDropContext)new DefaultDragAndDropContext((BrowserModel)
                                            getModel()));
            if(lvInfo != null)
            {
                listView = lvInfo.getListView();
                this.lastResultType = lvInfo.getRootType();
            }
            getTableModel().getListComponentModel().setSelectedIndexes(getModel().getSelectedIndexes());
            getTableModel().getListComponentModel().setActiveItem(getModel().getActiveItem());
        }
        return listView;
    }


    protected ColumnModelListener getColumnModelListener()
    {
        if(this.columnModelListener == null)
        {
            this.columnModelListener = (ColumnModelListener)new Object(this);
        }
        return this.columnModelListener;
    }


    protected void storeListViewConfiguration()
    {
        if(getTableModel() != null && getTableModel().getColumnComponentModel() instanceof DefaultColumnModel)
        {
            DefaultColumnModel columnModel = (DefaultColumnModel)getTableModel().getColumnComponentModel();
            ListViewConfiguration configuration = columnModel.getConfiguration();
            if(getModel() instanceof AbstractBrowserModel &&
                            getListViewConfigurationCode().equals("listViewContentBrowser"))
            {
                getUIConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)configuration,
                                UISessionUtils.getCurrentSession().getUser(), getRootType(), getListViewConfigurationCode(), ListViewConfiguration.class);
                ((AbstractBrowserModel)getModel()).setCacheView((MutableColumnModel)columnModel);
            }
        }
    }


    protected ListViewConfiguration getListViewConfiguration(ObjectTemplate objectTemplate, String code)
    {
        UIConfigurationService uiConfigService = getUIConfigurationService();
        if(code != null)
        {
            return (ListViewConfiguration)uiConfigService.getComponentConfiguration(objectTemplate, code, ListViewConfiguration.class);
        }
        return (ListViewConfiguration)uiConfigService.getComponentConfiguration(objectTemplate, "multiTypeListView", ListViewConfiguration.class);
    }


    protected void cleanup()
    {
        if(this.tableController != null)
        {
            this.tableController.unregisterListeners();
        }
        if(this.tableModelListener != null && getTableModel() != null)
        {
            getTableModel().removeTableModelListener(this.tableModelListener);
        }
        if(getTableModel() != null)
        {
            getTableModel().getColumnComponentModel().removeColumnModelListener(getColumnModelListener());
        }
    }


    protected CockpitSavedQueryModel getCurrentSavedQuery()
    {
        CockpitSavedQueryModel ret = null;
        UICockpitPerspective perspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
        UISavedQuery query = perspective.getNavigationArea().getSelectedSavedQuery();
        if(query != null && query.getSavedQuery() != null)
        {
            ret = query.getSavedQuery();
        }
        return ret;
    }


    protected String getListViewConfigurationCode()
    {
        CockpitSavedQueryModel ret = getCurrentSavedQuery();
        if(ret != null)
        {
            String internalCode = StringUtils.join((Object[])new String[] {"listViewContentBrowser", "_", ret
                            .getCode()});
            return internalCode;
        }
        return "listViewContentBrowser";
    }
}
