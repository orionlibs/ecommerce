package de.hybris.platform.configurablebundlebackoffice.labelproviders;

import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import org.zkoss.util.resource.Labels;

public class ChangeProductPriceBundleRuleLabelProvider extends AbstractBundleRuleLabelProvider<ChangeProductPriceBundleRuleModel>
{
    private static final String CHANGE_PRODUCT_PRICE_BUNDLE_RULE_LABEL_KEY = "configurablebundlebackoffice.changeproductpricebundlerule";


    public String getLabel(ChangeProductPriceBundleRuleModel changeProductPriceBundleRule)
    {
        return Labels.getLabel("configurablebundlebackoffice.changeproductpricebundlerule", new Object[] {getProductNames(changeProductPriceBundleRule.getTargetProducts()),
                        getProductNames(changeProductPriceBundleRule.getConditionalProducts())});
    }
}
