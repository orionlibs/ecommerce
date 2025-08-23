package de.hybris.platform.promotionengineservices.promotionengine.report.populators;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderEntryLevelPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderLevelPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResult;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResults;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Required;

public class NonDiscountPromotionEngineResultsClassifyingPopulator implements Populator<AbstractOrderModel, PromotionEngineResults>
{
    private Converter<PromotionResultModel, PromotionEngineResult> promotionResultConverter;


    public void populate(AbstractOrderModel source, PromotionEngineResults target)
    {
        Preconditions.checkArgument(Objects.nonNull(source), "Source cannot be null");
        Preconditions.checkArgument(Objects.nonNull(target), "Target cannot be null");
        Set<PromotionResultModel> nonDiscountPromotions = collectNonDiscountPromotions(source.getAllPromotionResults(), target
                        .getOrderLevelPromotionEngineResults(), target.getOrderEntryLevelPromotionEngineResults());
        List<PromotionEngineResult> nonDiscountPromotionsResult = getPromotionResultConverter().convertAll(nonDiscountPromotions);
        List<PromotionEngineResult> allowedPromotionResults = (List<PromotionEngineResult>)nonDiscountPromotionsResult.stream().filter(promotionEngineResult -> promotionEngineResult.isFired()).collect(Collectors.toList());
        classify(allowedPromotionResults, target);
        cleanup(target);
    }


    protected void cleanup(PromotionEngineResults target)
    {
        if(CollectionUtils.isNotEmpty(target.getOrderEntryLevelPromotionEngineResults()))
        {
            target.getOrderEntryLevelPromotionEngineResults().removeIf(result -> CollectionUtils.isEmpty(result.getPromotionEngineResults()));
        }
    }


    protected void classify(List<PromotionEngineResult> promotionsToClassify, PromotionEngineResults target)
    {
        if(CollectionUtils.isEmpty(promotionsToClassify))
        {
            return;
        }
        Map<Boolean, List<PromotionEngineResult>> groupedPromotionsResult = (Map<Boolean, List<PromotionEngineResult>>)promotionsToClassify.stream().collect(Collectors.groupingBy(this::isOrderEntryRelated));
        List<PromotionEngineResult> orderEntryLevelPromotionsResult = groupedPromotionsResult.get(Boolean.TRUE);
        if(CollectionUtils.isNotEmpty(orderEntryLevelPromotionsResult))
        {
            updateOrderEntryLevelPromotions(orderEntryLevelPromotionsResult, target);
        }
        List<PromotionEngineResult> orderLevelPromotionsResultToAppend = groupedPromotionsResult.get(Boolean.FALSE);
        if(CollectionUtils.isNotEmpty(orderLevelPromotionsResultToAppend))
        {
            updateOrderLevelPromotions(orderLevelPromotionsResultToAppend, target);
        }
    }


    protected void updateOrderEntryLevelPromotions(List<PromotionEngineResult> toAppend, PromotionEngineResults target)
    {
        Map<AbstractOrderEntryModel, List<PromotionEngineResult>> orderEntryRelatedPromotions = (Map<AbstractOrderEntryModel, List<PromotionEngineResult>>)toAppend.stream().collect(Collectors.groupingBy(orderEntry()));
        for(OrderEntryLevelPromotionEngineResults result : target.getOrderEntryLevelPromotionEngineResults())
        {
            List<PromotionEngineResult> promotionsResultToAppend = orderEntryRelatedPromotions.get(result.getOrderEntry());
            if(CollectionUtils.isNotEmpty(promotionsResultToAppend))
            {
                List<PromotionEngineResult> union = CollectionUtils.isNotEmpty(result.getPromotionEngineResults()) ? ListUtils.union(result.getPromotionEngineResults(), promotionsResultToAppend) : promotionsResultToAppend;
                result.setPromotionEngineResults(union);
            }
        }
    }


    protected void updateOrderLevelPromotions(List<PromotionEngineResult> toAppend, PromotionEngineResults target)
    {
        if(CollectionUtils.isNotEmpty(toAppend))
        {
            OrderLevelPromotionEngineResults orderLevelPromotion = target.getOrderLevelPromotionEngineResults();
            List<PromotionEngineResult> promotionEngineResults = orderLevelPromotion.getPromotionEngineResults();
            List<PromotionEngineResult> union = CollectionUtils.isNotEmpty(promotionEngineResults) ? ListUtils.union(promotionEngineResults, toAppend) : toAppend;
            target.getOrderLevelPromotionEngineResults().setPromotionEngineResults(union);
        }
    }


    protected Function<PromotionEngineResult, AbstractOrderEntryModel> orderEntry()
    {
        return promotionResult -> ((PromotionOrderEntryConsumedModel)promotionResult.getPromotionResult().getConsumedEntries().iterator().next()).getOrderEntry();
    }


    protected boolean isOrderEntryRelated(PromotionEngineResult promotionEngineResult)
    {
        Collection<PromotionOrderEntryConsumedModel> consumedEntries = promotionEngineResult.getPromotionResult().getConsumedEntries();
        return (hasConsumedEntries(consumedEntries) && hasSameOrderEntry(consumedEntries));
    }


    protected boolean hasSameOrderEntry(Collection<PromotionOrderEntryConsumedModel> consumedEntries)
    {
        return
                        (consumedEntries.stream().map(PromotionOrderEntryConsumedModel::getOrderEntry).filter(Objects::nonNull).distinct().count() == 1L);
    }


    protected boolean hasConsumedEntries(Collection<PromotionOrderEntryConsumedModel> consumedEntries)
    {
        return CollectionUtils.isNotEmpty(consumedEntries);
    }


    protected Set<PromotionResultModel> collectNonDiscountPromotions(Set<PromotionResultModel> allPromotions, OrderLevelPromotionEngineResults orderDiscountPromotions, List<OrderEntryLevelPromotionEngineResults> orderEntryDiscountPromotions)
    {
        Set<PromotionResultModel> convertedPromotions = collectConvertedPromotionsResult(orderDiscountPromotions, orderEntryDiscountPromotions);
        Set<PromotionResultModel> result = CollectionUtils.isNotEmpty(allPromotions) ? Sets.newHashSet(allPromotions) : Sets.newHashSet();
        result.removeAll(convertedPromotions);
        return result;
    }


    protected Set<PromotionResultModel> collectConvertedPromotionsResult(OrderLevelPromotionEngineResults orderDiscountPromotions, List<OrderEntryLevelPromotionEngineResults> orderEntryDiscountPromotions)
    {
        Stream<PromotionEngineResult> orderPromotions = toPromotionEngineResultsStream(orderDiscountPromotions);
        Stream<PromotionEngineResult> orderEntryPromotions = toPromotionEngineResultsStream(orderEntryDiscountPromotions);
        return (Set<PromotionResultModel>)Stream.<PromotionEngineResult>concat(orderPromotions, orderEntryPromotions).map(PromotionEngineResult::getPromotionResult).collect(Collectors.toSet());
    }


    protected Stream<PromotionEngineResult> toPromotionEngineResultsStream(List<OrderEntryLevelPromotionEngineResults> orderEntryDiscountPromotions)
    {
        return CollectionUtils.isNotEmpty(orderEntryDiscountPromotions) ?
                        orderEntryDiscountPromotions.stream().filter(oedp -> Objects.nonNull(oedp.getPromotionEngineResults())).flatMap(oedp -> oedp.getPromotionEngineResults().stream()) :
                        Stream.<PromotionEngineResult>empty();
    }


    protected Stream<PromotionEngineResult> toPromotionEngineResultsStream(OrderLevelPromotionEngineResults orderDiscountPromotions)
    {
        return (null != orderDiscountPromotions && CollectionUtils.isNotEmpty(orderDiscountPromotions.getPromotionEngineResults())) ?
                        Stream.<List>of(orderDiscountPromotions.getPromotionEngineResults()).flatMap(l -> l.stream()) : Stream.<PromotionEngineResult>empty();
    }


    protected Converter<PromotionResultModel, PromotionEngineResult> getPromotionResultConverter()
    {
        return this.promotionResultConverter;
    }


    @Required
    public void setPromotionResultConverter(Converter<PromotionResultModel, PromotionEngineResult> promotionResultConverter)
    {
        this.promotionResultConverter = promotionResultConverter;
    }
}
