package de.hybris.platform.promotionengineservices.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.promotionengine.impl.PromotionEngineServiceBaseTestBase;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;

@IntegrationTest
public class PromotionSourceRuleValidateInterceptorIT extends PromotionEngineServiceBaseTestBase
{
    @Resource
    private ModelService modelService;
    @Resource
    private ConfigurationService configurationService;


    @Test
    public void testCreatePromotionSourceRuleFail()
    {
        this.configurationService.getConfiguration().setProperty("promotionengineservices.maximum.limitation.perrule.enable", "true");
        this.configurationService.getConfiguration().setProperty("promotionengineservices.maximum.conditions.perrule", String.valueOf(1));
        this.configurationService.getConfiguration().setProperty("promotionengineservices.maximum.actions.perrule", String.valueOf(2));
        PromotionSourceRuleModel model = (PromotionSourceRuleModel)this.modelService.create(PromotionSourceRuleModel.class);
        model.setCode("testRule");
        model.setStatus(RuleStatus.UNPUBLISHED);
        model.setConditions("definitionId,definitionId:y_group,definitionId:y_container");
        model.setActions("definitionId12345678definitionId");
        this.modelService.save(model);
        Assert.assertNotNull(model.getUuid());
    }
}
