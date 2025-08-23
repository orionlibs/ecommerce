package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.ColumnModelListener;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.AbstractTableController;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableController;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTableModel;
import de.hybris.platform.cockpit.model.listview.impl.ListView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.session.ListSectionModel;
import de.hybris.platform.cockpit.session.SectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import java.util.Collection;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

public class ListSectionComponent extends AbstractSectionComponent implements DesktopRemovalAwareComponent
{
    protected static final String LIST_VIEW_CONFIG_CODE = "listViewSection";
    private static final Logger LOG = LoggerFactory.getLogger(ListSectionComponent.class);
    private boolean initialized = false;
    protected ObjectTemplate lastResultType;
    protected final DefaultListModel<TypedObject> resultModel = new DefaultListModel();
    private UIConfigurationService uiConfigurationService = null;
    protected transient UIListView listView = null;
    private transient AdvancedGroupbox sectionGroupBox = null;
    protected transient Div groupBoxContent = null;
    protected ComponentController tableController = null;
    protected ColumnModelListener columnModelListener;
    protected Div captionContainer;


    public ListSectionComponent(ListSectionModel sectionModel)
    {
        super((SectionModel)sectionModel);
    }


    protected UIListView loadListView()
    {
        UIListView listView = null;
        boolean changed = false;
        if(getSectionModel() == null || getSectionModel().getItems().isEmpty() || getRootType() == null)
        {
            this.lastResultType = null;
        }
        else
        {
            if(this.listView == null)
            {
                ListView listView1 = new ListView();
                listView1.setHeight("100%");
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
                defaultListComponentModel.setActivatable(false);
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
                this.tableController = (ComponentController)createTableController(getSectionModel(), getTableModel(), listView);
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
        }
        return listView;
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
                    getTableModel().getListComponentModel()
                                    .setSelectedIndexesDirectly(getSectionModel().getSelectedIndexes());
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


    public ListSectionModel getSectionModel()
    {
        return (ListSectionModel)super.getSectionModel();
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getSectionModel() != null)
        {
            setVisible(getSectionModel().isVisible());
            getChildren().clear();
            this.sectionGroupBox = createSectionView();
            if(this.sectionGroupBox == null)
            {
                throw new IllegalStateException("Section group box was not created successfully.");
            }
            appendChild((Component)this.sectionGroupBox);
            this.initialized = true;
        }
        return this.initialized;
    }


    protected AdvancedGroupbox createSectionView()
    {
        AdvancedGroupbox groupBox = new AdvancedGroupbox();
        if(getSectionModel().getIcon() != null)
        {
            Div preLabelComponent = groupBox.getPreLabelComponent();
            Image icon = new Image(getSectionModel().getIcon());
            icon.setSclass("advancedGroupboxIcon");
            preLabelComponent.appendChild((Component)icon);
        }
        groupBox.setLabel(getSectionModel().getLabel());
        this.groupBoxContent = new Div();
        this.groupBoxContent.setSclass("browserSectionContent");
        groupBox.appendChild((Component)this.groupBoxContent);
        this.listView = loadListView();
        if(this.listView == null)
        {
            this.groupBoxContent.appendChild((Component)new Label("Nothing to display"));
        }
        else
        {
            this.groupBoxContent.appendChild((Component)this.listView);
        }
        this.captionContainer = groupBox.getCaptionContainer();
        return groupBox;
    }


    public void setActiveItem(TypedObject activeItem)
    {
        if(this.initialized && getTableModel() != null)
        {
            getTableModel().getListComponentModel().setActiveItem(activeItem);
        }
    }


    public void updateActiveItems()
    {
        LOG.info("updateActiveItems");
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        if(this.initialized && this.listView != null)
        {
            this.listView.updateItem(item, modifiedProperties);
        }
    }


    public void updateSelectedItems()
    {
        if(this.initialized && this.listView != null)
        {
            this.listView.updateSelection();
        }
    }


    public void desktopRemoved(Desktop desktop)
    {
        cleanup();
    }


    public void detach()
    {
        super.detach();
        cleanup();
    }


    public void setParent(Component parent)
    {
        super.setParent(parent);
        if(parent == null)
        {
            cleanup();
        }
    }


    private void cleanup()
    {
        if(this.tableController != null)
        {
            this.tableController.unregisterListeners();
        }
        if(getTableModel() != null)
        {
            getTableModel().getColumnComponentModel().removeColumnModelListener(getColumnModelListener());
        }
    }


    protected DefaultListModel<TypedObject> getResultModel()
    {
        return this.resultModel;
    }


    protected void updateResult()
    {
        if(getSectionModel() == null)
        {
            getResultModel().clear();
        }
        else
        {
            Collection<TypedObject> items = getSectionModel().getItems();
            if(items != null)
            {
                getResultModel().clearAndAddAll(items, false, false);
            }
        }
    }


    protected String getListViewConfigurationCode()
    {
        String configCode = null;
        if(getSectionModel().getListViewConfigurationCode() == null)
        {
            configCode = "listViewSection";
        }
        else
        {
            configCode = getSectionModel().getListViewConfigurationCode();
        }
        return configCode;
    }


    protected AbstractTableController createTableController(ListSectionModel listSectionModel, MutableTableModel mutableTableModel, UIListView listView)
    {
        return (AbstractTableController)new DefaultTableController(mutableTableModel, listView);
    }


    protected MutableTableModel getTableModel()
    {
        return getSectionModel().getTableModel();
    }


    protected void setTableModel(MutableTableModel tableModel)
    {
        getSectionModel().setTableModel(tableModel);
    }


    protected ObjectTemplate getRootType()
    {
        return getSectionModel().getRootType();
    }


    protected ListViewConfiguration getListViewConfiguration(ObjectTemplate objectTemplate, String code)
    {
        UIConfigurationService uiConfigService = getUIConfigurationService();
        return (ListViewConfiguration)uiConfigService.getComponentConfiguration(objectTemplate, code, ListViewConfiguration.class);
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
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
            getUIConfigurationService().setLocalComponentConfiguration((UIComponentConfiguration)configuration,
                            UISessionUtils.getCurrentSession().getUser(), getRootType(), getListViewConfigurationCode(), ListViewConfiguration.class);
        }
    }


    protected AdvancedGroupbox getSectionGroupbox()
    {
        return this.sectionGroupBox;
    }
}
