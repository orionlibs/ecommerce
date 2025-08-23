package de.hybris.platform.configurablebundlebackoffice.labelproviders;

import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import org.zkoss.util.resource.Labels;

public class DisableProductBundleRuleLabelProvider extends AbstractBundleRuleLabelProvider<DisableProductBundleRuleModel>
{
    private static final String DISABLE_PRODUCT_BUNDLE_RULE_LABEL_KEY = "configurablebundlebackoffice.disableproductbundlerule";


    public String getLabel(DisableProductBundleRuleModel disableProductBundleRuleModel)
    {
        return Labels.getLabel("configurablebundlebackoffice.disableproductbundlerule", new Object[] {getProductNames(disableProductBundleRuleModel.getTargetProducts()),
                        getProductNames(disableProductBundleRuleModel.getConditionalProducts())});
    }
}
