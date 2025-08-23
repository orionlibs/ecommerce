package de.hybris.platform.configurablebundlecockpits.productcockpit.navigationnode.browserarea.list;

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
import de.hybris.platform.configurablebundlecockpits.servicelayer.services.BundleNavigationService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;

public class BundleRelatedItemListController implements ListboxController<TypedObject>
{
    private TypedObject currentNavigationNode;
    private BundleNavigationService bundleNavigationService;
    private SystemService systemService;
    private Set<TypedObject> selectedItems;
    private ModelService modelService;


    public void move(Listbox contentPageList, TypedObject node, TypedObject target)
    {
        Object value = node.getObject();
        if(value instanceof ProductModel)
        {
            ProductModel sourceModel = (ProductModel)value;
            ProductModel targetModel = (ProductModel)target.getObject();
            BundleTemplateModel naviNode = (BundleTemplateModel)this.currentNavigationNode.getObject();
            getBundleNavigationService().move(naviNode, sourceModel, targetModel);
            contentPageList.setModel((ListModel)new BundleRelatedItemListModel(this.currentNavigationNode));
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null, node, null));
        }
    }


    public boolean updateList(Listbox contentPageList)
    {
        boolean ret = false;
        if(this.currentNavigationNode != null &&
                        CollectionUtils.isNotEmpty(((BundleTemplateModel)this.currentNavigationNode.getObject()).getProducts()))
        {
            ret = true;
            contentPageList.setModel((ListModel)new BundleRelatedItemListModel(this.currentNavigationNode));
        }
        return ret;
    }


    public void setCurrentNavigationNode(TypedObject currentNavigationNode)
    {
        this.currentNavigationNode = currentNavigationNode;
    }


    public void delete(Listbox listbox, TypedObject typedObject)
    {
        if(getSystemService().checkAttributePermissionOn("BundleTemplate", "products", "change"))
        {
            ComponentsHelper.displayConfirmationPopup("", Labels.getLabel("general.confirm.delete"), (EventListener)new Object(this, typedObject, listbox));
        }
    }


    protected BundleNavigationService getBundleNavigationService()
    {
        return this.bundleNavigationService;
    }


    @Required
    public void setBundleNavigationService(BundleNavigationService bundleNavigationService)
    {
        this.bundleNavigationService = bundleNavigationService;
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
        browserArea.updateInfoArea((ListProvider)new Object(this, data), false);
        browserArea.getInfoArea().setVisible(browserArea.isOpenInspectorOnSelect());
    }


    protected ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = UISessionUtils.getCurrentSession().getModelService();
        }
        return this.modelService;
    }
}
