package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.notifier.Notification;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.util.NewItemPersistencePredicate;
import de.hybris.platform.cockpit.wizards.generic.NewItemWizard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;

public class ContextAreaTableModelListener extends DefaultBrowserTableModelListener
{
    private static final Logger LOG = LoggerFactory.getLogger(ContextAreaTableModelListener.class);
    private UICockpitPerspective perspective;


    public ContextAreaTableModelListener(BrowserModel browser, UIListView view)
    {
        super(browser, view);
    }


    public void selectionChanged(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        this.browser.setSelectedContextIndexes(rowIndexes);
        this.view.updateSelection();
    }


    public void cellChanged(int columnIndex, int rowIndex)
    {
        this.perspective = this.browser.getArea().getPerspective();
        TypedObject typedObject = this.browser.getContextRootItem();
        PropertyDescriptor propertyDescriptor = this.browser.getContextRootTypePropertyDescriptor();
        if(this.perspective instanceof de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective && propertyDescriptor != null)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this.browser
                            .getArea(), typedObject, Collections.singleton(propertyDescriptor)));
        }
        this.view.updateCell(columnIndex, rowIndex);
    }


    public void onEvent(String eventName, Object value)
    {
        if("updateitems".equalsIgnoreCase(eventName))
        {
            this.view.updateItems();
        }
        else if("deleteitems".equalsIgnoreCase(eventName) && this.view instanceof ListView)
        {
            ((ListView)this.view).removeSelectedItems();
            this.browser.updateItems();
        }
        else if("additem".equalsIgnoreCase(eventName))
        {
            if(this.browser.getContextRootItem() == null)
            {
                Notification notification = new Notification(Labels.getLabel("contextarea.notification.creationDenied.referenceNull"));
                UISessionUtils.getCurrentSession().getCurrentPerspective().getNotifier().setDialogNotification(notification);
            }
            else if(value instanceof TypedObject)
            {
                InlineItemCreateHelper helper = new InlineItemCreateHelper(this, (TypedObject)value);
                ObjectTemplate contextRootType = this.browser.getContextRootType();
                if(helper.hasAllMandatoryFields() && !contextRootType.isAbstract())
                {
                    List<TypedObject> contextItems = new ArrayList<>();
                    List<TypedObject> currentCtxItems = this.browser.getContextItems();
                    if(currentCtxItems != null)
                    {
                        contextItems.addAll(currentCtxItems);
                    }
                    TypedObject item = helper.createItem();
                    if(item != null)
                    {
                        contextItems.add(item);
                        CollectionUtils.filter(contextItems, (Predicate)new NewItemPersistencePredicate());
                        this.browser.setContextItems(this.browser.getContextRootItem(), contextItems);
                        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this.browser, item, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
                    }
                }
                else
                {
                    NewItemWizard itemWizard = new NewItemWizard(contextRootType, this.view.getPage().getFirstRoot(), (BrowserModel)this.browser);
                    itemWizard.setPredefinedValues(helper.getInitialValues());
                    Map<String, Object> customeParameters = new HashMap<>();
                    customeParameters.put("forceCreateInPopup", Boolean.TRUE);
                    itemWizard.setParameters(customeParameters);
                    itemWizard.setAllowCreate(true);
                    itemWizard.setAllowSelect(false);
                    itemWizard.setDisplaySubTypes(isPossibleAddContextSubTypes());
                    itemWizard.start();
                }
            }
            else
            {
                LOG.warn("Item not added. Reason: Not a typed object.");
            }
        }
        else
        {
            super.onEvent(eventName, value);
        }
    }


    protected boolean isPossibleAddContextSubTypes()
    {
        boolean ret = false;
        UIBrowserArea browserArea = this.browser.getArea();
        if(browserArea instanceof AbstractBrowserArea)
        {
            ret = ((AbstractBrowserArea)browserArea).isPossibleAddContextSubTypes();
        }
        return ret;
    }
}
