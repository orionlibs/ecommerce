package de.hybris.platform.ruleengineservices.calculation;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RuleEngineCalculationService
{
    void calculateTotals(AbstractOrderRAO paramAbstractOrderRAO);


    DiscountRAO addOrderLevelDiscount(CartRAO paramCartRAO, boolean paramBoolean, BigDecimal paramBigDecimal);


    DiscountRAO addOrderEntryLevelDiscount(OrderEntryRAO paramOrderEntryRAO, boolean paramBoolean, BigDecimal paramBigDecimal);


    default Set<DiscountRAO> addOrderEntryLevelDiscount(OrderEntryRAO orderEntryRAO, boolean absolute, BigDecimal amount, boolean perUnit)
    {
        return Sets.newHashSet();
    }


    ShipmentRAO changeDeliveryMode(CartRAO paramCartRAO, DeliveryModeRAO paramDeliveryModeRAO);


    FreeProductRAO addFreeProductsToCart(CartRAO paramCartRAO, ProductModel paramProductModel, int paramInt);


    DiscountRAO addFixedPriceEntryDiscount(OrderEntryRAO paramOrderEntryRAO, BigDecimal paramBigDecimal);


    BigDecimal getCurrentPrice(Set<OrderEntryRAO> paramSet, Map<Integer, Integer> paramMap);


    List<DiscountRAO> addFixedPriceEntriesDiscount(CartRAO paramCartRAO, Map<Integer, Integer> paramMap, Set<OrderEntryRAO> paramSet, BigDecimal paramBigDecimal);


    List<DiscountRAO> addOrderEntryLevelDiscount(Map<Integer, Integer> paramMap, Set<OrderEntryRAO> paramSet, boolean paramBoolean, BigDecimal paramBigDecimal);
}
