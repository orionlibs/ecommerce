package de.hybris.platform.promotions;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PromotionsService
{
    List<ProductPromotionModel> getProductPromotions(Collection<PromotionGroupModel> paramCollection, ProductModel paramProductModel);


    List<ProductPromotionModel> getProductPromotions(Collection<PromotionGroupModel> paramCollection, ProductModel paramProductModel, boolean paramBoolean, Date paramDate);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection, Date paramDate);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection, ProductModel paramProductModel);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection, ProductModel paramProductModel, Date paramDate);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection, boolean paramBoolean);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection, boolean paramBoolean, Date paramDate);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection, boolean paramBoolean, ProductModel paramProductModel);


    List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> paramCollection, boolean paramBoolean, ProductModel paramProductModel, Date paramDate);


    List<? extends AbstractPromotionModel> getAbstractProductPromotions(Collection<PromotionGroupModel> paramCollection, ProductModel paramProductModel);


    List<? extends AbstractPromotionModel> getAbstractProductPromotions(Collection<PromotionGroupModel> paramCollection, ProductModel paramProductModel, boolean paramBoolean, Date paramDate);


    PromotionOrderResults updatePromotions(Collection<PromotionGroupModel> paramCollection, AbstractOrderModel paramAbstractOrderModel);


    PromotionOrderResults updatePromotions(Collection<PromotionGroupModel> paramCollection, AbstractOrderModel paramAbstractOrderModel, boolean paramBoolean, PromotionsManager.AutoApplyMode paramAutoApplyMode1, PromotionsManager.AutoApplyMode paramAutoApplyMode2, Date paramDate);


    PromotionOrderResults getPromotionResults(AbstractOrderModel paramAbstractOrderModel);


    PromotionOrderResults getPromotionResults(Collection<PromotionGroupModel> paramCollection, AbstractOrderModel paramAbstractOrderModel, boolean paramBoolean, PromotionsManager.AutoApplyMode paramAutoApplyMode1, PromotionsManager.AutoApplyMode paramAutoApplyMode2, Date paramDate);


    void cleanupCart(CartModel paramCartModel);


    void transferPromotionsToOrder(AbstractOrderModel paramAbstractOrderModel, OrderModel paramOrderModel, boolean paramBoolean);


    PromotionGroupModel getDefaultPromotionGroup();


    PromotionGroupModel getPromotionGroup(String paramString);


    Collection<AbstractPromotionRestrictionModel> getRestrictions(AbstractPromotionModel paramAbstractPromotionModel);


    String getPromotionDescription(AbstractPromotionModel paramAbstractPromotionModel);
}
