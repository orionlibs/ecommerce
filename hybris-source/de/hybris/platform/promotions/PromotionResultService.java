package de.hybris.platform.promotions;

import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public interface PromotionResultService
{
    boolean isApplied(PromotionResultModel paramPromotionResultModel);


    boolean isAppliedToOrder(PromotionResultModel paramPromotionResultModel);


    boolean getFired(PromotionResultModel paramPromotionResultModel);


    boolean getCouldFire(PromotionResultModel paramPromotionResultModel);


    String getDescription(PromotionResultModel paramPromotionResultModel);


    String getDescription(PromotionResultModel paramPromotionResultModel, Locale paramLocale);


    boolean apply(PromotionResultModel paramPromotionResultModel);


    boolean undo(PromotionResultModel paramPromotionResultModel);


    long getConsumedCount(PromotionResultModel paramPromotionResultModel, boolean paramBoolean);


    double getTotalDiscount(PromotionResultModel paramPromotionResultModel);


    List<PromotionResultModel> getPotentialProductPromotions(PromotionOrderResults paramPromotionOrderResults, AbstractPromotionModel paramAbstractPromotionModel);


    List<PromotionResultModel> getPotentialOrderPromotions(PromotionOrderResults paramPromotionOrderResults, AbstractPromotionModel paramAbstractPromotionModel);


    List<PromotionResultModel> getFiredProductPromotions(PromotionOrderResults paramPromotionOrderResults, AbstractPromotionModel paramAbstractPromotionModel);


    List<PromotionResultModel> getFiredOrderPromotions(PromotionOrderResults paramPromotionOrderResults, AbstractPromotionModel paramAbstractPromotionModel);


    Optional<Set<String>> getCouponCodesFromPromotion(PromotionResultModel paramPromotionResultModel);
}
