package de.hybris.subscriptionbackoffice.tree;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecorator;
import com.hybris.cockpitng.tree.node.DynamicNodePopulator;
import com.hybris.cockpitng.tree.node.TypeNode;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

public class SubscriptionNodePopulator implements DynamicNodePopulator
{
    private static final String PROMOTION_LEGACY_MODE_PROP_KEY = "promotions.legacy.mode";
    private static final String PROMOTION_BILLING_TIME_RESTRICTION_NODE_ID = "hmc_typenode_promotionbillingtimerestriction";
    private static final String PROMOTION_BILLING_TIME_RESTRICTION_NODE_CODE = "PromotionBillingTimeRestriction";
    private ConfigurationService configurationService;


    public List<NavigationNode> getChildren(NavigationNode navigationNode)
    {
        List<NavigationNode> navigationNodeList = new ArrayList<>();
        Configuration configuration = this.configurationService.getConfiguration();
        if(configuration != null && configuration.getBoolean("promotions.legacy.mode") == true)
        {
            navigationNodeList.add(new NavigationNodeDecorator((NavigationNode)new TypeNode("hmc_typenode_promotionbillingtimerestriction", "PromotionBillingTimeRestriction")));
        }
        navigationNode.getContext().getParameters().forEach((id, code) -> navigationNodeList.add(new NavigationNodeDecorator((NavigationNode)new TypeNode(id, (String)code))));
        return navigationNodeList;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
