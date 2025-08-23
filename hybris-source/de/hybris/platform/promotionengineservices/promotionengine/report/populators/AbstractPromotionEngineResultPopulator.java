package de.hybris.platform.promotionengineservices.promotionengine.report.populators;

import com.google.common.base.Preconditions;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.dao.RuleBasedPromotionActionDao;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.AbstractPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResult;
import de.hybris.platform.promotionengineservices.util.ActionUtils;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.util.DiscountValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractPromotionEngineResultPopulator<S, R extends AbstractPromotionEngineResults> implements Populator<S, R>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPromotionEngineResultPopulator.class);
    private RuleBasedPromotionActionDao ruleBasedPromotionActionDao;
    private Populator<PromotionResultModel, PromotionEngineResult> promotionResultPopulator;
    private ActionUtils actionUtils;


    public void populate(S source, R target)
    {
        Preconditions.checkArgument(Objects.nonNull(source), "Source cannot be null");
        Preconditions.checkArgument(Objects.nonNull(target), "Target cannot be null");
        List<PromotionEngineResult> promotionEngineResult = getPromotionEngineResults(source);
        target.setPromotionEngineResults(promotionEngineResult);
    }


    protected List<PromotionEngineResult> getPromotionEngineResults(S source)
    {
        Collection<DiscountValue> discountValues = (Collection<DiscountValue>)getDiscountValues(source).stream().filter(dv -> getActionUtils().isActionUUID(dv.getCode())).collect(Collectors.toList());
        List<AbstractRuleBasedPromotionActionModel> promotions = getRuleBasedPromotionActionDao().findRuleBasedPromotions(getOrder(source), discountValues);
        Map<DiscountValue, List<PromotionResultModel>> discountValue2PromotionResults = (Map<DiscountValue, List<PromotionResultModel>>)discountValues.stream().collect(Collectors.toMap(
                        Function.identity(), dv -> (List)promotions.stream().filter(()).map(AbstractPromotionActionModel::getPromotionResult).collect(Collectors.toList())));
        return (List<PromotionEngineResult>)discountValue2PromotionResults.entrySet().stream()
                        .map(e -> createPromotionEngineResult((DiscountValue)e.getKey(), (List<PromotionResultModel>)e.getValue())).collect(Collectors.toList());
    }


    protected PromotionEngineResult createPromotionEngineResult(DiscountValue discountValue, List<PromotionResultModel> promotionResults)
    {
        Preconditions.checkArgument(Objects.nonNull(discountValue), "Source cannot be null");
        Preconditions.checkArgument(Objects.nonNull(promotionResults), "Target cannot be null");
        PromotionEngineResult promotionEngineResult = new PromotionEngineResult();
        if(promotionResults.size() == 1)
        {
            getPromotionResultPopulator().populate(promotionResults.iterator().next(), promotionEngineResult);
            promotionEngineResult.setDiscountValue(discountValue);
        }
        else
        {
            LOGGER.warn("Cannot find an action corresponding to discount value {}", discountValue);
            promotionEngineResult.setCode("Unable to find corresponding action");
        }
        return promotionEngineResult;
    }


    protected RuleBasedPromotionActionDao getRuleBasedPromotionActionDao()
    {
        return this.ruleBasedPromotionActionDao;
    }


    @Required
    public void setRuleBasedPromotionActionDao(RuleBasedPromotionActionDao ruleBasedPromotionActionDao)
    {
        this.ruleBasedPromotionActionDao = ruleBasedPromotionActionDao;
    }


    protected Populator<PromotionResultModel, PromotionEngineResult> getPromotionResultPopulator()
    {
        return this.promotionResultPopulator;
    }


    @Required
    public void setPromotionResultPopulator(Populator<PromotionResultModel, PromotionEngineResult> promotionResultPopulator)
    {
        this.promotionResultPopulator = promotionResultPopulator;
    }


    protected ActionUtils getActionUtils()
    {
        return this.actionUtils;
    }


    @Required
    public void setActionUtils(ActionUtils actionUtils)
    {
        this.actionUtils = actionUtils;
    }


    protected abstract Collection<DiscountValue> getDiscountValues(S paramS);


    protected abstract AbstractOrderModel getOrder(S paramS);
}
