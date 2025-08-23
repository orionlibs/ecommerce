package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UniformFixedDiscountDistributeStrategy extends AbstractFixedDiscountDistributeStrategy
{
    protected Map<Integer, BigDecimal> distributeDiscountOnOrderEntries(BigDecimal totalDiscount, List<OrderEntryRAO> triggeredOrderEntries, String currencyIsoCode)
    {
        Map<Integer, BigDecimal> orderEntryPrice = (Map<Integer, BigDecimal>)triggeredOrderEntries.stream().collect(Collectors.toMap(OrderEntryRAO::getEntryNumber, this::getTotalUnconsumedPriceOfOrderEntry));
        BigDecimal totalPrice = orderEntryPrice.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<Integer, BigDecimal> entryDiscount = (Map<Integer, BigDecimal>)orderEntryPrice.keySet().stream().collect(Collectors.toMap(entryNum -> entryNum, entryNum -> {
            BigDecimal price = (BigDecimal)orderEntryPrice.get(entryNum);
            return price.divide(totalPrice, 10, RoundingMode.DOWN).multiply(totalDiscount);
        }));
        entryDiscount.keySet().forEach(entryId -> entryDiscount.put(entryId, getCurrencyUtils().applyRounding(entryDiscount.get(entryId), currencyIsoCode)));
        BigDecimal currencyTotalDiscount = entryDiscount.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        Optional<Integer> digitsOfCurrency = getCurrencyUtils().getDigitsOfCurrency(currencyIsoCode);
        if(digitsOfCurrency.isPresent())
        {
            BigDecimal currencyUnit = BigDecimal.valueOf(1L, ((Integer)digitsOfCurrency.get()).intValue());
            int biasEntryNumber = totalDiscount.subtract(currencyTotalDiscount).divide(currencyUnit).toBigInteger().intValue();
            biasEntryNumber = Math.max(0, biasEntryNumber);
            if(biasEntryNumber > 0)
            {
                entryDiscount.keySet().stream().limit(biasEntryNumber)
                                .forEach(entryId -> entryDiscount.put(entryId, ((BigDecimal)entryDiscount.get(entryId)).add(currencyUnit)));
            }
        }
        return entryDiscount;
    }
}
