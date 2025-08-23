package de.hybris.platform.subscriptioncockpits.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import org.springframework.beans.factory.annotation.Required;

public class AbstractSubscriptionModelLabelProvider<R extends ItemModel> extends AbstractModelLabelProvider<R>
{
    private static final String NULL_TAG = "<null>";
    private L10NService l10NService;


    protected String getUsageChargeModelItemLabel(UsageChargeModel model)
    {
        String usageUnit = "<null>";
        String tiersAndOverage = "";
        if(model == null)
        {
            return getL10NService().getLocalizedString("cockpit.usagecharge.perunit.name", new Object[] {"<null>", "<null>", "<null>"});
        }
        if(model.getUsageUnit() != null)
        {
            usageUnit = model.getUsageUnit().getNamePlural();
        }
        if(model.getUsageChargeEntries() != null)
        {
            tiersAndOverage = getTiersAndOverage(model);
        }
        if(model.getName() == null)
        {
            return getL10NService().getLocalizedString("cockpit.usagecharge.perunit.noname", new Object[] {usageUnit, tiersAndOverage});
        }
        return getL10NService().getLocalizedString("cockpit.usagecharge.perunit.name", new Object[] {model
                        .getName(), usageUnit, tiersAndOverage});
    }


    protected String getTiersAndOverage(UsageChargeModel model)
    {
        int size = model.getUsageChargeEntries().size();
        if(isOverageUsageChargeModel(model))
        {
            if(size == 1)
            {
                return getL10NService().getLocalizedString("cockpit.usagecharge.overage.name");
            }
            return getL10NService().getLocalizedString("cockpit.usagecharge.tiersandoverage.name", new Object[] {Integer.toString(size - 1)});
        }
        return getL10NService().getLocalizedString("cockpit.usagecharge.tiers.name", new Object[] {Integer.toString(size)});
    }


    protected boolean isOverageUsageChargeModel(UsageChargeModel model)
    {
        boolean isOverage = false;
        for(UsageChargeEntryModel usageChargeEntryModel : model.getUsageChargeEntries())
        {
            if(usageChargeEntryModel instanceof de.hybris.platform.subscriptionservices.model.OverageUsageChargeEntryModel)
            {
                isOverage = true;
                break;
            }
        }
        return isOverage;
    }


    protected String getIconPath(R item)
    {
        return null;
    }


    protected String getIconPath(R item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(R item)
    {
        return null;
    }


    protected String getItemDescription(R item, String languageIso)
    {
        return null;
    }


    protected String getItemLabel(R item)
    {
        return null;
    }


    protected String getItemLabel(R item, String languageIso)
    {
        return null;
    }


    @Required
    public void setL10NService(L10NService l10nService)
    {
        this.l10NService = l10nService;
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }
}
