package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.UIItemView;
import de.hybris.platform.cockpit.model.general.impl.DefaultListComponentModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultBrowserTableController;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Div;

public abstract class AbstractMainAreaBrowserComponent extends AbstractBrowserComponent implements DesktopRemovalAwareComponent
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMainAreaBrowserComponent.class);
    protected static final String EMPTY_MESSAGE = "browser.emptymessage";
    protected transient Div mainArea = null;
    protected final DefaultListModel<TypedObject> resultModel = new DefaultListModel();
    protected ObjectTemplate lastResultType = null;
    protected UIConfigurationService uiConfigurationService = null;
    protected TypeService typeService = null;
    protected LoginService loginService;
    private Object lastActiveItem;
    protected MutableListModel listComponentModel;


    public AbstractMainAreaBrowserComponent(AdvancedBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super((BrowserModel)model, contentBrowser);
    }


    public AdvancedBrowserModel getModel()
    {
        return (AdvancedBrowserModel)super.getModel();
    }


    protected void resetSelection()
    {
        getModel().setAllMarked(false);
        getModel().setSelectedIndexes(Collections.EMPTY_LIST);
    }


    public boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            resetSelection();
            setHeight("100%");
            setWidth("100%");
            getChildren().clear();
            updateResult();
            Div mainAreaContainer = createMainArea();
            if(mainAreaContainer != null)
            {
                appendChild((Component)mainAreaContainer);
            }
            getContentBrowser().updateStatusBar();
            this.initialized = true;
        }
        return this.initialized;
    }


    public void resize()
    {
    }


    public void setActiveItem(TypedObject activeItem)
    {
        if(this.initialized)
        {
            if(this.listComponentModel != null)
            {
                if(this.lastActiveItem == null || !this.lastActiveItem.equals(activeItem))
                {
                    this.lastActiveItem = activeItem;
                    this.listComponentModel.setActiveItem(activeItem);
                }
            }
        }
    }


    public void updateActiveItems()
    {
        if(this.initialized)
        {
            UIItemView currentItemView = getCurrentItemView();
            if(currentItemView != null)
            {
                currentItemView.updateActiveItems();
            }
        }
    }


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        if(this.initialized)
        {
            UIItemView currentItemView = getCurrentItemView();
            if(currentItemView != null)
            {
                currentItemView.updateItem(item, modifiedProperties);
            }
        }
    }


    public void updateSelectedItems()
    {
        updateActiveItems();
    }


    protected DefaultListModel<TypedObject> getResultModel()
    {
        return this.resultModel;
    }


    protected void updateResult()
    {
        if(getModel() == null)
        {
            getResultModel().clear();
        }
        else
        {
            Collection<TypedObject> items = getModel().getItems();
            if(items != null)
            {
                getResultModel().clearAndAddAll(items, getModel().isItemsRemovable(), getModel().isItemsMovable());
            }
        }
    }


    protected void loadListModel()
    {
        if(this.listComponentModel == null)
        {
            this.listComponentModel = (MutableListModel)new DefaultListComponentModel();
            this.listComponentModel.setEditable(true);
            this.listComponentModel.setSelectable(true);
            this.listComponentModel.setActivatable(true);
            this.listComponentModel.setMultiple(true);
            this.listComponentModel.setListModel((ListModel)getResultModel());
        }
    }


    protected UIConfigurationService getUIConfigurationService()
    {
        if(this.uiConfigurationService == null)
        {
            this.uiConfigurationService = (UIConfigurationService)SpringUtil.getBean("uiConfigurationService");
        }
        return this.uiConfigurationService;
    }


    protected LoginService getLoginService()
    {
        if(this.loginService == null)
        {
            this.loginService = (LoginService)SpringUtil.getBean("loginService");
        }
        return this.loginService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected MutableTableModel getTableModel()
    {
        MutableTableModel tableModel = null;
        if(getModel() != null)
        {
            tableModel = getModel().getTableModel();
        }
        return tableModel;
    }


    protected void setTableModel(MutableTableModel tableModel)
    {
        if(getModel() != null)
        {
            getModel().setTableModel(tableModel);
        }
    }


    protected ObjectTemplate getRootType()
    {
        ObjectTemplate retType = null;
        if(getModel() != null)
        {
            retType = getModel().getLastType();
        }
        return retType;
    }


    protected DefaultBrowserTableController createTableController(AdvancedBrowserModel advancedBrowserModel, MutableTableModel mutableTableModel, UIListView listView)
    {
        return new DefaultBrowserTableController((BrowserModel)advancedBrowserModel, mutableTableModel, listView);
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


    protected abstract UIItemView getCurrentItemView();


    protected abstract Div createMainArea();


    protected abstract void cleanup();
}
