package de.hybris.platform.admincockpit.session.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserArea;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class AdmincockpitBrowserArea extends DefaultSearchBrowserArea
{
    public BrowserModel createNewDefaultBrowser()
    {
        BrowserModel browser = null;
        if(StringUtils.isNotBlank(getRootSearchTypeCode()))
        {
            AdmincockpitConstraintBrowserModel admincockpitConstraintBrowserModel2 = new AdmincockpitConstraintBrowserModel(UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getRootSearchTypeCode()));
            admincockpitConstraintBrowserModel2.setSimpleQuery("");
            admincockpitConstraintBrowserModel2.updateItems(0);
            AdmincockpitConstraintBrowserModel admincockpitConstraintBrowserModel1 = admincockpitConstraintBrowserModel2;
        }
        else
        {
            browser = super.createNewDefaultBrowser();
        }
        return browser;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof ItemChangedEvent && ((ItemChangedEvent)event).getItem() != null)
        {
            ItemChangedEvent itemChangedEvent = (ItemChangedEvent)event;
            if(itemChangedEvent.getChangeType() == ItemChangedEvent.ChangeType.CHANGED)
            {
                Object object = itemChangedEvent.getItem().getObject();
                if((object instanceof de.hybris.platform.validation.model.constraints.ConstraintGroupModel || object instanceof de.hybris.platform.validation.model.constraints.AbstractConstraintModel) &&
                                isUpdateItemsNeeded(itemChangedEvent.getProperties()))
                {
                    for(BrowserModel browserModel : getBrowsers())
                    {
                        browserModel.updateItems();
                    }
                }
            }
            else if(itemChangedEvent.getChangeType() == ItemChangedEvent.ChangeType.CREATED)
            {
                for(BrowserModel browserModel : getVisibleBrowsers())
                {
                    browserModel.updateItems();
                }
            }
        }
    }


    private boolean isUpdateItemsNeeded(Set<PropertyDescriptor> propDescriptors)
    {
        for(PropertyDescriptor propDescriptor : propDescriptors)
        {
            if("ConstraintGroup.constraints".equals(propDescriptor.getQualifier()) || "AbstractConstraint.constraintGroups"
                            .equals(propDescriptor.getQualifier()))
            {
                return true;
            }
        }
        return false;
    }
}
