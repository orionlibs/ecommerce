package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.listview.ContextAreaListViewListener;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.listview.ListViewListener;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModelListener;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.List;

public class ContextAreaTableController extends DefaultBrowserTableController
{
    private ContextAreaListViewListener listViewListener = null;


    public ContextAreaTableController(BrowserModel browser, MutableTableModel model, UIListView view)
    {
        super(browser, model, view);
    }


    protected ListViewListener createListViewListener()
    {
        this.listViewListener = new ContextAreaListViewListener(this.browser, this.model, this.view);
        return (ListViewListener)this.listViewListener;
    }


    public void setCreatingItem(boolean creating)
    {
        this.listViewListener.setCreatingItem(creating);
    }


    public boolean isCreatingItem()
    {
        return this.listViewListener.isCreatingItem();
    }


    public void setEditIndexes(List<Integer> editableColumns, int rowIndex)
    {
        this.listViewListener.setEditIndexes(editableColumns, rowIndex);
    }


    protected TableModelListener createTableModelListener()
    {
        return (TableModelListener)new ContextAreaTableModelListener(this.browser, this.view);
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        ItemChangedEvent itemChangedEvent = null;
        AdvancedBrowserModel abm = null;
        if(event instanceof ItemChangedEvent && this.browser instanceof AdvancedBrowserModel)
        {
            itemChangedEvent = (ItemChangedEvent)event;
            abm = (AdvancedBrowserModel)this.browser;
            TypedObject itemChangedTypedObject = itemChangedEvent.getItem();
            if(itemChangedTypedObject != null && itemChangedTypedObject.getObject() instanceof VariantProductModel)
            {
                VariantProductModel variant = (VariantProductModel)itemChangedTypedObject.getObject();
                ProductModel baseProduct = variant.getBaseProduct();
                if(abm.getContextRootItem().getObject() instanceof ProductModel && abm
                                .getContextRootType().getBaseType().equals(itemChangedTypedObject.getType()))
                {
                    ProductModel contextRootItem = (ProductModel)abm.getContextRootItem().getObject();
                    if(baseProduct.equals(contextRootItem))
                    {
                        refreshContextItem(abm, itemChangedEvent);
                    }
                }
            }
        }
    }


    private void refreshContextItem(AdvancedBrowserModel abm, ItemChangedEvent itemChangedEvent)
    {
        List<TypedObject> contextItems = new ArrayList<>(abm.getContextItems());
        removeNotYetPersistedContextItem(contextItems);
        if(itemChangedEvent.getChangeType() == ItemChangedEvent.ChangeType.CHANGED)
        {
            contextItems.remove(itemChangedEvent.getItem());
            contextItems.add(itemChangedEvent.getItem());
        }
        else if(itemChangedEvent.getChangeType() == ItemChangedEvent.ChangeType.CREATED && !contextItems.contains(itemChangedEvent.getItem()))
        {
            contextItems.add(itemChangedEvent.getItem());
        }
        abm.setContextItemsDirectly(abm.getContextRootItem(), contextItems);
    }


    private void removeNotYetPersistedContextItem(List<TypedObject> contextItems)
    {
        TypedObject notYetPersistedContextItem = null;
        for(TypedObject o : contextItems)
        {
            if(UISessionUtils.getCurrentSession().getModelService().isNew(o.getObject()))
            {
                notYetPersistedContextItem = o;
                break;
            }
        }
        if(notYetPersistedContextItem != null)
        {
            contextItems.remove(notYetPersistedContextItem);
        }
    }
}
