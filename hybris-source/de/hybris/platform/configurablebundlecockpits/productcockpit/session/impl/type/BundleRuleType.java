package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl.type;

public enum BundleRuleType
{
    CHANGE_PRODUCT_PRICE_BUNDLE_RULE("BundlePriceSearchBrowserModel", "ChangeProductPriceBundleRule.bundleTemplate", "ChangeProductPriceBundleRule"),
    DISABLE_PRODUCT_BUNDLE_RULE("BundleDisabledRuleSearchBrowserModel", "DisableProductBundleRule.bundleTemplate", "DisableProductBundleRule");
    private String templateBundleName;
    private String modelName;
    private String ruleName;


    BundleRuleType(String modelName, String templateBundleName, String ruleName)
    {
        this.templateBundleName = templateBundleName;
        this.modelName = modelName;
        this.ruleName = ruleName;
    }


    public String getTemplateBundleName()
    {
        return this.templateBundleName;
    }


    public String getModelName()
    {
        return this.modelName;
    }


    public String getRuleName()
    {
        return this.ruleName;
    }


    public static BundleRuleType fromValue(String v)
    {
        for(BundleRuleType type : values())
        {
            if(type.getRuleName().equals(v))
            {
                return type;
            }
        }
        return null;
    }
}
