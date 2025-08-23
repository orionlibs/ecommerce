package de.hybris.platform.cmscockpit.navigationnode.browserarea.list;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cockpit.components.ComponentsHelper;
import de.hybris.platform.cockpit.components.mvc.listbox.Listbox;
import de.hybris.platform.cockpit.components.mvc.listbox.ListboxController;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.util.ListProvider;
import de.hybris.platform.core.model.ItemModel;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;

public class RelatedItemListController implements ListboxController<TypedObject>
{
    private TypedObject currentNavigationNode;
    private CMSNavigationService cmsNavigationService;
    private SystemService systemService;
    private Set<TypedObject> selectedItems;


    public void move(Listbox contentPageList, TypedObject node, TypedObject target)
    {
        Object value = node.getObject();
        if(value instanceof ItemModel)
        {
            ItemModel sourceModel = (ItemModel)value;
            ItemModel targetModel = (ItemModel)target.getObject();
            CMSNavigationNodeModel naviNode = (CMSNavigationNodeModel)this.currentNavigationNode.getObject();
            getCmsNavigationService().move(naviNode, sourceModel, targetModel);
            contentPageList.setModel((ListModel)new RelatedItemListModel(this.currentNavigationNode));
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null, node, null));
        }
    }


    public boolean updateList(Listbox contentPageList)
    {
        boolean ret = false;
        if(this.currentNavigationNode != null && this.currentNavigationNode.getObject() instanceof CMSNavigationNodeModel &&
                        CollectionUtils.isNotEmpty(((CMSNavigationNodeModel)this.currentNavigationNode.getObject()).getEntries()))
        {
            ret = true;
            contentPageList.setModel((ListModel)new RelatedItemListModel(this.currentNavigationNode));
        }
        return ret;
    }


    public void setCurrentNavigationNode(TypedObject currentNavigationNode)
    {
        this.currentNavigationNode = currentNavigationNode;
    }


    public void delete(Listbox listbox, TypedObject typedObject)
    {
        if(getSystemService().checkPermissionOn("CMSNavigationNode", "remove"))
        {
            ComponentsHelper.displayConfirmationPopup("", Labels.getLabel("general.confirm.delete"), (EventListener)new Object(this, typedObject, listbox));
        }
    }


    protected CMSNavigationService getCmsNavigationService()
    {
        return this.cmsNavigationService;
    }


    @Required
    public void setCmsNavigationService(CMSNavigationService cmsNavigationService)
    {
        this.cmsNavigationService = cmsNavigationService;
    }


    protected SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    public Set<TypedObject> getSelected()
    {
        return this.selectedItems;
    }


    public void selected(Listbox component, Set<Listitem> selectedItems)
    {
        TypedObject data = (TypedObject)((Listitem)selectedItems.iterator().next()).getValue();
        AbstractBrowserArea browserArea = (AbstractBrowserArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        browserArea.updateInfoArea((ListProvider)new Object(this, data), true);
        if(browserArea.getInfoArea() != null)
        {
            browserArea.getInfoArea().setVisible(browserArea.isOpenInspectorOnSelect());
        }
    }
}
