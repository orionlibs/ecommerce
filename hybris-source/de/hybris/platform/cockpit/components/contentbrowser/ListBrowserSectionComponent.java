package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.AbstractTableController;
import de.hybris.platform.cockpit.model.listview.impl.DefaultBrowserSectionTableController;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableModel;
import de.hybris.platform.cockpit.model.listview.impl.ListView;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropContext;
import de.hybris.platform.cockpit.services.dragdrop.impl.DefaultDragAndDropContext;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.ListSectionModel;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public class ListBrowserSectionComponent extends ListSectionComponent implements BrowserSectionComponent
{
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewBrowserSection";
    private static final Logger LOG = LoggerFactory.getLogger(ListBrowserSectionComponent.class);
    private boolean initialized = false;
    private Component additionalCaptionComponent;
    private boolean listHeaderVisible = true;


    public boolean isListHeaderVisible()
    {
        return this.listHeaderVisible;
    }


    public void setListHeaderVisible(boolean listHeaderVisible)
    {
        this.listHeaderVisible = listHeaderVisible;
    }


    public ListBrowserSectionComponent(ListBrowserSectionModel sectionModel)
    {
        super((ListSectionModel)sectionModel);
        if(sectionModel == null)
        {
            throw new IllegalArgumentException("Section model can not be null.");
        }
        setDroppable("PerspectiveDND");
    }


    public ListBrowserSectionModel getSectionModel()
    {
        return (ListBrowserSectionModel)super.getSectionModel();
    }


    public SectionBrowserModel getModel()
    {
        return getSectionModel().getSectionBrowserModel();
    }


    public boolean initialize()
    {
        this.initialized = super.initialize();
        setAdditionalCaptionComponent(this.additionalCaptionComponent);
        return this.initialized;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            setVisible(getSectionModel().isVisible());
            this.groupBoxContent.getChildren().clear();
            this.listView = loadListView();
            if(this.listView == null)
            {
                this.groupBoxContent.appendChild((Component)new Label("Nothing to display"));
            }
            else
            {
                this.groupBoxContent.appendChild((Component)this.listView);
                if(getTableModel() != null)
                {
                    getTableModel().getListComponentModel().setSelectedIndexesDirectly(
                                    getSectionModel().getSelectedIndexes());
                    getTableModel().getListComponentModel().setActiveItem(getModel().getActiveItem());
                }
                this.listView.update();
                success = true;
            }
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    protected UIListView loadListView()
    {
        UIListView listView = null;
        boolean changed = false;
        if(getModel() == null || getSectionModel() == null || getSectionModel().getItems().isEmpty() ||
                        getRootType() == null)
        {
            this.lastResultType = null;
        }
        else
        {
            if(this.listView == null)
            {
                ListView newListView = new ListView();
                ListView listView1 = newListView;
                newListView.setShowColumnHeaders(this.listHeaderVisible);
                newListView.setupLazyLoading(0, 0);
                listView1.setHeight("100%");
                listView1.setDDContext((DragAndDropContext)new DefaultDragAndDropContext((BrowserModel)getModel()));
                changed = true;
            }
            else
            {
                listView = this.listView;
            }
            if(getTableModel() == null || (listView
                            .getModel() != null && listView.getModel().getListComponentModel() != null && listView.getModel()
                            .getListComponentModel().getListModel() == null) || this.lastResultType == null ||
                            !this.lastResultType.getBaseType().isAssignableFrom((ObjectType)getRootType()) || (
                            !this.lastResultType.equals(getRootType()) && getListViewConfiguration(getRootType(),
                                            getListViewConfigurationCode()) != null))
            {
                this.lastResultType = getRootType();
                ListViewConfiguration listViewConfig = null;
                try
                {
                    listViewConfig = getListViewConfiguration(getRootType(), getListViewConfigurationCode());
                }
                catch(Exception e)
                {
                    LOG.warn("Could not load list view configuration (Reason: '" + e.getMessage() + "').", e);
                    return null;
                }
                if(listViewConfig == null)
                {
                    LOG.warn("Can not display list view (Reason: No matching list view configuration found).");
                    return null;
                }
                if(getTableModel() != null)
                {
                    getTableModel().getListComponentModel().setListModel(null);
                }
                DefaultListComponentModel defaultListComponentModel = new DefaultListComponentModel();
                defaultListComponentModel.setEditable(true);
                defaultListComponentModel.setSelectable(true);
                defaultListComponentModel.setActivatable(true);
                defaultListComponentModel.setMultiple(false);
                DefaultColumnModel defaultColumnModel = new DefaultColumnModel(listViewConfig);
                setTableModel((MutableTableModel)new DefaultTableModel((MutableListModel)defaultListComponentModel, (MutableColumnModel)defaultColumnModel));
                listView.setModel(null);
                getTableModel().getListComponentModel().setListModel(null);
                updateResult();
                getTableModel().getListComponentModel().setListModel((ListModel)getResultModel());
                listView.setModel((TableModel)getTableModel());
                changed = true;
            }
            else if(changed)
            {
                listView.setModel((TableModel)getTableModel());
            }
            if(changed)
            {
                if(this.tableController != null)
                {
                    try
                    {
                        this.tableController.unregisterListeners();
                    }
                    catch(Exception e)
                    {
                        LOG.warn("Could not unregister listeners (Reason: '" + e.getMessage() + "').", e);
                    }
                    if(getTableModel() != null)
                    {
                        getTableModel().getColumnComponentModel().removeColumnModelListener(getColumnModelListener());
                    }
                }
                this.tableController = (ComponentController)createTableController((ListSectionModel)getSectionModel(), getTableModel(), listView);
                this.tableController.initialize();
                if(getTableModel() != null)
                {
                    getTableModel().getColumnComponentModel().addColumnModelListener(getColumnModelListener());
                }
            }
            else
            {
                updateResult();
            }
            getTableModel().getListComponentModel().setSelectedIndexes(getSectionModel().getSelectedIndexes());
            getTableModel().getListComponentModel().setActiveItem(
                            getSectionModel().getSectionBrowserModel().getActiveItem());
        }
        return listView;
    }


    public void setAdditionalCaptionComponent(Component component)
    {
        this.additionalCaptionComponent = component;
        if(this.captionContainer != null && component != null)
        {
            this.captionContainer.appendChild(component);
        }
    }


    protected AbstractTableController createTableController(ListSectionModel listSectionModel, MutableTableModel mutableTableModel, UIListView listView)
    {
        if(listSectionModel instanceof ListBrowserSectionModel)
        {
            return (AbstractTableController)new DefaultBrowserSectionTableController((ListBrowserSectionModel)listSectionModel, mutableTableModel, listView);
        }
        throw new IllegalArgumentException("List section model not a list browser section model.");
    }


    public void setModel(BrowserModel model)
    {
        LOG.error("Can not change the browser model of a section browser component");
    }


    protected String getListViewConfigurationCode()
    {
        String configCode = null;
        if(getSectionModel().getListViewConfigurationCode() == null)
        {
            configCode = "listViewBrowserSection";
        }
        else
        {
            configCode = getSectionModel().getListViewConfigurationCode();
        }
        return configCode;
    }
}
