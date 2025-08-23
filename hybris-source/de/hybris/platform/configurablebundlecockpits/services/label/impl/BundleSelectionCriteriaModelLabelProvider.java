package de.hybris.platform.configurablebundlecockpits.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.PickExactlyNBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.PickNToMBundleSelectionCriteriaModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import org.springframework.beans.factory.annotation.Required;

public class BundleSelectionCriteriaModelLabelProvider extends AbstractModelLabelProvider<BundleSelectionCriteriaModel>
{
    private L10NService l10NService;


    protected String getItemLabel(BundleSelectionCriteriaModel selectionCriteria)
    {
        String label = "";
        if(selectionCriteria instanceof PickNToMBundleSelectionCriteriaModel)
        {
            PickNToMBundleSelectionCriteriaModel model = (PickNToMBundleSelectionCriteriaModel)selectionCriteria;
            label = getL10NService().getLocalizedString("cockpit.bundleselection.pickntom", new Object[] {model
                            .getN(), model.getM()});
        }
        else if(selectionCriteria instanceof PickExactlyNBundleSelectionCriteriaModel)
        {
            PickExactlyNBundleSelectionCriteriaModel model = (PickExactlyNBundleSelectionCriteriaModel)selectionCriteria;
            label = getL10NService().getLocalizedString("cockpit.bundleselection.pickexactly", new Object[] {model
                            .getN()});
        }
        return label;
    }


    protected String getItemLabel(BundleSelectionCriteriaModel selectionCriteria, String languageIso)
    {
        return getItemLabel(selectionCriteria);
    }


    protected String getIconPath(BundleSelectionCriteriaModel item)
    {
        return null;
    }


    protected String getIconPath(BundleSelectionCriteriaModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(BundleSelectionCriteriaModel item)
    {
        return "";
    }


    protected String getItemDescription(BundleSelectionCriteriaModel item, String languageIso)
    {
        return "";
    }


    @Required
    public void setL10NService(L10NService l10NService)
    {
        this.l10NService = l10NService;
    }


    protected L10NService getL10NService()
    {
        return this.l10NService;
    }
}
