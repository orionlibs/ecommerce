package de.hybris.platform.cockpit.components.contentbrowser.browsercomponents;

import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.MainAreaListviewBrowserComponent;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultBrowserTableController;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.TaskTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskMainAreaBrowserComponent extends MainAreaListviewBrowserComponent
{
    private static final Logger log = LoggerFactory.getLogger(TaskMainAreaBrowserComponent.class);
    private List<String> attachmentTypes = null;
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewTaskContentBrowser";


    public TaskMainAreaBrowserComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    public TaskMainAreaBrowserComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser, String attachmentType)
    {
        this(model, contentBrowser, Collections.singletonList(attachmentType));
    }


    public TaskMainAreaBrowserComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser, List<String> attachmentTypes)
    {
        super(model, contentBrowser);
        setAttachmentTypes(attachmentTypes);
    }


    protected void resetSelection()
    {
        getModel().setAllMarked(false);
    }


    protected UIListView loadListView()
    {
        UIListView listView = null;
        boolean changed = false;
        if(getModel() != null && !getModel().getItems().isEmpty())
        {
            if(this.listView == null)
            {
                TaskListView taskListView = new TaskListView(this);
                taskListView.setHeight("100%");
                taskListView.setWidth("100%");
                taskListView.setShowColumnHeaders(false);
                taskListView.setSclass("taskListView");
                changed = true;
            }
            else
            {
                listView = this.listView;
            }
            ObjectTemplate rootType = getRootType();
            if(getTableModel() == null || this.lastResultType == null ||
                            !this.lastResultType.getBaseType().isAssignableFrom((ObjectType)getRootType()) || (
                            !this.lastResultType.equals(rootType) && getListViewConfiguration(rootType, "listViewTaskContentBrowser") != null))
            {
                this.lastResultType = rootType;
                ListViewConfiguration listViewConfig = null;
                try
                {
                    listViewConfig = getListViewConfiguration(rootType, "listViewTaskContentBrowser");
                }
                catch(Exception e)
                {
                    log.warn("Could not load list view configuration (Reason: '" + e.getMessage() + "').", e);
                    return null;
                }
                if(listViewConfig == null)
                {
                    log.warn("Can not display list view (Reason: No matching list view configuration found).");
                    return null;
                }
                if(getTableModel() != null)
                {
                    getTableModel().getListComponentModel().setListModel(null);
                }
                DefaultColumnModel defaultColumnModel = new DefaultColumnModel(listViewConfig);
                setTableModel((MutableTableModel)new TaskTableModel(this.listComponentModel, (MutableColumnModel)defaultColumnModel, getAttachmentTypes()));
                listView.setModel(null);
                updateResult();
                getTableModel().getListComponentModel().setListModel((ListModel)getResultModel());
                getTableModel().getListComponentModel().setSelectedIndexes(getModel().getSelectedIndexes());
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
                        log.warn("Could not unregister listeners (Reason: '" + e.getMessage() + "').", e);
                    }
                }
                this.tableController = (ComponentController)createTableController(getModel(), getTableModel(), listView);
                this.tableController.initialize();
            }
            else
            {
                updateResult();
            }
            getTableModel().getListComponentModel().setSelectedIndexes(getModel().getSelectedIndexes());
            getTableModel().getListComponentModel().setActiveItem(getModel().getActiveItem());
        }
        else
        {
            this.lastResultType = null;
        }
        return listView;
    }


    protected void loadListModel()
    {
        this.listComponentModel = (MutableListModel)new DefaultListComponentModel();
        this.listComponentModel.setEditable(false);
        this.listComponentModel.setSelectable(true);
        this.listComponentModel.setActivatable(false);
        this.listComponentModel.setMultiple(false);
        this.listComponentModel.setListModel((ListModel)getResultModel());
    }


    protected DefaultBrowserTableController createTableController(AdvancedBrowserModel advancedBrowserModel, MutableTableModel mutableTableModel, UIListView listView)
    {
        return (DefaultBrowserTableController)new TaskTableController(this, (BrowserModel)advancedBrowserModel, mutableTableModel, listView);
    }


    public String getAttachmentType()
    {
        if(!getAttachmentTypes().isEmpty())
        {
            return getAttachmentTypes().get(0);
        }
        return null;
    }


    public void setAttachmentType(String attachmentType)
    {
        setAttachmentTypes(Collections.singletonList(attachmentType));
    }


    public void setAttachmentTypes(List<String> attachmentTypes)
    {
        this.attachmentTypes = attachmentTypes;
    }


    public List<String> getAttachmentTypes()
    {
        return this.attachmentTypes;
    }
}
