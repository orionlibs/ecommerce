package de.hybris.platform.configurablebundlecockpits.services.label.impl;

import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import org.springframework.beans.factory.annotation.Required;

public class DisableProductBundleRuleLabelProvider extends AbstractBundleRuleLabelProvider<DisableProductBundleRuleModel>
{
    private L10NService l10NService;


    protected String getItemLabel(DisableProductBundleRuleModel disableProductRule)
    {
        return getL10NService().getLocalizedString("cockpit.bundle.disablerule", new Object[] {getProductNames(disableProductRule.getTargetProducts()), getProductNames(disableProductRule.getConditionalProducts())});
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
