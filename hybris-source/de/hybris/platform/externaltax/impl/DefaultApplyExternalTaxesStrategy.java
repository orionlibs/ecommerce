package de.hybris.platform.externaltax.impl;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.externaltax.ApplyExternalTaxesStrategy;
import de.hybris.platform.externaltax.ExternalTaxDocument;
import de.hybris.platform.util.TaxValue;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultApplyExternalTaxesStrategy implements ApplyExternalTaxesStrategy
{
    public void applyExternalTaxes(AbstractOrderModel order, ExternalTaxDocument externalTaxes)
    {
        if(!Boolean.TRUE.equals(order.getNet()))
        {
            throw new IllegalStateException("Order " + order.getCode() + " must be of type NET to apply external taxes to it.");
        }
        BigDecimal entryTaxSum = applyEntryTaxes(order, externalTaxes);
        BigDecimal shippingTaxSum = applyShippingCostTaxes(order, externalTaxes);
        setTotalTax(order, entryTaxSum.add(shippingTaxSum));
    }


    protected BigDecimal applyEntryTaxes(AbstractOrderModel order, ExternalTaxDocument taxDoc)
    {
        BigDecimal totalTax = BigDecimal.ZERO;
        Set<Integer> consumedEntryNumbers = new HashSet<>(taxDoc.getAllTaxes().keySet());
        for(AbstractOrderEntryModel entry : order.getEntries())
        {
            Integer entryNumber = entry.getEntryNumber();
            if(entryNumber == null)
            {
                throw new IllegalStateException("Order entry " + order.getCode() + "." + entry + " does not have a entry number. Cannot apply external tax to it.");
            }
            List<TaxValue> taxesForOrderEntry = taxDoc.getTaxesForOrderEntry(entryNumber.intValue());
            if(taxesForOrderEntry != null)
            {
                for(TaxValue taxForOrderEntry : taxesForOrderEntry)
                {
                    assertValidTaxValue(order, taxForOrderEntry);
                    totalTax = totalTax.add(BigDecimal.valueOf(taxForOrderEntry.getAppliedValue()));
                }
            }
            entry.setTaxValues(taxesForOrderEntry);
            consumedEntryNumbers.remove(entryNumber);
        }
        if(!consumedEntryNumbers.isEmpty())
        {
            throw new IllegalArgumentException("External tax document " + taxDoc + " seems to contain taxes for more lines items than available within " + order
                            .getCode());
        }
        return totalTax;
    }


    protected BigDecimal applyShippingCostTaxes(AbstractOrderModel order, ExternalTaxDocument taxDoc)
    {
        BigDecimal totalTax = BigDecimal.ZERO;
        List<TaxValue> shippingTaxes = taxDoc.getShippingCostTaxes();
        if(shippingTaxes != null)
        {
            for(TaxValue taxForOrderEntry : shippingTaxes)
            {
                assertValidTaxValue(order, taxForOrderEntry);
                totalTax = totalTax.add(BigDecimal.valueOf(taxForOrderEntry.getAppliedValue()));
            }
        }
        order.setTotalTaxValues(shippingTaxes);
        return totalTax;
    }


    protected void setTotalTax(AbstractOrderModel order, BigDecimal totalTaxSum)
    {
        Integer digits = order.getCurrency().getDigits();
        if(digits == null)
        {
            throw new IllegalStateException("Order " + order.getCode() + " has got a currency without decimal digits defined. Cannot apply external taxes.");
        }
        order.setTotalTax(Double.valueOf(CoreAlgorithms.round(totalTaxSum.doubleValue(), digits.intValue())));
    }


    protected void assertValidTaxValue(AbstractOrderModel order, TaxValue value)
    {
        if(!value.isAbsolute())
        {
            throw new IllegalArgumentException("External tax " + value + " is not absolute. Cannot apply it to order " + order
                            .getCode());
        }
        if(!order.getCurrency().getIsocode().equalsIgnoreCase(value.getCurrencyIsoCode()))
        {
            throw new IllegalArgumentException("External tax " + value + " currency " + value.getCurrencyIsoCode() + " does not match order currency " + order
                            .getCurrency().getIsocode() + ". Cannot apply.");
        }
    }
}
