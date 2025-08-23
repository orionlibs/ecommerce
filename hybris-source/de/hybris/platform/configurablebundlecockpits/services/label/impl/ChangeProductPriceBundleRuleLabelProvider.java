package de.hybris.platform.configurablebundlecockpits.services.label.impl;

import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import org.springframework.beans.factory.annotation.Required;

public class ChangeProductPriceBundleRuleLabelProvider extends AbstractBundleRuleLabelProvider<ChangeProductPriceBundleRuleModel>
{
    private L10NService l10NService;


    protected String getItemLabel(ChangeProductPriceBundleRuleModel changeProductPriceRule)
    {
        return getL10NService().getLocalizedString("cockpit.bundle.changepricerule", new Object[] {getProductNames(changeProductPriceRule.getTargetProducts()),
                        getProductNames(changeProductPriceRule.getConditionalProducts())});
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
