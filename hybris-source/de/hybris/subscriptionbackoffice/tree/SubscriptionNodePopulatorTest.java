package de.hybris.subscriptionbackoffice.tree;

import com.hybris.backoffice.navigation.NavigationNode;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionNodePopulatorTest
{
    private static final String PROMOTION_LEGACY_MODE_PROP_KEY = "promotions.legacy.mode";
    private static final String PROMOTION_BILLING_TIME_RESTRICTION_NODE_ID = "hmc_typenode_promotionbillingtimerestriction";
    @InjectMocks
    private SubscriptionNodePopulator subscriptionNodePopulator;
    @Mock
    private ConfigurationService configurationService;
    @Mock
    private Configuration configuration;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private NavigationNode navigationNode;


    @Before
    public void setUp()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("testId", "testCode");
        Mockito.when(this.configurationService.getConfiguration()).thenReturn(this.configuration);
        Mockito.when(this.navigationNode.getContext().getParameters()).thenReturn(map);
    }


    @Test
    public void shouldReturnSubscriptionBillingTimeRestrictionNodeWhenInLegacyMode()
    {
        Mockito.when(this.configurationService.getConfiguration()).thenReturn(this.configuration);
        Mockito.when(Boolean.valueOf(this.configuration.getBoolean("promotions.legacy.mode"))).thenReturn(Boolean.valueOf(true));
        Assertions.assertThat(this.subscriptionNodePopulator.getChildren(this.navigationNode).size()).isEqualTo(2);
        Assertions.assertThat(((NavigationNode)this.subscriptionNodePopulator.getChildren(this.navigationNode).get(0)).getId()).isEqualTo("hmc_typenode_promotionbillingtimerestriction");
    }


    @Test
    public void shouldNotReturnSubscriptionBillingTimeRestrictionNodeWhenNotinLegacyMode()
    {
        Mockito.when(Boolean.valueOf(this.configuration.getBoolean("promotions.legacy.mode"))).thenReturn(Boolean.valueOf(false));
        Assertions.assertThat(this.subscriptionNodePopulator.getChildren(this.navigationNode).size()).isEqualTo(1);
    }
}
