package de.hybris.platform.promotions.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.PromotionResultService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class DefaultPromotionResultService extends AbstractPromotionsService implements PromotionResultService
{
    public boolean apply(PromotionResultModel promotionResult)
    {
        boolean result = getResult(promotionResult).apply();
        refreshModifiedModelsAfter(Arrays.asList(new ItemModel[] {(ItemModel)promotionResult}));
        return result;
    }


    public long getConsumedCount(PromotionResultModel promotionResult, boolean includeCouldFirePromotions)
    {
        return getResult(promotionResult).getConsumedCount(includeCouldFirePromotions);
    }


    public boolean getCouldFire(PromotionResultModel promotionResult)
    {
        return getResult(promotionResult).getCouldFire();
    }


    public String getDescription(PromotionResultModel promotionResult)
    {
        return getResult(promotionResult).getDescription();
    }


    public String getDescription(PromotionResultModel promotionResult, Locale locale)
    {
        return getResult(promotionResult).getDescription(locale);
    }


    public boolean getFired(PromotionResultModel promotionResult)
    {
        return getResult(promotionResult).getFired();
    }


    public double getTotalDiscount(PromotionResultModel promotionResult)
    {
        return getResult(promotionResult).getTotalDiscount();
    }


    public boolean isApplied(PromotionResultModel promotionResult)
    {
        return getResult(promotionResult).isApplied();
    }


    public boolean isAppliedToOrder(PromotionResultModel promotionResult)
    {
        return getResult(promotionResult).isAppliedToOrder(getSessionContext());
    }


    public boolean undo(PromotionResultModel promotionResult)
    {
        boolean result = getResult(promotionResult).undo();
        refreshModifiedModelsAfter(Arrays.asList(new ItemModel[] {(ItemModel)promotionResult}));
        return result;
    }


    public List<PromotionResultModel> getPotentialProductPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        return (List<PromotionResultModel>)getModelService().getAll(promoResult.getPotentialProductPromotions(), new ArrayList());
    }


    public List<PromotionResultModel> getPotentialOrderPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        return (List<PromotionResultModel>)getModelService().getAll(promoResult.getPotentialOrderPromotions(), new ArrayList());
    }


    public List<PromotionResultModel> getFiredProductPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        return (List<PromotionResultModel>)getModelService().getAll(promoResult.getFiredProductPromotions(), new ArrayList());
    }


    public List<PromotionResultModel> getFiredOrderPromotions(PromotionOrderResults promoResult, AbstractPromotionModel promotion)
    {
        return (List<PromotionResultModel>)getModelService().getAll(promoResult.getFiredOrderPromotions(), new ArrayList());
    }


    public Optional<Set<String>> getCouponCodesFromPromotion(PromotionResultModel promotionResult)
    {
        return Optional.empty();
    }
}
