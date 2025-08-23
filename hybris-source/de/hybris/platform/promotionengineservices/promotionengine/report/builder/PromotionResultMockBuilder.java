package de.hybris.platform.promotionengineservices.promotionengine.report.builder;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.promotionengineservices.constants.PromotionEngineServicesConstants;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.PromotionResultService;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import org.mockito.Mockito;

public class PromotionResultMockBuilder
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";


    public PromotionResultModel createSamplePromotionResult(PromotionResultService promotionResultServiceMock)
    {
        PromotionResultModel promotionResultModel = (PromotionResultModel)Mockito.mock(PromotionResultModel.class);
        RuleBasedPromotionModel promotion = createRuleBasedPromotion("code", "name");
        Mockito.lenient().when(promotionResultModel.getPromotion()).thenReturn(promotion);
        Mockito.lenient().when(promotionResultServiceMock.getDescription(promotionResultModel)).thenReturn("description");
        return promotionResultModel;
    }


    public PromotionResultModel createSamplePotentialPromotionResult(PromotionResultService promotionResultServiceMock)
    {
        PromotionResultModel promotionResultModel = createSamplePromotionResult(promotionResultServiceMock);
        Mockito.lenient().when(promotionResultModel.getCertainty()).thenReturn(PromotionEngineServicesConstants.PromotionCertainty.POTENTIAL.value());
        return promotionResultModel;
    }


    public PromotionResultModel createSampleFiredPromotionResult(PromotionResultService promotionResultServiceMock)
    {
        PromotionResultModel promotionResultModel = createSamplePromotionResult(promotionResultServiceMock);
        Mockito.lenient().when(promotionResultModel.getCertainty()).thenReturn(PromotionEngineServicesConstants.PromotionCertainty.FIRED.value());
        return promotionResultModel;
    }


    public PromotionResultModel createSamplePotentialPromotionResultForOrderEntry(AbstractOrderEntryModel orderEntry)
    {
        PromotionResultModel promotionResultModel = createSamplePromotionResultForOrderEntry(orderEntry);
        Mockito.lenient().when(promotionResultModel.getCertainty()).thenReturn(PromotionEngineServicesConstants.PromotionCertainty.POTENTIAL.value());
        return promotionResultModel;
    }


    public PromotionResultModel createSamplePromotionResultForOrderEntry(AbstractOrderEntryModel orderEntry)
    {
        PromotionResultModel promotionResultModel = (PromotionResultModel)Mockito.mock(PromotionResultModel.class);
        RuleBasedPromotionModel promotion = createRuleBasedPromotion("code", "name");
        Mockito.lenient().when(promotionResultModel.getPromotion()).thenReturn(promotion);
        associateToOrderEntry(promotionResultModel, orderEntry);
        return promotionResultModel;
    }


    public PromotionResultModel createSamplePromotionResultForOrder()
    {
        PromotionResultModel promotionResultModel = (PromotionResultModel)Mockito.mock(PromotionResultModel.class);
        RuleBasedPromotionModel promotion = createRuleBasedPromotion("code", "name");
        Mockito.lenient().when(promotionResultModel.getPromotion()).thenReturn(promotion);
        associateToOrderEntry(promotionResultModel, null);
        return promotionResultModel;
    }


    protected void associateToOrderEntry(PromotionResultModel promotionResultModel, AbstractOrderEntryModel orderEntry)
    {
        PromotionOrderEntryConsumedModel consumedEntry = (PromotionOrderEntryConsumedModel)Mockito.mock(PromotionOrderEntryConsumedModel.class);
        Mockito.lenient().when(promotionResultModel.getConsumedEntries()).thenReturn(Lists.newArrayList((Object[])new PromotionOrderEntryConsumedModel[] {consumedEntry}));
        Mockito.lenient().when(consumedEntry.getOrderEntry()).thenReturn(orderEntry);
    }


    public RuleBasedPromotionModel createRuleBasedPromotion(String code, String name)
    {
        RuleBasedPromotionModel promotion = (RuleBasedPromotionModel)Mockito.mock(RuleBasedPromotionModel.class);
        AbstractRuleEngineRuleModel rule = (AbstractRuleEngineRuleModel)Mockito.mock(DroolsRuleModel.class);
        Mockito.lenient().when(promotion.getRule()).thenReturn(rule);
        Mockito.lenient().when(promotion.getCode()).thenReturn(code);
        Mockito.lenient().when(promotion.getName()).thenReturn(name);
        AbstractRuleModel sourceRule = createSourceRule(code, name);
        Mockito.lenient().when(rule.getSourceRule()).thenReturn(sourceRule);
        return promotion;
    }


    public AbstractRuleModel createSourceRule(String code, String name)
    {
        AbstractRuleModel sourceRule = (AbstractRuleModel)Mockito.mock(SourceRuleModel.class);
        Mockito.lenient().when(sourceRule.getCode()).thenReturn(code);
        Mockito.lenient().when(sourceRule.getName()).thenReturn(name);
        return sourceRule;
    }
}
