package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.List;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class ShowSynchProductsAction extends AbstractProductAction
{
    protected static final String ICON_SHOW_SYNC_PRODUCTS_ACTION_AVAILABLE = "cockpit/images/icon_func_sync_info_available.png";
    protected static final String ICON_SHOW_SYNC_PRODUCTS_ACTION_UNAVAILABLE = "cockpit/images/icon_func_sync_info_unavailable.png";
    private SynchronizationService synchronizationService;
    private TypeService typeService;


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return "cockpit/images/icon_func_sync_info_available.png";
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            return "cockpit/images/icon_func_sync_info_available.png";
        }
        return "cockpit/images/icon_func_sync_info_unavailable.png";
    }


    public Menupopup getMultiSelectPopup(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            context.setItem(getTypeService().wrapItem(selectedItems.iterator().next()));
            return getPopup(context);
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        Menupopup popupMenu = new Menupopup();
        Menuitem sourcesMenuItem = new Menuitem(Labels.getLabel("synchaction.popupmenu.item.sources"));
        sourcesMenuItem.setParent((Component)popupMenu);
        Menuitem targetsMenuItem = new Menuitem(Labels.getLabel("synchaction.popupmenu.item.targets"));
        targetsMenuItem.setParent((Component)popupMenu);
        addSearchEventListener((Component)targetsMenuItem, (EventListener)new Object(this, context));
        addSearchEventListener((Component)sourcesMenuItem, (EventListener)new Object(this, context));
        return popupMenu;
    }


    public SynchronizationService getSynchronizationService()
    {
        if(this.synchronizationService == null)
        {
            this.synchronizationService = (SynchronizationService)SpringUtil.getBean("synchronizationService");
        }
        return this.synchronizationService;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("gridview.item.searchsynch.tooltip");
    }


    protected void addSearchEventListener(Component component, EventListener listener)
    {
        component.addEventListener("onClick", (EventListener)new Object(this, listener));
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
